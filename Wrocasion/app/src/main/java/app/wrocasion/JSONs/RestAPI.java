package app.wrocasion.JSONs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestAPI {

    @POST("/api/UserApi/AddUser")
    void addUser(@Body AddUser pBody, Callback<AddUser> pResponse);

    @POST("/api/UserApi/RemoveUser")
    void removeUser(@Body RemoveUser pBody, Callback<RemoveUser> pResponse);

    @GET("/api/CategoryApi/GetAllCategories")
    void getAllCategories(Callback<List<AllCategories>> pResponse);

    @POST("/api/UserApi/UserCategories")
    void getUserCategories(@Body UserCategories pBody, Callback<ResponseUserCategories> pResponse);

    @POST("/api/CategoryApi/AddOrChangeUserCategories")
    void addOrChangeUserCategories(@Body AddOrChangeUserCategories pBody, Callback<ChangeCategoriesResponse> pResponse);

    @POST("/api/EventApi/EventToAndroid")
    void getEvents(@Body SetCurrentLocation pBody, Callback<List<GetEvents>> pResponse);

}
