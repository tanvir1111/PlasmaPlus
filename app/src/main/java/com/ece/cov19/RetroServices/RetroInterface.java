package com.ece.cov19.RetroServices;

import com.ece.cov19.DataModels.RegDataModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetroInterface {
    @FormUrlEncoded
    @POST("registration.php")
    Call<RegDataModel> sendData(@Field("name") String name,@Field("phone") String phone,@Field("gender") String gender,
                                @Field("blood_group") String bloodGroup,@Field("division") String division,
                                @Field("district") String district, @Field("thana") String thana, @Field("age")String age,
                                @Field("donor") String donorInfo,@Field("password") String password);
}
