package ba.edu.ibu.javafx;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public class ListController implements Initializable {
    private DatabaseConnection databaseConnection;
    @FXML
    private TableView<Student> studentTableView;
    @FXML
    public TextField txtId;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtSurname;
    @FXML
    public TextField txtYear;
    @FXML
    public TextField txtCycle;
    @FXML
    public TableView tblStudents;
    @FXML
    public TableColumn<Student, Integer> colId;
    @FXML
    public TableColumn<Student, String> colName;
    @FXML
    public TableColumn<Student, String> colSurname;
    @FXML
    public TableColumn<Student, String> colCycle;
    @FXML
    public TableColumn<Student, String> colYear;
    @FXML
    public Button btnSave;
    @FXML
    private TextField filterField;

    ObservableList<Student> listM;
    ObservableList<Student> dataList;

    int selectedIndex = -1;

    StudentService studentService = new StudentService();

    public void saveStudent() {
        try {
            int studentId = Integer.valueOf(txtId.getText());
            String name = txtName.getText();
            String surname = txtSurname.getText();
            String year = txtYear.getText();
            String cycle = txtCycle.getText();

            Student student = new Student(studentId, name, surname, year, cycle);

            if (studentService.isDuplicateId(studentId)) {
                // Update existing student
                studentService.saveStudent(student);
            } else {
                // Add new student
                studentService.saveStudent(student);
            }

            UpdateTable();
            searchStudent();
        } catch (NumberFormatException e) {
            showError("Invalid attribute", "You have to populate all fields");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void getStudent(MouseEvent event) {
        selectedIndex = tblStudents.getSelectionModel().getSelectedIndex();
        if (selectedIndex <= -1) return;

        txtId.setText(String.valueOf(colId.getCellData(selectedIndex)));
        txtName.setText(colName.getCellData(selectedIndex));
        txtSurname.setText(colSurname.getCellData(selectedIndex));
        txtYear.setText(colYear.getCellData(selectedIndex));
        txtCycle.setText(colCycle.getCellData(selectedIndex));
    }

    public void UpdateTable() throws SQLException {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycle"));

        listM = databaseConnection.getStudents();
        tblStudents.setItems(listM);
    }

    @FXML
    void searchStudent() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colCycle.setCellValueFactory(new PropertyValueFactory<>("cycle"));

        dataList = studentService.getStudents();
        tblStudents.setItems(dataList);

        FilteredList<Student> filteredData = new FilteredList<>(dataList, b -> true);

        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return String.valueOf(student.getId()).toLowerCase().contains(lowerCaseFilter)
                        || student.getName().toLowerCase().contains(lowerCaseFilter)
                        || student.getSurname().toLowerCase().contains(lowerCaseFilter)
                        || student.getYear().toLowerCase().contains(lowerCaseFilter)
                        || student.getCycle().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<Student> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tblStudents.comparatorProperty());
        tblStudents.setItems(sortedData);
    }



    static void showError(String title, String message) {
        Locale.setDefault(Locale.ENGLISH);
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle(title);
        error.setHeaderText(message);
        error.show();
    }





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTable();
    }

    private void initializeTable() {
    }


}
