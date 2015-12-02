package app.wrocasion.JSONs;


import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public class RemoveUserJSON {

    private String Name;
    //private String Id;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    /*public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }*/


    public static interface WebServiceRemoveUser {

        @GET("/api/UserApi/RemoveUser") // deklarujemy endpoint oraz metodę
        void getData(Callback<List<RemoveUserJSON>> pResponse);

        @POST("/api/UserApi/RemoveUser") // deklarujemy endpoint, metodę oraz dane do wysłania
        void postData(@Body RemoveUserJSON pBody, Callback<RemoveUserJSON> pResponse);
    }

}
