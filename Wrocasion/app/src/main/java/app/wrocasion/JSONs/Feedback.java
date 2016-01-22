package app.wrocasion.JSONs;

public class Feedback {

    private int EventId;
    private int Rate;
    private String Description;
    private String UserName;

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventid) {
        EventId = eventid;
    }

    public int getRate() {
        return Rate;
    }

    public void setRate(int rate) {
        Rate = rate;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
