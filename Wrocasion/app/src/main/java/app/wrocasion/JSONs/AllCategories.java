package app.wrocasion.JSONs;

public class AllCategories {

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

}
