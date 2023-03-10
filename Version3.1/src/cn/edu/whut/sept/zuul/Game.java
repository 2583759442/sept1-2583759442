/**
 * 该类是“World-of-Zuul”应用程序的主类。
 * 《World of Zuul》是一款简单的文本冒险游戏。用户可以在一些房间组成的迷宫中探险。
 * 你们可以通过扩展该游戏的功能使它更有趣!.
 *
 * 如果想开始执行这个游戏，用户需要创建Game类的一个实例并调用“play”方法。
 *
 * Game类的实例将创建并初始化所有其他类:它创建所有房间，并将它们连接成迷宫；它创建解析器
 * 接收用户输入，并将用户输入转换成命令后开始运行游戏。
 *
 * @author  Michael Kölling and David J. Barnes
 * @version 1.0.0
 */
package cn.edu.whut.sept.zuul;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.*;
import java.util.function.Function;

public class Game
{
    private final Parser parser;
    private Room currentRoom;
    private final Deque<Room> path;
    private final HashMap<Integer,Room> idRoomMap;
    private int roomNum=0;
    private Player player;
    private final HashMap<String, Function<Command, Boolean>> commandList;

    /**
     * 创建一个新的游戏并将解析器和数据进行初始化.
     */
    public Game() throws IOException {
        parser = new Parser();
        path = new ArrayDeque<>();
        idRoomMap = new HashMap<>();
        commandList = new HashMap<>();
        setCommandList();
        createRooms();
    }

    /**
     * 设置游戏的可用命令，
     * 每个命令与对应的方法匹配。
     */
    private void setCommandList(){
        commandList.put("go",this::goRoom);
        commandList.put("back",this::back);
        commandList.put("look",this::look);
        commandList.put("take",this::take);
        commandList.put("drop",this::drop);
        commandList.put("eat-cookie",this::eatCookie);
        commandList.put("items",this::showItems);
        commandList.put("help",this::printHelp);
        commandList.put("quit",this::quit);
    }
    /**
     * 初始游戏地图房间
     */
    private void createRooms()
    {

        Room outside, theater, pub, lab, office, magic;

        // create the rooms
        outside = new Room("outside the main entrance of the university",0);
        theater = new Room("in a lecture theater",0);
        pub = new Room("in the campus pub",0);
        lab = new Room("in a computing lab",0);
        office = new Room("in the computing admin office",0);
        magic = new Room("in a magic room!You will be transported to a random room!", 1);
        idRoomMap.put(outside.getId(), outside);
        idRoomMap.put(theater.getId(), theater);
        idRoomMap.put(pub.getId(), pub);
        idRoomMap.put(lab.getId(), lab);
        idRoomMap.put(office.getId(), office);
        idRoomMap.put(magic.getId(), magic);
        roomNum=6;
        // initialise room exits
        outside.setExit("east", theater.getId());
        outside.setExit("south", lab.getId());
        outside.setExit("west", pub.getId());
        outside.setExit("north",magic.getId());
        outside.addItem("gun","a weapon", 50);

        theater.setExit("west", outside.getId());
        theater.addItem("sword","a weapon", 100);
        theater.addItem("pretext","a armor", 1000);

        pub.setExit("east", outside.getId());

        lab.setExit("north", outside.getId());
        lab.setExit("east", office.getId());

        office.setExit("west", lab.getId());

        magic.setExit("south",outside.getId());
        currentRoom = outside;  // start game outside
        path.addLast(currentRoom);
    }

    /**
     * 用户登陆
     * 获取用户的输入，在数据库中获取该玩家的存档，若没有该玩家则创建一个新的玩家之后开始游戏。
     */
    public void login(){
        System.out.println("Hello!Please entre your userName for loading!");
        System.out.print("> ");
        String input = new Scanner(System.in).nextLine();
        if(input==null){
            System.out.println("Welcome back");
        }else{
            this.player = new Player(input, 0, 1000);
            System.out.println("A new player has been created!");
        }
        this.currentRoom=idRoomMap.get(this.player.getNowRoomId());
        path.add(this.currentRoom);
        play();
    }

    /**
     *  游戏主控循环，直到用户输入退出命令后结束整个程序.
     */
    public void play() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.

        boolean run = true;
        while (run) {
            Command command = parser.getCommand();
            run = processCommand(command);
        }
        System.out.println("Thank you for playing.  Good bye.");
    }

    /**
     * 向用户输出欢迎信息，以及基本的游戏介绍和帮助方法.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Welcome to the World of Zuul!");
        System.out.println("World of Zuul is a new, incredibly boring adventure game.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentRoom.getLongDescription());
    }

    /**
     * 执行用户输入的游戏指令.
     * @param command 用户输出待处理的游戏指令，由解析器从用户输入的内容生成.
     * @return 如果执行的是游戏结束指令，则返回false，否则返回true，并执行指令中的功能.
     */
    private boolean processCommand(Command command) {
        if (command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return true;
        }
        return commandList.get(command.getCommandWord()).apply(command);
    }

    /**
     * 执行help指令，在终端打印游戏帮助信息.
     * @param command 用户命令
     * 此处会输出游戏中用户可以输入的命令列表
     */
    private boolean printHelp(Command command)
    {
        System.out.println("You are lost. You are alone. You wander");
        System.out.println("around at the university.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
        return true;
    }

    /**
     * 执行go指令，向房间的指定方向出口移动，如果该出口连接了另一个房间，则会进入该房间，
     * @param command 用户命令
     * 否则打印输出错误提示信息.
     */
    private boolean goRoom(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
            return true;
        }

        String direction = command.getSecondWord();

        // Try to leave current room.
        int nextId = currentRoom.getExit(direction);
        if(nextId==-1){
            System.out.println("Go where?");
            return true;
        }
        Room nextRoom = idRoomMap.get(nextId);

        if (nextRoom == null) {
            System.out.println("There is no way!");
        }
        else {
            if (nextRoom.getType() == 1) {
                System.out.println("You are "+nextRoom.getShortDescription());
                Random rdm=new Random();
                nextRoom = idRoomMap.get(rdm.nextInt(this.roomNum));

            }
            currentRoom = nextRoom;
            System.out.println(currentRoom.getLongDescription());
        }
        path.addLast(currentRoom);
        return true;
    }

    /**
     * 执行Quit指令，用户退出游戏。如果用户在命令中输入了其他参数，则进一步询问用户是否真的退出.
     * @param command 用户命令
     * @return 如果游戏继续执行则返回true，否则返回false.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * 查看当前房间的基本信息以及存在于房间内的物品信息。
     * @param command 用户命令
     * @return 如果游戏继续执行则返回true，否则返回false.
     */
    private boolean look(Command command){
        System.out.println(currentRoom.getLongDescription());
        currentRoom.showItems();
        return true;
    }

    /**
     * 执行back指令，回退到上一个经过房间。
     * 如果已经回退到游戏开始的状态，则提示已经回到起点。
     * 特别的，如果上一个经过的房间是一个传送房间(magic room)，则返回到进入传送房间前所在的房间。
     * @param command 用户命令
     * @return 如果游戏继续执行则返回true，否则返回false.
     */
    private boolean back(Command command){
        if(path.size()==1){
            System.out.println("You are already at the start!");
            return true;
        }
        path.removeLast();
        currentRoom= path.getLast();
        System.out.println(currentRoom.getLongDescription());
        return true;
    }

    /**
     * 用户需要输入一个数字参数，表示拾取房价内指定编号的物品。
     * 如果参数有误，则会报错。
     * @param command 用户命令
     * @return 如果游戏继续执行则返回true，否则返回false.
     */
    private boolean take(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Take what?");
            return true;
        }
        String thing=command.getSecondWord();
        int id;
        try{
            id=Integer.parseInt(thing);
        }catch (Exception e){
            System.out.println("Take what?");
            return true;
        }
        Item item=currentRoom.delItem(id);
        if(item!=null){
            if(player.carryItem(item)){
                System.out.println("You have taken the "+item.getName());
            }else{
                System.out.println("You are unable to carry it!");
                currentRoom.addItem(item);
            }
        }else{
            System.out.println("No such thing with this id in current room!");
        }
        return true;
    }

    /**
     * 执行drop指令，用户需要输入一个数字参数，表示将背包内指定编号的物品丢弃到当前房间内。
     * 如果参数有误，则会报错。
     * @param command 用户命令
     * @return 如果游戏继续运行则返回true，否则返回false.
     */
    private boolean drop(Command command){
        if(!command.hasSecondWord()) {
            System.out.println("Drop what?");
            return true;
        }
        String things=command.getSecondWord();
        if(things.equals("all")){
            Item item;
            do {
                item=player.dropItem(0);
                if(item!=null){
                    currentRoom.addItem(item);
                }
            }while(item!=null);
            System.out.println("You have dropped everything in your bag!");
            return true;
        }
        String thing=command.getSecondWord();
        int id;
        try{
            id=Integer.parseInt(thing);
        }catch (Exception e){
            System.out.println("Drop what?");
            return true;
        }
        Item item=player.dropItem(id);
        if(item!=null){
            currentRoom.addItem(item);
            System.out.println("You have drop the "+item.getName());
        }
        else{
            System.out.println("No such thing with this id in your bag!");
        }
        return true;
    }

    /**
     * 执行eat-cookie命令，检查背包内是否有物品“魔法饼干”，如果有，则将该物品从背包内移除，
     * 并且增加玩家的承重能力(capacity).
     * @param command 用户命令
     * @return 如果游戏继续运行则返回true，否则返回false.
     */
    private boolean eatCookie(Command command){
        boolean exist = player.eatCookie();
        if(!exist){
            System.out.println("There is no magic cookie in your bag!");
        }
        return true;
    }

    /**
     * 执行items指令，打印当前房间内所有物品，以及玩家随身携带的所有物品及玩家的负重。
     * @param command 用户命令
     * @return 如果游戏继续运行则返回true，否则返回false.
     */
    private boolean showItems(Command command){
        currentRoom.showItems();
        player.showBag();
        return true;
    }
}