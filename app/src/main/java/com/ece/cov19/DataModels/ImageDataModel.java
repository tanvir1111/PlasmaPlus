package com.ece.cov19.DataModels;
import com.google.gson.annotations.SerializedName;
public class ImageDataModel {


    @SerializedName("title")
        private String Title;

    @SerializedName("image")
        private String Image;


    @SerializedName("serverMsg")
        private String serverMsg;

    public String getServerMsg() {
        return serverMsg;
    }

    public String getTitle() {
        return Title;
    }

    public String getImage() {
        return Image;
    }
}
