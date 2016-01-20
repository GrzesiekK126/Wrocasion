package app.wrocasion.JSONs;

public class Feedback {

    private int Id;
    private int Rate;
    private String Feedback;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }
}
