package com.rmproduct.rmprinteradmin;


class PrinterInfo {
    String roll, due, advance, uid, bill, pay;

    public PrinterInfo() {
    }

    public PrinterInfo(String roll, String due, String advance, String uid, String bill, String pay) {
        this.roll = roll;
        this.due = due;
        this.advance = advance;
        this.uid = uid;
        this.bill = bill;
        this.pay = pay;
    }

    public String getRoll() {
        return roll;
    }

    public String getDue() {
        return due;
    }

    public String getAdvance() {
        return advance;
    }

    public String getUid() {
        return uid;
    }

    public String getBill() {
        return bill;
    }

    public String getPay() {
        return pay;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public void setDue(String due) {
        this.due = due;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setBill(String bill) {
        this.bill = bill;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }
}

