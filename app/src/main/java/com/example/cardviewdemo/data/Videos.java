
package com.example.cardviewdemo.data;

public class Videos {

    String id;
    String name;
    String key;

    public Videos(String id, String name, String key) {
        this.id = id;
        this.key = key;
        this.name = name;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
