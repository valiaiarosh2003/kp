package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class Registration {
    @FXML private TextField login;
    @FXML private PasswordField passwordField;
    @FXML private TextField name;
    @FXML private TextField lastname;
    @FXML private Button btnregistration;
    @FXML
    private void initialize() {
        FXMLLoader loader = new FXMLLoader();
        btnregistration.setOnAction(event -> {
            boolean inBase = false;
            if (!(login.getText().isEmpty() || passwordField.getText().isEmpty()
                    || name.getText().isEmpty() || lastname.getText().isEmpty())){
                try{
                    String username = login.getText();
                    Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/yarosh_schema",
                            "root", "root")){
                        PreparedStatement statement = conn.prepareStatement
                                ("INSERT users(login, password, name, surname) VALUES(?, ?, ?, ?)");
                        ResultSet resultSet = statement.executeQuery("SELECT * FROM users");
                        while(resultSet.next()){
                            if (resultSet.getString("login").equals(username)){
                                inBase = true;
                                break;
                            }
                        }
                        if (!inBase){
                            statement.setString(1, login.getText());
                            statement.setString(2, passwordField.getText());
                            statement.setString(3, name.getText());
                            statement.setString(4, lastname.getText());
                            statement.executeUpdate();
                            btnregistration.getScene().getWindow().hide();
                            loader.setLocation(getClass().getResource("firstWindow.fxml"));

                            Parent root = null;
                            try {
                                root = loader.load();
                            } catch (IOException e){
                                e.printStackTrace();
                            }
                            Stage stage = new Stage();
                            assert root != null;
                            stage.setScene(new Scene(root));
                            stage.show();
                        }
                    }
                }
                catch(Exception ex){
                    System.out.println("Ошибка доступа к БД");
                }
            }
        });
    }
}

