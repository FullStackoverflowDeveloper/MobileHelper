package com.program.supgnhelper.model;

import java.util.ArrayList;
import java.util.List;


// Класс, описывающий улицу
public class Street {
    private String name;
    private int id;
    private List<Home> homes;

    public Street(){}

    public Street(String name, List<Home> list){
        this.name = name;
        id = Model.generateId();
        homes = list;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    public void addHome(Home home){
        if (homes == null) {
            homes = new ArrayList<>();
        }
        homes.add(home);
    }
}
