package ba.edu.ibu.javafx;

public class UserService {

        public static User authenticateUser(String enteredUsername, String enteredPassword) {
            // Simulate user authentication (replace with your actual logic)
            // For simplicity, using hardcoded credentials
            if ("test".equals(enteredUsername) && "password".equals(enteredPassword)) {
                return new User("test", "password");
            } else {
                return null;
            }
        }

}
