package app.wrocasion.JSONs;

import java.util.ArrayList;

public class ResponseUserCategories {

    private ArrayList<String> Categories;
    private String User;


    public ArrayList<String> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<String> categories) {
        Categories = categories;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
