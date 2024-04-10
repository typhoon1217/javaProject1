package server;

//사용자 정보를 저장하는 추상 클래스
public abstract class User {
 protected String id;
 protected String password;

 public User(String id, String password) {
     this.id = id;
     this.password = password;
 }

 public abstract boolean LogIn(String id, String password);
}
