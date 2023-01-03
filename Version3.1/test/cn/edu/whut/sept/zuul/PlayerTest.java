package cn.edu.whut.sept.zuul;

import junit.framework.TestCase;

public class PlayerTest extends TestCase {

    public void testCarryItem() {
        Player p1 = new Player("Jason",1,500);
        Item book = new Item("pretext","a armor",1000);
        if(!(p1.carryItem(book))){
            System.out.println("CarryItem Accepted");
        }else{
            System.out.println("CarryItem error");
        }
    }

}