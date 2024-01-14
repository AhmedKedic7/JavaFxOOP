package ba.edu.ibu.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;


public class DatabaseConnection {
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/kedi";
        String username = "root";
        String password = "rootroot";
        return DriverManager.getConnection(url, username, password);
    }

    public static ObservableList<Student> getStudents() throws SQLException {
        Connection conn = getConnection();
        ObservableList<Student> list = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM students");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("year"),
                        rs.getString("cycle")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.close();
            }
        }

        return list;
    }}