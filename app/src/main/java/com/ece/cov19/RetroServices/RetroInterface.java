package com.ece.cov19.RetroServices;

import com.ece.cov19.DataModels.DashBoardNumberModel;
import com.ece.cov19.DataModels.ImageDataModel;
import com.ece.cov19.DataModels.PatientDataModel;
import com.ece.cov19.DataModels.RequestDataModel;
import com.ece.cov19.DataModels.UserDataModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetroInterface {

    @GET("appVer.php")
    Call<UserDataModel> latestVersion();

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
    @POST("checkUser.php")
    Call<UserDataModel> checkUser(@Field("phone") String phone);

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
                                                @Field("phone") String phone,@Field("amountOfBloodNeeded") String amountOfBloodNeeded);




    @FormUrlEncoded
    @POST("searchDonor.php")
    Call<ArrayList<UserDataModel>> findDonor(@Field("bloodGroup") String bloodgroup,@Field("district") String district,@Field("phone") String phone,@Field("userDistrict") String userDistrict,@Field("userDivision") String userDivision);


    @FormUrlEncoded
    @POST("searchPatients.php")
    Call<ArrayList<PatientDataModel>> searchPatients(@Field("bloodGroup") String bloodGroup,@Field("district") String district,@Field("phone") String phone,@Field("userDistrict") String userDistrict,@Field("userDivision") String userDivision);

    @FormUrlEncoded
    @POST("dashBoardNumbers.php")
    Call<DashBoardNumberModel> getDashBoardNumbers(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("donorEligibility.php")
    Call<DashBoardNumberModel> eligibilityCheck(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("changeEligibility.php")
    Call<DashBoardNumberModel> eligibilityChange(@Field("phone") String phone, @Field("date") String date);


    @FormUrlEncoded
    @POST("ownPatients.php")
    Call<ArrayList<PatientDataModel>> ownPatients(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("updatePatientProfile.php")
    Call<PatientDataModel> updatePatientProfile(@Field("name") String name, @Field("age") String age, @Field("blood_group") String bloodGroup, @Field("need") String need,
                                                @Field("phone") String phone,

                                                @Field("newname") String newName, @Field("newage") String newaAge, @Field("newgender") String newGender,
                                                @Field("newhospital") String newHospital,
                                                @Field("newdivision") String newDivision, @Field("newdistrict") String newDistrict,
                                                @Field("newdate") String newDate, @Field("newAmountOfBlood") String newAmountOfBlood);

    @FormUrlEncoded
    @POST("deletePatientProfile.php")
    Call<PatientDataModel> deletePatientProfile(@Field("name") String name, @Field("age") String age, @Field("gender") String gender,
                                                @Field("blood_group") String bloodGroup, @Field("hospital") String hospital,
                                                @Field("division") String division, @Field("district") String district,
                                                @Field("date") String date, @Field("need") String need,
                                                @Field("phone") String phone);

    @FormUrlEncoded
    @POST("deleteUserProfile.php")
    Call<UserDataModel> deleteUserProfile( @Field("phone") String phone);



    @FormUrlEncoded
    @POST("sendRequest.php")
    Call<RequestDataModel> sendRequest( @Field("donorPhone") String donorPhone, @Field("patientName") String patientName,
                                        @Field("patientAge") String patientAge, @Field("patientBloodGrp") String patientBloodGrp,
                                        @Field("patientDate") String patientDate, @Field("patientPhone") String patientPhone,
                                        @Field("patientNeed") String patientNeed,
                                        @Field("requestedBy") String requestedBy, @Field("operation") String operation);






    @FormUrlEncoded
    @POST("requestsFromDonorsAlpha.php")
    Call<ArrayList<PatientDataModel>> requestsFromDonorsAlpha(@Field("phone") String phone, @Field("status") String status);

    @FormUrlEncoded
    @POST("requestsFromDonorsBeta.php")
    Call<ArrayList<UserDataModel>> requestsFromDonorsBeta(@Field("name") String name,@Field("age") String age,
                                                       @Field("bloodGroup") String bloodgroup, @Field("phone") String phone,
                                                          @Field("status") String status);

    @FormUrlEncoded
    @POST("responsesFromDonorsAlpha.php")
    Call<ArrayList<PatientDataModel>> responsesFromDonorsAlpha(@Field("phone") String phone, @Field("status") String status);

    @FormUrlEncoded
    @POST("responsesFromDonorsBeta.php")
    Call<ArrayList<UserDataModel>> responsesFromDonorsBeta(@Field("name") String name,@Field("age") String age,
                                                      @Field("bloodGroup") String bloodgroup, @Field("phone") String phone,
                                                           @Field("status") String status);

    @FormUrlEncoded
    @POST("requestsFromPatients.php") Call<ArrayList<PatientDataModel>> requestsFromPatients(@Field("phone") String phone, @Field("status") String status);



    @FormUrlEncoded
    @POST("responsesFromPatients.php") Call<ArrayList<PatientDataModel>> responsesFromPatients(@Field("phone") String phone, @Field("status") String status);






    @FormUrlEncoded
    @POST("checkDonorApproaches.php")
    Call<ArrayList<PatientDataModel>> checkRequestByDonor(@Field("donorPhone") String loggedInUserPhone);

    @FormUrlEncoded
    @POST("getPatientRequest.php")
    Call<ArrayList<UserDataModel>> getPatientRequest(@Field("name") String name,@Field("age") String age,
                                                       @Field("bloodGroup") String bloodgroup, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("lookForRequests.php")
    Call<RequestDataModel> requestsOperation(@Field("donorPhone") String donorPhone, @Field("patientName") String patientName,
                                             @Field("patientAge") String patientAge, @Field("patientBloodGroup") String patientBloodGroup,
                                             @Field("patientDate") String patientDate, @Field("patientPhone") String patientPhone,
                                             @Field("patientNeed") String patientNeed, @Field("requestedBy") String requestedBy,
                                             @Field("operation") String operation);
    @FormUrlEncoded
    @POST("tokenRegister.php")
    Call <UserDataModel> sendToken(@Field("phone") String phone, @Field("token") String token);

    @FormUrlEncoded
    @POST("tokenDelete.php")
    Call <UserDataModel> deleteToken(@Field("phone") String phone, @Field("token") String token);

    @FormUrlEncoded
    @POST("pushNotification.php")
    Call <UserDataModel> sendNotification(@Field("phone") String phone, @Field("title") String title, @Field("body") String body,
                                          @Field("activity") String activity, @Field("hidden") String hidden);

    @FormUrlEncoded
    @POST("checkNotification.php")
    Call <UserDataModel> checkNotification(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("deleteNotification.php")
    Call <UserDataModel> deleteNotification(@Field("phone") String phone);


    @FormUrlEncoded
    @POST("imageUpload.php")
    Call<ImageDataModel> uploadImage(@Field("title") String title, @Field("image") String image);

    @FormUrlEncoded
    @POST("imageDownload.php")
    Call<ImageDataModel> downloadImage(@Field("title") String title);

    @FormUrlEncoded
    @POST("imageDelete.php")
    Call<ImageDataModel> deleteImage(@Field("title") String title);
}


