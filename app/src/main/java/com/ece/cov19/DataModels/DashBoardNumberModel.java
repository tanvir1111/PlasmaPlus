package com.ece.cov19.DataModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardNumberModel {
    @SerializedName("numberOfDonors")
    @Expose
    private String numberOfDonors;

    @SerializedName("numberOfPatients")
    @Expose
    private String numberOfPatients;

    @SerializedName("numberOfRequests")
    @Expose
    private String numberOfRequests;

    @SerializedName("numberOfResponses")
    @Expose
    private String numberOfResponses;

    public String getNumberOfDonors() {
        return numberOfDonors;
    }

    public String getNumberOfPatients() {
        return numberOfPatients;
    }

    public String getNumberOfRequests() {
        return numberOfRequests;
    }

    public String getNumberOfResponses() {
        return numberOfResponses;
    }
}
