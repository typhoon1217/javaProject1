package login;

//관리자 클래스
public class Admin extends User {
	public Admin(String id, String password) {
		super(id, password);
	}

	@Override
	public boolean LogIn(String id, String password) {
		return this.id.equals(id) && this.password.equals(password);
	}
}