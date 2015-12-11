package app.wrocasion.JSONs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestAPI {

    @POST("/api/UserApi/AddUser") // deklarujemy endpoint, metodę oraz dane do wysłania
    void addUser(@Body AddUser pBody, Callback<AddUser> pResponse);

    @POST("/api/UserApi/RemoveUser") // deklarujemy endpoint, metodę oraz dane do wysłania
    void removeUser(@Body RemoveUser pBody, Callback<RemoveUser> pResponse);

    @GET("/api/CategoryApi/GetAllCategories") // deklarujemy endpoint oraz metodę
    void getAllCategories(Callback<List<AllCategories>> pResponse);

    @POST("/api/UserApi/UserCategories") // deklarujemy endpoint, metodę oraz dane do wysłania
    void getUserCategories(@Body UserCategories pBody, Callback<List<ResponseUserCategories>> pResponse);

    @POST("/api/CategoryApi/AddOrChangeUserCategories") // deklarujemy endpoint, metodę oraz dane do wysłania
    void addOrChangeUserCategories(@Body AddOrChangeUserCategories pBody, Callback<AddOrChangeUserCategories> pResponse);

    @POST("/api/CategoryApi/AddOrChangeUserCategories") // deklarujemy endpoint, metodę oraz dane do wysłania
    void getEvents(@Body SetCurrentLocation pBody, Callback<GetEvents> pResponse);

}
