package code.controllers;

import code.model.Person;
import code.phonebook.PhoneBook;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteController implements Initializable {
    @FXML
    private Label labelDelete;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnCancel;

    private Person person;
    private PhoneBook phoneBook;

    private ResourceBundle resBundle;

    public void actionDelete(ActionEvent actionEvent) {
        phoneBook.delete(person);
        actionClose(actionEvent);
    }

    public void actionClose(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage stage = (Stage) node.getScene().getWindow();
        stage.close();
    }

    public void setPerson(Person person) {
        this.person = person;

        String text = String.format(resBundle.getString("do.you.want.delete") + " %s ?", person.getFio());
        labelDelete.setText(text);
    }

    public void setPhoneBook(PhoneBook phoneBook) {
        this.phoneBook = phoneBook;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resBundle = resourceBundle;
    }
}
