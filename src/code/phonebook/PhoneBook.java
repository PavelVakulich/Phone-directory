package code.phonebook;

import javafx.collections.ObservableList;
import code.model.Person;

public interface PhoneBook {
    void add(Person person);

    void update(Person person);

    void delete(Person person);

    ObservableList<Person> getPersonList();

    ObservableList<Person> findAll();

    ObservableList<Person> find(String text);
}
