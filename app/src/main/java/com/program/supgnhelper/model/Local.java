package com.program.supgnhelper.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Класс, описывающий район
public class Local {
    private String name;
    private List<Street> streets = new ArrayList<>();
    private int id;

    public Local(){

    }

    public Local(String name, List<Street> list){
        this.name = name;
        streets = list;
        id = Model.generateId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Street> getStreets() {
        return streets;
    }

    public void setStreets(List<Street> streets) {
        this.streets = streets;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void addStreet(Street street){
        streets.add(street);
    }

    public Map<String, Object> generateMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        map.put("streets", streets);
        return map;
    }
}
