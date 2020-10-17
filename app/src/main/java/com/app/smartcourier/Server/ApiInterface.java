package com.app.smartcourier.Server;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.Model.BranchManager;
import com.app.smartcourier.Model.OtherInfo;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("signup.php")
    Call<User> signUp(
            @Field(Config.NAME) String userName,
            @Field(Config.EMAIL) String email,
            @Field(Config.CONTACT) String contact,
            @Field(Config.PASSWORD) String password
    );

    //for upload image and info
    //for signup
    //for login
    @FormUrlEncoded
    @POST("signin.php")
    Call<User> signIn(
            @Field(Config.CONTACT) String contact,
            @Field(Config.PASSWORD) String password);
    @FormUrlEncoded
    @POST("signin.php")
    Call<BranchManager> managerSignIn(
            @Field(Config.CONTACT) String contact,
            @Field(Config.PASSWORD) String password);
    @FormUrlEncoded
    @POST("submit_parcel.php")
    Call<Parcel> submitParcel(
            @Field("tracking_id") String tracking_id,
            @Field("contact_no") String contact_no,
            @Field("title") String title,
            @Field("description") String desc,
            @Field("location") String location,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude,
            @Field("payment_method") String payment_method,
            @Field("branch") String branch,
            @Field("time") String time,
            @Field("date") String date,
            @Field("status") String status);

    @FormUrlEncoded
    @POST("manager_submit_parcel.php")
    Call<Parcel> managerSubmitParcel(
            @Field("tracking_id") String tracking_id,
            @Field("contact_no") String contact_no,
            @Field("title") String title,
            @Field("description") String desc,
            @Field("location") String location,
            @Field("payment_method") String payment_method,
            @Field("branch") String branch,
            @Field( "dest_branch")  String dest_branch,
            @Field("time") String time,
            @Field("date") String date,
            @Field("status") String status);
    @FormUrlEncoded
    @POST("manager_update_parcel.php")
    Call<Parcel> managerUpdateParcel(
            @Field("tracking_id") String tracking_id,
            @Field("contact_no") String contact_no,
            @Field("title") String title,
            @Field("description") String desc,
            @Field("location") String location,
            @Field("payment_method") String payment_method,
            @Field("branch") String branch,
            @Field("time") String time,
            @Field("date") String date,
            @Field("status") String status);
    @FormUrlEncoded
    @POST("submit_payment.php")
    Call<Payment> submitPayment(
            @Field( "tracking_id")  String tracking_id,
            @Field( "bkash_trx_id")  String bkash_trx_id,
            @Field( "bkash_number")  String bkash_number,
            @Field( "contact_no")  String contact_no,
            @Field( "amount")  String amount,
            @Field( "branch")  String branch,
            @Field( "payment_status")  String paymentStatus
    );

    @FormUrlEncoded
    @POST("manager_submit_payment.php")
    Call<Payment> managerSubmitPayment(
            @Field( "tracking_id")  String tracking_id,
            @Field( "bkash_trx_id")  String bkash_trx_id,
            @Field( "bkash_number")  String bkash_number,
            @Field( "contact_no")  String contact_no,
            @Field( "amount")  String amount,
            @Field( "branch")  String branch,
            @Field( "date")  String date,
            @Field( "time")  String time,
            @Field( "payment_status")  String paymentStatus
    );

    @FormUrlEncoded
    @POST("update_profile.php")
    Call<User> updateProfile(
            @Field(Config.NAME) String name,
            @Field(Config.EMAIL) String email,
            @Field(Config.PASSWORD) String password);

    @FormUrlEncoded
    @POST("update_manager.php")
    Call<BranchManager> updateManager(
            @Field("request") String request);
    @FormUrlEncoded
    @POST("manager_update_payment.php")
    Call<Payment> updateManagerPayment(
            @Field("tracking_id") String tracking_id,
            @Field("payment_status") String payment_status);

    @GET("get_user_profile.php")
    Call<List<User>> getProfile(
            @Query("contact") String contact
    );
    @GET("get_manager_data.php")
    Call<List<BranchManager>> getManagerProfile(
            @Query("contact") String contact
    );

    @GET("get_payment_data.php")
    Call<List<Payment>> getPaymentData(
            @Query("contact_no") String contact,
            @Query("tracking_id") String tracking_id
    );

    @GET("get_branched_payment_data.php")
    Call<List<Payment>> getManagerPayment(
            @Query("branch") String branch
    );

    @GET("get_parcel_data.php")
    Call<List<Parcel>> getParcelData(
            @Query("contact_no") String contact,
            @Query("tracking_id") String tracking_id


    );
    @GET("get_Branch.php")
    Call<List<Branch>> getBranchData();
    @GET("get_branched_parcel_request_data.php")
    Call<List<Parcel>> getBranchedParcel(
            @Query("branch") String branch
    );
    @GET("get_manager_parcel_data.php")
    Call<List<Parcel>> getManagerParcel(
            @Query("branch") String branch
    );

}