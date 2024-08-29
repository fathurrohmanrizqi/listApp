package com.example.project11.model;

public class User {
    private long id;
    private String name;
    private String domisili;

    public User(long id, String name, String domisili) {
        this.id = id;
        this.name = name;
        this.domisili = domisili;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomisili() {
        return domisili;
    }

    public void setDomisili(String domisili) {
        this.domisili = domisili;
    }
}
