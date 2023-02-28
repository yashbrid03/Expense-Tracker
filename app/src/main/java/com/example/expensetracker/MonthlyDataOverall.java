package com.example.expensetracker;

public class MonthlyDataOverall {


    public int Totalexpense;
    public int Totalincome;


    public MonthlyDataOverall(){

    }
    public MonthlyDataOverall(int income,int expense){
        this.Totalincome = income;
        this.Totalexpense = expense;
    }
}
