package com.ece.cov19.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RequestDataModel {

    @SerializedName("donorPhone")
    @Expose
    private String donorPhone;
    @SerializedName("patientName")
    @Expose
    private String patientName;
    @SerializedName("patientAge")
    @Expose
    private String patientAge;
    @SerializedName("patientPhone")
    @Expose
    private String patientPhone;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("serverMsg")
    @Expose
    private String serverMsg;

    public String getServerMsg() {
        return serverMsg;
    }

    public String getDonorPhone() {
        return donorPhone;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getPatientAge() {
        return patientAge;
    }

    public String getPatientPhone() {
        return patientPhone;
    }

    public String getStatus() {
        return status;
    }


    public RequestDataModel(String donorPhone, String patientName, String patientAge, String patientPhone) {
        this.donorPhone = donorPhone;
        this.patientName = patientName;
        this.patientAge = patientAge;
        this.patientPhone = patientPhone;
    }
}
