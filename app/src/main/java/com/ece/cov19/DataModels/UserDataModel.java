package com.ece.cov19.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDataModel {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("blood_group")
    @Expose
    private String bloodGroup;
    @SerializedName("division")
    @Expose
    private String division;
    @SerializedName("district")
    @Expose
    private String district;
    @SerializedName("thana")
    @Expose
    private String thana;
    @SerializedName("age")
    @Expose
    private String age;
    @SerializedName("donor")
    @Expose
    private String donor;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("eligibility")
    @Expose
    private String eligibility;
    @SerializedName("last_date")
    @Expose
    private String last_date;
    @SerializedName("serverMsg")
    @Expose
    private String serverMsg;

    public UserDataModel(String name, String phone, String gender, String bloodGroup,
                         String division, String district, String thana, String age, String donor, String password, String eligibility, String last_date) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.bloodGroup = bloodGroup;
        this.division = division;
        this.district = district;
        this.thana = thana;
        this.age = age;
        this.donor = donor;
        this.password = password;
        this.eligibility = eligibility;
        this.last_date = last_date;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getThana() {
        return thana;
    }

    public void setThana(String thana) {
        this.thana = thana;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDonor() {
        return donor;
    }

    public void setDonor(String donor) {
        this.donor = donor;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }

    public String getLast_date() {
        return last_date;
    }

    public void setLast_date(String last_date) {
        this.last_date = last_date;
    }

    public String getServerMsg() {
        return serverMsg;
    }

    public void setServerMsg(String serverMsg) {
        this.serverMsg = serverMsg;
    }
}
