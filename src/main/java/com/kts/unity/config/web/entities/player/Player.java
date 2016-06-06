package com.kts.unity.config.web.entities.player;

public class Player {
    
    private int id;
    private String name;
    private String password;
    private String email;

    
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        
        out.append("Player data:\n");
        out.append("ID : " + this.id + " ; name : " + this.name + " ; email : " + this.email);
        
        return out.toString();
    }
    
    
}
