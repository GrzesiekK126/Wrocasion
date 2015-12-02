package app.wrocasion.JSONs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public class UserCategoriesJSON {

    private String Name;
    private String Id;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }


    public static interface WebServiceUserCategories {

        @GET("/api/UserApi/UserCategories") // deklarujemy endpoint oraz metodę
        void getData(Callback<List<UserCategoriesJSON>> pResponse);

        @POST("/api/UserApi/UserCategories") // deklarujemy endpoint, metodę oraz dane do wysłania
        void postData(@Body UserCategoriesJSON pBody, Callback<UserCategoriesJSON> pResponse);
    }


}
