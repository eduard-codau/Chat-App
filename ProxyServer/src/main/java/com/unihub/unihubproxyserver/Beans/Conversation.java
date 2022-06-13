package com.unihub.unihubproxyserver.Beans;

public class Conversation {
    private int userId;
    private String username;

    public Conversation()
    {

    }


    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername( String username) {
        this.username = username;
    }

}
