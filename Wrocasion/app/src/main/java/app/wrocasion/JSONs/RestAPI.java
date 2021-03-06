package app.wrocasion.JSONs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public interface RestAPI {

    @POST("/api/UserApi/AddUser")
    void addUser(@Body AddUser pBody, Callback<LoginResponse> pResponse);

    @POST("/api/UserApi/RemoveUser")
    void removeUser(@Body RemoveUser pBody, Callback<RemoveUser> pResponse);

    @POST("/api/UserApi/LoginUser")
    void loginUser(@Body LoginUser pBody, Callback<LoginResponse> pResponse);

    @GET("/api/CategoryApi/GetAllCategories")
    void getAllCategories(Callback<List<AllCategories>> pResponse);

    @POST("/api/UserApi/UserCategories")
    void getUserCategories(@Body UserCategories pBody, Callback<ResponseUserCategories> pResponse);

    @POST("/api/CategoryApi/AddOrChangeUserCategories")
    void addOrChangeUserCategories(@Body AddOrChangeUserCategories pBody, Callback<ChangeCategoriesResponse> pResponse);

    @POST("/api/EventApi/EventToAndroid")
    void getEvents(@Body SetCurrentLocation pBody, Callback<List<GetEvents>> pResponse);

    @POST("/api/FeedbackApi/RateEvent")
    void getFeedback(@Body Feedback pBody, Callback<ChangeCategoriesResponse> pResponse);

    @POST("/api/EventApi/EventToAndroid")
    void getEventsCategories(@Body SetCurrentLocationWithCategories pBody, Callback<List<GetEvents>> pResponse);

    @POST("/api/EventApi/UserTakingPart")
    void joinToEvent(@Body JoinToEvent pBody, Callback<ChangeCategoriesResponse> pResponse);
}
