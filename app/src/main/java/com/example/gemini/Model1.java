package com.example.gemini;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Model1 {
    @Id
    long id;
    List<String> chatgen;
    List<String> user;
    public Model1(){
        chatgen=new ArrayList<>();
        user=new ArrayList<>();
    }
    public void addChatGen(String chatGen) {
        chatgen.add(chatGen);
    }

    public void addUser(String user) {
        this.user.add(user);
    }
}
