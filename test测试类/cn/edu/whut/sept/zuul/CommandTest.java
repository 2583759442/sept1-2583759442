package cn.edu.whut.sept.zuul;

import junit.framework.TestCase;

public class CommandTest extends TestCase {

    public void testIsUnknown() {
        Command cmd = new Command("build","op");
        if(cmd.isUnknown()){
            System.out.println("isUnknown Accepted");
        }else{
            System.out.println("isUnknown error");
        }
    }

    public void testHasSecondWord() {
    }
}