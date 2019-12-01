package com.mridx.accountsmanage.dataholder;

public class ExpensesData {

    private String time, date, type, amount;

    public ExpensesData(String time, String date, String type, String amount) {
        this.time = time;
        this.date = date;
        this.type = type;
        this.amount = amount;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    public String getAmount() {
        return amount;
    }
}
