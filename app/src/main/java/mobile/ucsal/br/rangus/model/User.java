package mobile.ucsal.br.rangus.model;

public class User {

    public String telefone;
    public String email;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String telefone, String email) {
        this.telefone = telefone;
        this.email = email;
    }

}
