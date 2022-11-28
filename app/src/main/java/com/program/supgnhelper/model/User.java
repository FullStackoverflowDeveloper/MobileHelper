package com.program.supgnhelper.model;

import com.program.supgnhelper.model.enums.Role;
import java.util.concurrent.ThreadLocalRandom;

// Класс, описывающий пользователя
public class User {
    private String name, surname, patronym, email, password;
    private Role role;
    private int id;

    public User(){}

    public User(String name, String surname, String patronym, String email, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.patronym = patronym;
        this.email = email;
        this.password = password;
        this.role = role;
        this.id = generateId();
    }

    public String getFullName(){
        return surname + " " + name + " " + patronym;
    }

    public int generateId(){
        return ThreadLocalRandom.current().nextInt(0, 10000 + 1);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setId(int id) {
        this.id = id;
    }
}
