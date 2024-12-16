package code.controllers;

import code.managers.DialogManager;
import code.model.Person;
import code.phonebook.PhoneBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditController implements Initializable {
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldPhone;

    private Person person;
    private ResourceBundle resBundle;
    private PhoneBook phoneBook;
    private boolean isChanged;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resBundle = resourceBundle;
    }

    public Person getPerson() {
        return person;
    }

    public void actionClose(ActionEvent actionEvent) {
        this.person = null;
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void setPerson(Person person) {
        this.person = person;

        fieldName.setText(person.getFio());
        fieldPhone.setText(person.getPhone());
    }

    public void setPhoneBook(PhoneBook phoneBook){
        this.phoneBook = phoneBook;
    }

    public void actionOk(ActionEvent actionEvent) {
        String name = fieldName.getText();
        String phone = fieldPhone.getText();

        if (name.isEmpty() || phone.isEmpty()) {
            DialogManager.showInfoDialog(resBundle.getString("error"), resBundle.getString("input.data"));
            return;
        }

        person.setFio(name);
        person.setPhone(phone);

        phoneBook.update(person);
        isChanged = true;

        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public boolean isChanged(){
        boolean result = isChanged;
        isChanged = false;
        return result;
    }
}
