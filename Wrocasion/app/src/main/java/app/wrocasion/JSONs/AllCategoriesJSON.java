package app.wrocasion.JSONs;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

public class AllCategoriesJSON {

    private String Nazwa;
    private int Id;
    private String LinkDoObrazka;


    public String getLinkDoObrazka() {
        return LinkDoObrazka;
    }

    public void setLinkDoObrazka(String linkDoObrazka) {
        this.LinkDoObrazka = linkDoObrazka;
    }

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String nazwa) {
        this.Nazwa = nazwa;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        this.Id = id;
    }


    public static interface WebServiceAllCategories {

        @GET("/api/CategoryApi/GetAllCategories") // deklarujemy endpoint oraz metodę
        void getData(Callback<List<AllCategoriesJSON>> pResponse);

        @POST("/api/CategoryApi/GetAllCategories") // deklarujemy endpoint, metodę oraz dane do wysłania
        void postData(@Body AllCategoriesJSON pBody, Callback<AllCategoriesJSON> pResponse);

    }
}
