
package cn.edu.whut.sept.zuul;

public class Item {
    private String name;

    private String description;

    private int weight;

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public int getWeight(){
        return weight;
    }

    /**
     * 创建一个物品
     * @param name 物品名称
     * @param description 物品描述
     * @param weight 物品重量
     */
    public Item(String name, String description, int weight){
        this.name=name;
        this.description=description;
        this.weight=weight;
    }
}
