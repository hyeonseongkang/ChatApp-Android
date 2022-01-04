package com.example.chatapp;

class ChatData {

    private String userName;
    private String time;
    private String message;

    public ChatData() {}

    public ChatData(String userName, String time, String message) {
        this.userName = userName;
        this.time = time;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
