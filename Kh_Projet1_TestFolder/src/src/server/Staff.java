package server;

public class Staff extends User {
    public Staff(String id, String password) {
        super(id, password);
    }

    @Override
    public boolean LogIn(String id, String password) {
        return this.id.equals(id) && this.password.equals(password);
    }
}
