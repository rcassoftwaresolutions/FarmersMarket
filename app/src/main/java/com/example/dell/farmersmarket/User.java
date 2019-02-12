package com.example.dell.farmersmarket;

public class User {

    String id,name,ph,email,role;

    public User(String id, String name, String ph, String email, String role) {
        this.id = id;
        this.name = name;
        this.ph = ph;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPh() {
        return ph;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
