package com.program.supgnhelper.model;

// Класс, описывающий квартиру
public class Flat {
    private int id;
    private int number;

    public Flat(){}

    public Flat(int number){
        this.number = number;
        id = Model.generateId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
