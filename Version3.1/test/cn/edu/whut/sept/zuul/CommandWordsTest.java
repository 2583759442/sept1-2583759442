package cn.edu.whut.sept.zuul;

import junit.framework.TestCase;

public class CommandWordsTest extends TestCase {

    public void testIsCommand() {
        CommandWords cmdw=new CommandWords();
        if(cmdw.isCommand( "help")&&!cmdw.isCommand("build")){
            System.out.println("isCommand Accepted");
        }else{
            System.out.println("isCommand error");
        }
    }

    public void testShowAll() {

    }
}