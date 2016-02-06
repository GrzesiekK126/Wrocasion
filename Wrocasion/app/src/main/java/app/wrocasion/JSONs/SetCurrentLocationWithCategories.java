package app.wrocasion.JSONs;

import java.util.ArrayList;

public class SetCurrentLocationWithCategories {

    private double Longtitude,  Latitude;
    private String Username;
    private ArrayList<String> CategoriesList;

    public double getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(double longtitude) {
        Longtitude = longtitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public ArrayList<String> getCategoriesList() {
        return CategoriesList;
    }

    public void setCategoriesList(ArrayList<String> categoriesList) {
        CategoriesList = categoriesList;
    }
}
