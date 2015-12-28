package app.wrocasion.JSONs;

public class LoginResponse {

    private String Id;
    private String Message;
    private boolean CorrectLogin;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isCorrectLogin() {
        return CorrectLogin;
    }

    public void setCorrectLogin(boolean correctLogin) {
        CorrectLogin = correctLogin;
    }
}
