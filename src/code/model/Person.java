package code.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Person {
    private final SimpleIntegerProperty id = new SimpleIntegerProperty();
    private final SimpleStringProperty fio = new SimpleStringProperty("");
    private final SimpleStringProperty phone = new SimpleStringProperty("");

    public Person() {
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFio() {
        return fio.get();
    }

    public void setFio(String fio) {
        this.fio.set(fio);
    }

    public String getPhone() {
        return phone.get();
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    @Override
    public String toString() {
        return String.format("%d %s %s",getId(), getFio(), getPhone());
    }
}
