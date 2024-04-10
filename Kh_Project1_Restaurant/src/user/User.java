package user;

public class User {
    private String userId;
    private String password;

    public User(String userId, String password) {
        this.userId = new String(userId);
        this.password = new String(password);
    }

    public String getUserId() {
        return userId.toString();
    }

    public String getPassword() {
        return password.toString();
    }

    public void resetCredentials() {
    	this.userId = " ";
    	this.password = " ";
    }
}


