<<<<<<< HEAD
=======
/**
 * 该类用于获取并解析用户输入
 */

>>>>>>> 22b5a0c (Version1.1)
package cn.edu.whut.sept.zuul;

import java.util.Scanner;

public class Parser
{
    private CommandWords commands;
    private Scanner reader;

<<<<<<< HEAD
    public Parser()
    {
=======
    /**
     * 创建一个解析器
     */
    public Parser(){
>>>>>>> 22b5a0c (Version1.1)
        commands = new CommandWords();
        reader = new Scanner(System.in);
    }

<<<<<<< HEAD
=======
    /**
     * 读入用户从控制台输入的命令，并将命令分为两部分：命令和参数（其中参数可能不存在）
     * @return 一个解析后的命令
     */
>>>>>>> 22b5a0c (Version1.1)
    public Command getCommand()
    {
        String inputLine;
        String word1 = null;
        String word2 = null;

        System.out.print("> ");

        inputLine = reader.nextLine();

        Scanner tokenizer = new Scanner(inputLine);
        if(tokenizer.hasNext()) {
<<<<<<< HEAD
            word1 = tokenizer.next();   
=======
            word1 = tokenizer.next();
>>>>>>> 22b5a0c (Version1.1)
            if(tokenizer.hasNext()) {
                word2 = tokenizer.next();
            }
        }

        if(commands.isCommand(word1)) {
            return new Command(word1, word2);
        }
        else {
            return new Command(null, word2);
        }
    }

<<<<<<< HEAD
    public void showCommands()
    {
=======
    /**
     * 展示玩家输入的完整命令
     */
    public void showCommands(){
>>>>>>> 22b5a0c (Version1.1)
        commands.showAll();
    }
}
