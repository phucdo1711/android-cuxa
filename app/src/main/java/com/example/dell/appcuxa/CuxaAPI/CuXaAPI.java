package com.example.dell.appcuxa.CuxaAPI;

import com.example.dell.appcuxa.FileInfo;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.RoomSearch;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.ObjectModels.SavedRoom;
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
    /**
     * Đăng nhập FB
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("auth/facebook")
    Call<UserModel> getInfoUserByFacebook(@Field("access_token") String token);

    /**
     * Đăng nhập Google
     * @param token
     * @return
     */
    @FormUrlEncoded
    @POST("auth/google")
    Call<UserModel> getInfoUserByGoogle(@Field("access_token") String token);

    @Multipart
    @POST("images/upload")
    //Call<List<FileInfo>> postImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part... files);
    Call<ResponseBody> postImage(@Header("Authorization") String authHeader, @Part MultipartBody.Part[] files);

    /**
     * Truyền id ảnh vào link để xóa
     * @param authHeader
     * @param id
     * @return
     */
    @DELETE("images/{id}")
    Call<ResponseBody> deleteImage(@Header("Authorization") String authHeader, @Path("id") String id );

    /**
     * Lấy ra dữ liệu của tiện nghi
     * @param sort
     * @return
     */
    @GET("utilities")
    Call<ResponseBody> getAllUtilities(@Query("sort") String sort);

    @GET("rooms")
    Call<ObjectListByOption> getMyRooms(@Header("Authorization") String authHeader,
                                        @Header("Content-Type") String content_type,
                                        @Query("landlord") String landlord);
    @GET("rooms")
    Call<ObjectListByOption> getListTop(@Header("Authorization") String authHeader,@Header("Content-Type") String content_type);

    @GET("rooms")
    Call<ObjectListByOption> getPeople(@Header("Authorization") String authHeader, @Query("type") String type);

    @POST("rooms")
    Call<ResponseBody> uploadRoom(@Header("Authorization") String authHeader, @Body RoomObject room);

    @POST("rooms/search")
    Call<RoomSearchResult> searchRoom(@Header("Authorization") String authHeader, @Body RoomSearch room);

    @DELETE("rooms/{id}")
    Call<ResponseBody> deleteRoom(@Header("Authorization") String authHeader, @Path("id") String id );

    @GET("rooms/{id}/save")
    Call<ResponseBody> saveRoom(@Header("Authorization") String authHeader,
                                       /* @Header("Content-Type") String content_type,*/
                                        @Path("id") String id);
    @GET("rooms/saved")
    Call<SavedRoom[]> getLstSavedRoom(@Header("Authorization") String authHeader);
}
