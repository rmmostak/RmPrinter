package com.rmproduct.rmprinteradmin;

public class DailyData {
    private String topic, date, adv, due, bill, pay;

    public DailyData() {
    }

    public DailyData(String topic, String date, String adv, String due, String bill, String pay) {
        this.topic = topic;
        this.date=date;
        this.adv = adv;
        this.due = due;
        this.bill = bill;
        this.pay = pay;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdv() {
        return adv;
    }

    public void setAdv(String adv) {
        this.adv = adv;
    }

    public String getDue() {
        return due;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public String getBill() {
        return bill;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}
