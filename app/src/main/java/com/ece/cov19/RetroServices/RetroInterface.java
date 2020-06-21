package com.ece.cov19.RetroServices;

import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.UserDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroInterface {
    @FormUrlEncoded
    @POST("registration.php")
    Call<UserDataModel> registerRetroMethod(@Field("name") String name, @Field("phone") String phone, @Field("gender") String gender,
                                            @Field("blood_group") String bloodGroup, @Field("division") String division,
                                            @Field("district") String district, @Field("thana") String thana, @Field("age") String age,
                                            @Field("donor") String donorInfo, @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<UserDataModel> loginRetroMethod(@Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST("updateUserInfo.php")
    Call<UserDataModel> updateUser(@Field("phone") String phone, @Field("name") String name, @Field("division") String division, @Field("district") String district, @Field("thana") String thana, @Field("age") String age, @Field("donor") String donorInfo);

    @FormUrlEncoded
    @POST("updateUserPassword.php")
    Call<UserDataModel> updatePassword(@Field("phone") String loggedInUserPhone, @Field("password") String password);


    @FormUrlEncoded
    @POST("patientData.php")
    Call<PatientDataModel> registerPatientRetro(@Field("name") String name, @Field("age") String age, @Field("gender") String gender,
                                                @Field("blood_group") String bloodGroup, @Field("hospital") String hospital,
                                                @Field("division") String division, @Field("district") String district,
                                                @Field("date") String date, @Field("need") String need,
                                                @Field("phone") String phone);


    @FormUrlEncoded
    @POST("searchDonor.php")
    Call<ArrayList<UserDataModel>> searchDonor(@Field("bloodGroup") String bloodgroup,@Field("district") String district);
    @GET("seeRequest.php") Call<ArrayList<PatientDataModel>> seeRequest();

}


