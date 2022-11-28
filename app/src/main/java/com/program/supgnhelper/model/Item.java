package com.program.supgnhelper.model;

import android.annotation.SuppressLint;

import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.model.enums.Type;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

// Класс, описывающий задачу, заявку
public class Item {
    private int addressId;
    private String fullAddress;
    private int id;
    private int workerId;
    private String family, name, patronym;
    private String phoneNumber;
    private int plomb, plugPlomb;
    private Date date;
    private Status status;
    private Type type;
    private int numberCounter, previousReadings, currentReadings;
    private String comment;
    private boolean isProblem;


    public Item(){}

    public Item(String family, String name, String patronym, String phoneNumber,  Status status, int addressId, int numberCounter, int plomb, int plugPlomb, int readings){
        this.id = Model.generateId();
        this.family = family;
        this.name = name;
        this.patronym = patronym;
        this.status = status;
        this.phoneNumber = phoneNumber;
        this.addressId = addressId;
        this.numberCounter = numberCounter;
        this.plomb = plomb;
        this.plugPlomb = plugPlomb;
        this.currentReadings = readings;
    }

    public Item(int addressId, String fullAddress, String clientName, String phoneNumber, Status status, int id) {
        this.id = Model.generateId();
        this.workerId = id;
        this.fullAddress = fullAddress;
        this.addressId = addressId;
        this.phoneNumber = phoneNumber;
        this.date = generateDate();
        this.status = status;
        this.numberCounter = 0;
        this.previousReadings = 0;
        this.currentReadings = 0;
        this.plomb = 0;
        this.plugPlomb = 0;
        this.comment = null;
    }

    private Date generateDate(){
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }

    public String generateData(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd MM yyyy");
        return format.format(date);
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getNumberCounter() {
        return numberCounter;
    }

    public void setNumberCounter(int numberCounter) {
        this.numberCounter = numberCounter;
    }

    public void setPlomb(int plomb) {
        this.plomb = plomb;
    }

    public void setPlugPlomb(int plugPlomb) {
        this.plugPlomb = plugPlomb;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public int getWorkerId() {
        return workerId;
    }

    public Map<String, Object> generateMap(){
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("workerId", workerId);
        map.put("addressId", addressId);
        map.put("fullAddress", fullAddress);
        map.put("family", family);
        map.put("name", name);
        map.put("patronym", patronym);
        map.put("phoneNumber", phoneNumber);
        map.put("date", date);
        map.put("status", status);
        map.put("type", type);
        map.put("isProblem", isProblem);
        map.put("numberCounter", numberCounter);
        map.put("previousReadings", previousReadings);
        map.put("currentReadings", currentReadings);
        map.put("plomb", plomb);
        map.put("plugPlomb", plugPlomb);
        map.put("comment", comment);
        return map;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setWorkerId(int workerId) {
        this.workerId = workerId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public int getPlomb() {
        return plomb;
    }

    public int getPlugPlomb() {
        return plugPlomb;
    }

    public Date getDate() {
        return date;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return addressId == item.addressId && id == item.id && workerId == item.workerId && plomb == item.plomb && plugPlomb == item.plugPlomb && numberCounter == item.numberCounter && currentReadings == item.currentReadings && previousReadings == item.previousReadings&& Objects.equals(phoneNumber, item.phoneNumber) && Objects.equals(date, item.date) && status == item.status && Objects.equals(comment, item.comment);
    }

    @Override
    public int hashCode() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isProblem() {
        return isProblem;
    }

    public void setProblem(boolean problem) {
        isProblem = problem;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatronym() {
        return patronym;
    }

    public void setPatronym(String patronym) {
        this.patronym = patronym;
    }

    public int getCurrentReadings() {
        return currentReadings;
    }

    public void setCurrentReadings(int currentReadings) {
        this.currentReadings = currentReadings;
    }

    public int getPreviousReadings() {
        return previousReadings;
    }

    public void setPreviousReadings(int previousReadings) {
        this.previousReadings = previousReadings;
    }

    @Override
    public String toString() {
        return "Item{" +
                "addressId=" + addressId +
                ", fullAddress='" + fullAddress + '\'' +
                ", id=" + id +
                ", workerId=" + workerId +
                ", family='" + family + '\'' +
                ", name='" + name + '\'' +
                ", patronym='" + patronym + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", plomb=" + plomb +
                ", plugPlomb=" + plugPlomb +
                ", date=" + date +
                ", status=" + status +
                ", type=" + type +
                ", numberCounter=" + numberCounter +
                ", previousReadings=" + previousReadings +
                ", currentReadings=" + currentReadings +
                ", comment='" + comment + '\'' +
                ", isProblem=" + isProblem +
                '}';
    }
}