package code.phonebook;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import code.model.Person;

public class CollectionPhoneBook implements PhoneBook {
    private final ObservableList<Person> personList = FXCollections.observableArrayList();
    private final ObservableList<Person> searchPersonList = FXCollections.observableArrayList();

    @Override
    public void add(Person person) {
        personList.add(person);
    }

    @Override
    public void update(Person person) {
    }

    @Override
    public void delete(Person person) {
        personList.remove(person);
    }

    @Override
    public ObservableList<Person> findAll() {
        return personList;
    }

    @Override
    public ObservableList<Person> find(String text) {
        return null;
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return personList;
    }

    public void searchPerson(String searchStr) {
        this.searchPersonList.clear();
    }

    public ObservableList<Person> getSearchPersonList() {
        return searchPersonList;
    }
}
