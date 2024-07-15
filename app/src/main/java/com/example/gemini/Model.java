package com.example.gemini;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Model {
    @Id
    long id;
    String user;
    String chatGen;
    String name;

    public void setChatGen(String chatGen) {
        this.chatGen = chatGen;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
