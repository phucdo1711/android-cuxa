package com.example.dell.appcuxa.CuxaAPI;

import com.example.dell.appcuxa.FileInfo;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CuXaAPI {
    @FormUrlEncoded
    @POST("auth/facebook")
    Call<UserModel> getInfoUserByFacebook(@Field("access_token") String token);

    @FormUrlEncoded
    @POST("auth/google")
    Call<UserModel> getInfoUserByGoogle(@Field("access_token") String token);

    @Multipart
    @POST("images/upload")
    //Call<List<FileInfo>> postImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part... files);
    Call<ResponseBody> postImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part[] files);

    @DELETE("images/{id}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String authHeader, @Path("id") String id );

    @GET("utilities")
    Call<ResponseBody> getAllUtilities(@Query("sort") String sort);

    @GET("rooms")
    Call<ResponseBody> getListTop(@Header("Authorization") String authHeader,@Header("Content-Type") String content_type);

    @POST("rooms")
    Call<ResponseBody> uploadRoom(@Header("Authorization") String authHeader, @Body RoomObject room);
}
