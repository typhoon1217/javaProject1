package localLogin;

import main.Main;

public class Admin implements LogInInterface {
	static final String BAR = Main.BAR;
	private static final String ADMIN_ID = "admin";
	private static final String ADMIN_PASSWORD = "admin1234";

	@Override
	public boolean logIn(User user) {
		return user.getUserId().equals(ADMIN_ID) && user.getPassword().equals(ADMIN_PASSWORD);
	}
}