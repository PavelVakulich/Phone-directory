package code.phonebook;


import code.connection.SQLiteConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import code.model.Person;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBPhoneBook implements PhoneBook {
    private ObservableList<Person> personList = FXCollections.observableArrayList();

    public DBPhoneBook() {
        personList = findAll();
    }

    @Override
    public void add(Person person) {
        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "INSERT INTO person(fio, phone) VALUES (?, ?)",
                     Statement.RETURN_GENERATED_KEYS
             )) {
            statement.setString(1, person.getFio());
            statement.setString(2, person.getPhone());

            int result = statement.executeUpdate();
            if (result > 0) {
                // получить сгенерированный id вставленной записи
                int id = statement.getGeneratedKeys().getInt(1);
                person.setId(id);
                personList.add(person);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPhoneBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void delete(Person person) {
        try (Connection con = SQLiteConnection.getConnection();
             Statement statement = con.createStatement()
        ) {
            int result = statement.executeUpdate(
                    "DELETE FROM person WHERE id=" + person.getId()
            );

            if (result > 0) {
                personList.remove(person);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPhoneBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ObservableList<Person> findAll() {
        personList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             Statement statement = con.createStatement();
             ResultSet rs = statement.executeQuery(
                     "SELECT * FROM person"
             )) {

            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setFio(rs.getString("fio"));
                person.setPhone(rs.getString("phone"));
                personList.add(person);
            }

        } catch (SQLException ex) {
            Logger.getLogger(DBPhoneBook.class.getName()).log(Level.SEVERE, null, ex);
        }

        return personList;
    }

    @Override
    public void update(Person person) {
        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "UPDATE person SET fio=?, phone=? WHERE id=?"
             )) {

            statement.setString(1, person.getFio());
            statement.setString(2, person.getPhone());
            statement.setInt(3, person.getId());

            statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DBPhoneBook.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ObservableList<Person> find(String text) {
        personList.clear();

        try (Connection con = SQLiteConnection.getConnection();
             PreparedStatement statement = con.prepareStatement(
                     "SELECT * FROM person WHERE fio LIKE ? OR phone LIKE ?"
             )) {

            String searchStr = "%" + text + "%";

            statement.setString(1, searchStr);
            statement.setString(2, searchStr);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Person person = new Person();
                person.setId(rs.getInt("id"));
                person.setFio(rs.getString("fio"));
                person.setPhone(rs.getString("phone"));
                personList.add(person);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DBPhoneBook.class.getName()).log(Level.SEVERE, null, ex);
        }

        return personList;
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return personList;
    }
}
