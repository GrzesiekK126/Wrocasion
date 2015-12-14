package app.wrocasion.JSONs;

import java.util.Date;

public class GetEvents {

    private String Nazwa;
    private String Street;
    private String City;
    private String ZipCode;
    private String Image;
    private String Link;
    private String Categories;
    private int Id;
    private int Operator;
    private int LocationId;
    private int TakingPart;
    private Date Data;
    private Date AddData;
    private double Price;
    private double Longtitude;
    private double Latitude;

    public String getNazwa() {
        return Nazwa;
    }

    public void setNazwa(String nazwa) {
        Nazwa = nazwa;
    }

    public String getStreet() {
        return Street;
    }

    public void setStreet(String street) {
        Street = street;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getZipCode() {
        return ZipCode;
    }

    public void setZipCode(String zipCode) {
        ZipCode = zipCode;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getCategories() {
        return Categories;
    }

    public void setCategories(String categories) {
        Categories = categories;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getOperator() {
        return Operator;
    }

    public void setOperator(int operator) {
        Operator = operator;
    }

    public int getLocationId() {
        return LocationId;
    }

    public void setLocationId(int locationId) {
        LocationId = locationId;
    }

    public int getTakingPart() {
        return TakingPart;
    }

    public void setTakingPart(int takingPart) {
        TakingPart = takingPart;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public Date getAddData() {
        return AddData;
    }

    public void setAddData(Date addData) {
        AddData = addData;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

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
}
