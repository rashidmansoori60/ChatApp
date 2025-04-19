package com.example.chatapp;

public class Message {
    String msg;
    long date;
    String senderuid;
    String messageid;
    public Message() {
    }



    public Message(long date, String msg, String senderuid) {
        this.date = date;
        this.msg = msg;
        this.senderuid = senderuid;

    }
    public String getMessageid() {
        return messageid;
    }

    public void setMessageid(String messageid) {
        this.messageid = messageid;
    }
    public String getSenderuid() {
        return senderuid;
    }

    public void setSenderuid(String senderuid) {
        this.senderuid = senderuid;
    }






    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }


}
