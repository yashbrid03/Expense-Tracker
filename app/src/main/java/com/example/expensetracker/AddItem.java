package com.example.expensetracker;

import java.util.Date;

public class AddItem {

    public String category,type;
    public Date date;
    public int price;

    public AddItem(){}

    public AddItem(String category, Date date,int price, String type){
        this.category = category;
        this.date = date;
        this.price = price;
        this.type = type;
    }
}
