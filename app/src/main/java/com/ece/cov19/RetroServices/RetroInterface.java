package com.ece.cov19.RetroServices;

import com.ece.cov19.DataModels.DashBoardNumberModel;
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
    Call<ArrayList<UserDataModel>> findDonor(@Field("bloodGroup") String bloodgroup,@Field("district") String district,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("checkDonorRequest.php") Call<ArrayList<PatientDataModel>> checkDonorRequest(@Field("phone") String userPhone,@Field("status") String status);

    @FormUrlEncoded
    @POST("searchPatients.php")
    Call<ArrayList<PatientDataModel>> searchPatients(@Field("bloodGroup") String bloodGroup,@Field("district") String district,@Field("phone") String phone);

    @FormUrlEncoded
    @POST("dashBoardNumbers.php")
    Call<DashBoardNumberModel> getDashBoardNumbers(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("ownPatients.php")
    Call<ArrayList<PatientDataModel>> ownPatients(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("updatePatientProfile.php")
    Call<PatientDataModel> updatePatientProfile(@Field("name") String name, @Field("age") String age, @Field("blood_group") String bloodGroup, @Field("phone") String phone,


                                                @Field("newname") String newName, @Field("newage") String newaAge, @Field("newgender") String newGender,
                                                @Field("newblood_group") String newBloodGroup, @Field("newhospital") String newHospital,
                                                @Field("newdivision") String newDivision, @Field("newdistrict") String newDistrict,
                                                @Field("newdate") String newDate, @Field("newneed") String newNeed);

    @FormUrlEncoded
    @POST("deletePatientProfile.php")
    Call<PatientDataModel> deletePatientProfile(@Field("name") String name, @Field("age") String age, @Field("gender") String gender,
                                                @Field("blood_group") String bloodGroup, @Field("hospital") String hospital,
                                                @Field("division") String division, @Field("district") String district,
                                                @Field("date") String date, @Field("need") String need,
                                                @Field("phone") String phone);


    @FormUrlEncoded
    @POST("sendRequest.php")
    Call<RequestDataModel> sendRequest( @Field("donorPhone") String donorPhone, @Field("patientName") String patientName,
                                        @Field("patientAge") String patientAge, @Field("patientPhone") String patientPhone,
                                        @Field("patientBloodGrp") String patientBloodGrp,@Field("requestedBy") String requester);


    @FormUrlEncoded
    @POST("checkPatientRequest.php")
    Call<ArrayList<UserDataModel>> checkPatientRequest(@Field("name") String name,@Field("age") String age,
                                                 @Field("bloodGroup") String bloodgroup, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("getPatientRequest.php")
    Call<ArrayList<UserDataModel>> getPatientRequest(@Field("name") String name,@Field("age") String age,
                                                       @Field("bloodGroup") String bloodgroup, @Field("phone") String phone);

    @FormUrlEncoded
    @POST("lookForRequests.php")
    Call<RequestDataModel> requestsOperation(@Field("donorPhone") String donorPhone, @Field("patientName") String patientName,
                                             @Field("patientAge") String patientAge, @Field("patientBloodGroup") String patientBloodGroup,
                                             @Field("patientPhone") String phone, @Field("requestedBy") String requestedBy, @Field("operation") String operation);

}


