package code.controllers;

import code.managers.DialogManager;
import code.managers.LocaleManager;
import code.phonebook.CollectionPhoneBook;
import code.phonebook.DBPhoneBook;
import code.phonebook.PhoneBook;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import code.model.Lang;
import code.model.Person;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Observable;
import java.util.ResourceBundle;

public class MainController extends Observable implements Initializable {
    private static final String FXML_EDIT = "../fxml/edit.fxml";
    private static final String FXML_DELETE = "../fxml/delete.fxml";
    private static final String BUNDLES_FOLDER = "code.bundles.Locale";
    private final PhoneBook phoneBook = new DBPhoneBook();
    private Stage mainStage;

    @FXML
    private TextField fieldSearch;
    @FXML
    private ComboBox<Lang> comboLocale;
    @FXML
    private VBox main;
    @FXML
    private TableView<Person> tableView;
    @FXML
    private TableColumn<Person, String> columnName;
    @FXML
    private TableColumn<Person, String> columnPhone;
    @FXML
    private Label labelInfo;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnAdd;

    private Parent fxmlEdit;
    private Parent fxmlDelete;
    private final FXMLLoader fxmlEditLoader = new FXMLLoader();
    private final FXMLLoader fxmlDeleteLoader = new FXMLLoader();
    private EditController editController;
    private DeleteController deleteController;
    private Stage editDialogStage;
    private Stage deleteDialogStage;
    private ResourceBundle resBundle;

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resBundle = resourceBundle;

        addClearButton();

        columnName.setCellValueFactory(new PropertyValueFactory<Person, String>("fio"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<Person, String>("phone"));

        initListeners();
        fillData();
        // сначала инициализируем Локаль (внутри метода есть null=ru)
        fillLangComboBox();
        // а потом уже инициализируем загрузчики
        initLoaders();
    }

    private void addClearButton() {
        InputStream inputStream = getClass().getResourceAsStream("../resources/red_cross.png");
        ImageView imageView = new ImageView(new Image(inputStream));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        btnClear.setGraphic(imageView);
        btnClear.setVisible(false);
    }

    private void initLoaders() {
        try {
            fxmlEditLoader.setLocation(getClass().getResource(FXML_EDIT));
            fxmlEditLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            fxmlEdit = fxmlEditLoader.load();
            editController = fxmlEditLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fxmlDeleteLoader.setLocation(getClass().getResource(FXML_DELETE));
            fxmlDeleteLoader.setResources(ResourceBundle.getBundle(BUNDLES_FOLDER, LocaleManager.getCurrentLang().getLocale()));
            fxmlDelete = fxmlDeleteLoader.load();
            deleteController = fxmlDeleteLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void fillData() {
        phoneBook.findAll();
        tableView.setItems(phoneBook.getPersonList());
    }

    private void initListeners() {
        phoneBook.getPersonList().addListener(
                (ListChangeListener<Person>) c -> updateLabelInfo(phoneBook.getPersonList().size())
        );

        tableView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                Person person = tableView.getSelectionModel().getSelectedItem();
                if (person == null) return;
                editController.setPerson(person);
                showDialog(false);
            }
        });

        comboLocale.setOnAction((EventHandler<ActionEvent>) event -> {
            Lang selectedLang = (Lang) comboLocale.getSelectionModel().getSelectedItem();
            LocaleManager.setCurrentLang(selectedLang);
            setChanged();
            notifyObservers(selectedLang);
        });

        fieldSearch.textProperty().addListener(event -> {
            btnClear.setVisible(!fieldSearch.getText().equals(""));
        });
    }

    private void fillLangComboBox() {
        Lang langRU = new Lang(0, "ru", resBundle.getString("lang.ru"), LocaleManager.RU_LOCALE);
        Lang langEN = new Lang(1, "en", resBundle.getString("lang.en"), LocaleManager.EN_LOCALE);

        comboLocale.getItems().add(langRU);
        comboLocale.getItems().add(langEN);

        if (LocaleManager.getCurrentLang() == null) {
            LocaleManager.setCurrentLang(langRU);
            comboLocale.getSelectionModel().select(0);
        } else {
            comboLocale.getSelectionModel().select(LocaleManager.getCurrentLang().getIndex());
        }
    }

    public void actionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            return;
        }

        Button clickedButton = (Button) source;
        Person selectedPerson = tableView.getSelectionModel().getSelectedItem();

        switch (clickedButton.getId()) {
            case "btnAdd":
                editController.setPhoneBook(phoneBook);
                editController.setPerson(new Person());
                showDialog(true);
                Person person = editController.getPerson();
                if (person != null) {
                    phoneBook.add(person);
                }
                break;

            case "btnEdit":
                if (selectedPerson == null) {
                    DialogManager.showInfoDialog(resBundle.getString("error"), resBundle.getString("select.person"));
                    return;
                }
                editController.setPhoneBook(phoneBook);
                editController.setPerson(selectedPerson);
                showDialog(false);
                if(editController.isChanged()){
                    tableView.refresh();
                }
                break;

            case "btnDelete":
                if (selectedPerson == null) {
                    DialogManager.showInfoDialog(resBundle.getString("error"), resBundle.getString("select.person"));
                    return;
                }
                deleteController.setPerson(selectedPerson);
                deleteController.setPhoneBook(phoneBook);
                showDeleteDialog();
                break;

            case "btnSearch":
                if (!fieldSearch.getText().isEmpty()) {
                    phoneBook.find(fieldSearch.getText());
                    tableView.setItems(phoneBook.getPersonList());
                } else {
                    tableView.setItems(phoneBook.findAll());
                }
                updateLabelInfo(tableView.getItems().size());
                break;

            case "btnClear":
                fieldSearch.setText("");
                tableView.setItems(phoneBook.findAll());
                updateLabelInfo(tableView.getItems().size());
                fieldSearch.requestFocus();
                break;
        }
    }

    private void updateLabelInfo(int count) {
        labelInfo.setText(resBundle.getString("records.in.table") + ": " + count);
    }

    private void showDialog(boolean isAdd) {
        if (editDialogStage == null) {
            editDialogStage = new Stage();
            editDialogStage.setMinHeight(200);
            editDialogStage.setMinWidth(400);
            editDialogStage.setResizable(false);
            editDialogStage.setScene(new Scene(fxmlEdit));
            //editDialogStage.initStyle(StageStyle.UNDECORATED);
            editDialogStage.initOwner(mainStage);
            editDialogStage.initModality(Modality.WINDOW_MODAL);
        }
        editDialogStage.setTitle(resBundle.getString(isAdd ? "title.add" : "title.edit"));
        editDialogStage.showAndWait();
    }

    private void showDeleteDialog() {
        if (deleteDialogStage == null) {
            deleteDialogStage = new Stage();
            deleteDialogStage.setTitle(resBundle.getString("delete"));
            deleteDialogStage.setMinWidth(400);
            deleteDialogStage.setMinHeight(200);
            deleteDialogStage.setResizable(false);
            deleteDialogStage.setScene(new Scene(fxmlDelete));
            deleteDialogStage.initOwner(mainStage);
            deleteDialogStage.initModality(Modality.WINDOW_MODAL);
        }

        deleteDialogStage.showAndWait();
    }
}
