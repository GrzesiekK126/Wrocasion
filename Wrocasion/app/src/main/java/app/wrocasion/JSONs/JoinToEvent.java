package app.wrocasion.JSONs;

public class JoinToEvent {

    private String Username;
    private boolean TakingPart;
    private int EventIdToTakingPart;

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public boolean isTakingPart() {
        return TakingPart;
    }

    public void setTakingPart(boolean takingPart) {
        TakingPart = takingPart;
    }

    public int getEventIdToTakingPart() {
        return EventIdToTakingPart;
    }

    public void setEventIdToTakingPart(int eventIdToTakingPart) {
        EventIdToTakingPart = eventIdToTakingPart;
    }
}
