package com.ece.cov19.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PatientDataModel {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("hospital")
    @Expose
    private String hospital;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("need")
    @Expose
    private String need;
    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("amountOfBloodNeeded")
    @Expose
    private String amountOfBloodNeeded;
    @SerializedName("serverMsg")
    @Expose
    private String serverMsg;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNeed() {
        return need;
    }

    public void setNeed(String need) {
        this.need = need;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }

    public String getDate() {
        return date;
    }
    public String getAmountOfBloodNeeded() {
        return amountOfBloodNeeded;
    }

    public void setAmountOfBloodNeeded(String amountOfBloodNeeded) {
        this.amountOfBloodNeeded = amountOfBloodNeeded;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public PatientDataModel(String name, String age, String gender, String bloodGroup, String hospital, String division, String district, String phone, String need, String date) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.hospital = hospital;
        this.division = division;
        this.district = district;
        this.phone = phone;
        this.need = need;
        this.date = date;
    }

}
