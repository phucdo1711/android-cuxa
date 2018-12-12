package com.example.dell.appcuxa.CuxaAPI;

import com.example.dell.appcuxa.ObjectModels.ChatObject;
import com.example.dell.appcuxa.ObjectModels.ChatRoomObj;
import com.example.dell.appcuxa.ObjectModels.CommentContent;
import com.example.dell.appcuxa.ObjectModels.ContentObject;
import com.example.dell.appcuxa.ObjectModels.NotiModel;
import com.example.dell.appcuxa.ObjectModels.NotiObject;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.ObjectModels.ObjectListByOption;
import com.example.dell.appcuxa.ObjectModels.RoomCreatedObj;
import com.example.dell.appcuxa.ObjectModels.RoomObject;
import com.example.dell.appcuxa.ObjectModels.RoomSearch;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.ObjectModels.RoomSearchResult;
import com.example.dell.appcuxa.ObjectModels.UpdateUserObj;
import com.example.dell.appcuxa.ObjectModels.UserModel;

import okhttp3.MultipartBody;
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
import retrofit2.http.PUT;
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

    @PUT("rooms/{id}")
    Call<ResponseBody> updateRoom(@Header("Authorization") String authHeader, @Path("id") String id, @Body RoomObject room);

    @POST("rooms/search")
    Call<RoomSearchResult> searchRoom(@Header("Authorization") String authHeader, @Body RoomSearch room);

    @DELETE("rooms/{id}")
    Call<ResponseBody> deleteRoom(@Header("Authorization") String authHeader, @Path("id") String id );

    @DELETE("chat-rooms/{id}")
    Call<ResponseBody> deleteChatRoom(@Header("Authorization") String authHeader, @Path("id") String id );

    @DELETE("notifications/{id}")
    Call<ResponseBody> deleteNoti(@Header("Authorization") String authHeader, @Path("id") String id );

    @GET("rooms/{id}/save")
    Call<ResponseBody> saveRoom(@Header("Authorization") String authHeader,
                                       /* @Header("Content-Type") String content_type,*/
                                        @Path("id") String id);
    @GET("rooms/saved")
    Call<RoomSearchItem[]> getLstSavedRoom(@Header("Authorization") String authHeader);

    @GET("rooms/{id}")
    Call<RoomSearchItem> getRoomById(@Header("Authorization") String authHeader, @Path("id") String id );

    @PUT("users/me")
    Call<ResponseBody> updateInfoUser(@Header("Authorization") String authHeader,@Body UpdateUserObj userObj);

    @GET("users/me")
    Call<ResponseBody> getInfoMe(@Header("Authorization") String authHeader);

    @GET("chat-rooms")
    Call<ChatRoomObj> getLstChatRoom(@Header("Authorization") String authHeader);

    @GET("chat-rooms/{id}")
    Call<ChatObject> getListMess(@Header("Authorization")String authHeader, @Path("id")String id, @Query("limit")int limit, @Query("page") long page);

    @POST("chat-rooms")
    Call<ObjectChat> createRoom(@Header("Authorization")String authHeader, @Body RoomCreatedObj roomCreatedObj);

    @POST("comments")
    Call<CommentContent> uploadCommentNoParent(@Header("Authorization")String authHeader, @Body CommentContent commentObject);

    @POST("comments")
    Call<CommentContent> uploadCommentWithParent(@Header("Authorization")String authHeader, @Body CommentContent commentObject);

    @POST("feedbacks")
    Call<ResponseBody> uploadFeedBack(@Header("Authorization")String authHeader, @Body ContentObject content);

    @GET("comments")
    Call<CommentContent[]> getListComment(@Header("Authorization") String authHeader, @Query("room") String idRoom, @Query("sort") String sort);

    @GET("notifications")
    Call<NotiModel> getLstNoti(@Header("Authorization") String token, @Query("receiver") String idUser);

    @GET("notifications/{id}")
    Call<NotiObject> getDetailNoti(@Header("Authorization") String token, @Path("id")String id);
}
