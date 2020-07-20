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

    @SerializedName("numberOfMyPatients")
    @Expose
    private String numberOfMyPatients;


    @SerializedName("numberOfRequestsFromDonors")
    @Expose
    private String numberOfRequestsFromDonors;
    @SerializedName("numberOfRequestsFromPatients")
    @Expose
    private String numberOfRequestsFromPatients;

    @SerializedName("numberOfResponsesFromDonors")
    @Expose
    private String numberOfResponsesFromDonors;
    @SerializedName("numberOfResponsesFromPatients")
    @Expose
    private String numberOfResponsesFromPatients;

    @SerializedName("eligibility")
    @Expose
    private String eligibility;
    @SerializedName("lastdate")
    @Expose
    private String lastdate;

    @SerializedName("serverMsg")
    @Expose
    private String serverMsg;

    public String getNumberOfMyPatients() {
        return numberOfMyPatients;
    }

    public String getNumberOfDonors() {
        return numberOfDonors;
    }

    public String getNumberOfPatients() {
        return numberOfPatients;
    }

    public String getNumberOfRequestsFromDonors() {
        return numberOfRequestsFromDonors;
    }

    public String getNumberOfRequestsFromPatients() {
        return numberOfRequestsFromPatients;
    }

    public String getNumberOfResponsesFromDonors() {
        return numberOfResponsesFromDonors;
    }

    public String getNumberOfResponsesFromPatients() {
        return numberOfResponsesFromPatients;
    }

    public String getEligibility() {
        return eligibility;
    }

    public String getLastdate() {
        return lastdate;
    }

    public String getServerMsg() {
        return serverMsg;
    }
}
