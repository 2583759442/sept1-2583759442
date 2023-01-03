/**
 * 该类用于存储解析后的用户命令
 * 用户命令包含两部分：指令（必要）和参数（非必要）
 */

package cn.edu.whut.sept.zuul;

public class Command{
    private String commandWord;
    private String secondWord;

    /**
     * 创建一个命令
     * @param firstWord 指令（CommandWord）
     * @param secondWord 参数
     */
    public Command(String firstWord, String secondWord){
        commandWord = firstWord;
        this.secondWord = secondWord;
    }

    /**
     * 获取输入的指令词
     * @return 返回输入中的 firstWord指令（CommandWord）
     */
    public String getCommandWord(){
        return commandWord;
    }

    /**
     * 获取输入的参数词
     * @return 返回输入中的 secondWord参数
     */
    public String getSecondWord(){
        return secondWord;
    }

    /**
     * 检查输入中是否有合法指令
     * @return 若指令存在则返回true，否则返回false
     */
    public boolean isUnknown(){
        return (commandWord == null);
    }

    /**
     * 检查该条输入命令种是否有参数
     * @return 若有参数则返回true，否则返回false
     */
    public boolean hasSecondWord(){
        return (secondWord != null);
    }
}
