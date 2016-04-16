package com.mallardduckapps.akbankir.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oguzemreozcan on 06/03/16.
 */
public class Person {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("portrait")
    private String portait;
    @SerializedName("email")
    private String email;
    @SerializedName("phone")
    private String phone;
    @SerializedName("position")
    private String position;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortait() {
        return portait;
    }

    public void setPortait(String portait) {
        this.portait = portait;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
