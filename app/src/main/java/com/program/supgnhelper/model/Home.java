package com.program.supgnhelper.model;

import java.util.ArrayList;
import java.util.List;

// Класс, описывающий дом
public class Home {
    private int id;
    private boolean isFlat;
    private List<Flat> flats;
    private int number;

    public Home(){}

    public Home(boolean flat, int number){
        isFlat = flat;
        this.number = number;
        id = Model.generateId();
    }

    public Home(boolean flat, List<Flat> flats, int number){
        this.flats = flats;
        isFlat = flat;
        this.number = number;
        id = Model.generateId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFlat() {
        return isFlat;
    }

    public void setFlat(boolean flat) {
        isFlat = flat;
    }

    public List<Flat> getFlats() {
        if (flats == null){
            flats = new ArrayList<>();
        }
        return flats;
    }

    public void setFlats(List<Flat> flats) {
        this.flats = flats;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void addFlat(Flat flat){
        if (flats == null){
            flats = new ArrayList<>();
        }
        flats.add(flat);
    }
}
