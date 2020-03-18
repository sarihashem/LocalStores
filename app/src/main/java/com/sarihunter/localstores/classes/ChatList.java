package com.sarihunter.localstores.classes;

public class ChatList {
    private String id;
    private String idReciver;


    public ChatList(){}

    public ChatList(String id, String idReciver) {
        this.id = id;
        this.idReciver = idReciver;
    }

    public ChatList(String emailReciever) {

        this.idReciver = emailReciever;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmailReciever() {
        return idReciver;
    }

    public void setEmailReciever(String emailReciever) {
        this.idReciver = emailReciever;
    }

    @Override
    public String toString() {
        return "ChatList{" +
                "id='" + id + '\'' +
                ", emailReciever='" + idReciver + '\'' +
                '}';
    }
}
