package com.ece.cov19.RetroServices;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroInstance {
    private static Retrofit retrofit=null;

    private static String baseUrl="http://192.168.0.6/scripts/";

    public static RetroInterface getRetro(){
        if(retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();

        }
        return retrofit.create(RetroInterface.class);

    }
}
