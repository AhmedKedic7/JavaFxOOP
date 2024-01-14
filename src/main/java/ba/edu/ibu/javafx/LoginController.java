package ba.edu.ibu.javafx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static ba.edu.ibu.javafx.ListController.showError;

public class LoginController implements Initializable {
    @FXML
    public Button btnLogin;
    private Parent root;
    private Stage stage;
    private Scene scene;
    private UserService userService;


    void switchToStudentsScreen(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("students.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    void login(ActionEvent event) throws IOException {

        if (authenticateUser()) {
            switchToStudentsScreen(event);
        } else {
            showError("Login Failed", "Invalid credentials. Please try again.");
        }
    }

    private boolean authenticateUser() {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();

        // Use UserService to authenticate the user
        User authenticatedUser = UserService.authenticateUser(enteredUsername, enteredPassword);

        // Return true if authentication is successful, false otherwise
        return authenticatedUser != null;
    }
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
