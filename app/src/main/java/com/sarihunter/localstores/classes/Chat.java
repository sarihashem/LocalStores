package com.sarihunter.localstores.classes;

public class Chat {


   private String msg;
   private String sender;
   private String reciever;
   private String timeStamp;



   public Chat(){
   }

    public Chat(String sender, String reciever,String msg , String timeStamp) {
        this.msg = msg;
        this.sender = sender;
        this.reciever = reciever;
        this.timeStamp = timeStamp;
    }

    public Chat(String sender, String reciever, String msg){
       this.msg = msg;
       this.reciever = reciever;
       this.sender = sender;

   }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }
}
