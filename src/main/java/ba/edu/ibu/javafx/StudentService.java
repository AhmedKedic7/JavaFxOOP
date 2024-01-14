package ba.edu.ibu.javafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentService {
    private ObservableList<Student> students;



    public void setStudents(ObservableList<Student> students) {
        this.students = students;
    }

    // Check for duplicate student ID
    public boolean isDuplicateId(int studentId) {
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students WHERE id = ?")) {

            statement.setInt(1, studentId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Save a new student or update an existing one
    public void saveStudent(Student student) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (isDuplicateId(student.getId())) {
                // Update existing student
                try (PreparedStatement updateStatement = connection.prepareStatement(
                        "UPDATE students SET name=?, surname=?, year=?, cycle=? WHERE id=?")) {

                    updateStatement.setString(1, student.getName());
                    updateStatement.setString(2, student.getSurname());
                    updateStatement.setString(3, student.getYear());
                    updateStatement.setString(4, student.getCycle());
                    updateStatement.setInt(5, student.getId());

                    updateStatement.executeUpdate();
                }
            } else {
                // Add new student
                try (PreparedStatement insertStatement = connection.prepareStatement(
                        "INSERT INTO students (id, name, surname, year, cycle) VALUES (?, ?, ?, ?, ?)")) {

                    insertStatement.setInt(1, student.getId());
                    insertStatement.setString(2, student.getName());
                    insertStatement.setString(3, student.getSurname());
                    insertStatement.setString(4, student.getYear());
                    insertStatement.setString(5, student.getCycle());

                    insertStatement.executeUpdate();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public ObservableList<Student> getStudents() {
        ObservableList<Student> students = FXCollections.observableArrayList();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM students")) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String surname = resultSet.getString("surname");
                int year = resultSet.getInt("year");
                int cycle = resultSet.getInt("cycle");

                students.add(new Student(id, name, surname, String.valueOf(year), String.valueOf(cycle)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }



}
