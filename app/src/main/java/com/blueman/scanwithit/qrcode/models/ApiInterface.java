package com.blueman.scanwithit.qrcode.models;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("register.php")
    Call<Student> registerStudent(
            @Field("student_name") String student_name,
            @Field("student_email") String student_email,
            @Field("security_word") String security_word,
            @Field("student_class") String student_class,
            @Field("QR_code") String QR_code,
            @Field("student_pass") String student_pass

    );
}