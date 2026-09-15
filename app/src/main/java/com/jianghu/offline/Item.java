package com.jianghu.offline;

public class Item {
    private final String id;
    private final String name;
    private final String description;
    private final int heal;
    private final int price;

    public Item(String id, String name, String description, int heal, int price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.heal = heal;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getHeal() { return heal; }
    public int getPrice() { return price; }
}
