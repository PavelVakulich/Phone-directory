package code.model;

import java.util.Locale;

public class Lang {
    private String code;
    private final String name;
    private final Locale locale;
    private final int index;


    public Lang(int index, String code, String name, Locale locale) {
        this.code = code;
        this.name = name;
        this.locale = locale;
        this.index = index;
    }

    public Locale getLocale() {
        return locale;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return name;
    }
}
