package cn.edu.whut.sept.zuul;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {

    public void testCarryItem() {
        Player p1 = new Player("p1",1,100);
        Item book = new Item("name","IQ++",150);
        if(!(p1.carryItem(book))){
            System.out.println("CarryItem Accepted");
        }else{
            System.out.println("CarryItem error");
        }
    }

    public void testDropItem() {
    }

    public void testEatCookie() {
    }
}