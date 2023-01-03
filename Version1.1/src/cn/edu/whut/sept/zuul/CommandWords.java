
package cn.edu.whut.sept.zuul;

public class CommandWords
{
    /**
     * 命令集
     */
    private static final String[] validCommands = {
            "go", "look", "back", "take", "drop", "eat-cookie", "items", "help", "quit"
    };

    public CommandWords(){
    }

    /**
     * 判断输入的指令是否合法
     * @param aString 经过解析的用户输入的前一部分
     * @return 检查用户输入是否时一个命令集中的命令，若是则返回true，否则返回false
     */
    public boolean isCommand(String aString){
        for (String validCommand : validCommands) {
            if (validCommand.equals(aString))
                return true;
        }
        return false;
    }

    /**
     * 依次输出命令集中的命令
     */
    public void showAll(){
        for(String command: validCommands){
            System.out.print(command + "  ");
        }
        System.out.println();
    }
}
