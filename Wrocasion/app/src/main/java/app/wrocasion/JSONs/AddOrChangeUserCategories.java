package app.wrocasion.JSONs;

import java.util.ArrayList;
import java.util.List;

public class AddOrChangeUserCategories {

    private String user;
    private ArrayList<String> Categories;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<String> getCategories() {
        return Categories;
    }

    public void setCategories(ArrayList<String> categories) {
        Categories = categories;
    }

}
