package user;

//싱글톤 쓰다가 아이디 비번 리셋안되서 망함 스트링 버퍼로 수정
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


