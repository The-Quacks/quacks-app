package com.example.quacks_app;

/**
 * A class that makes it a lot easier to select and notify users
 */
public class Cartable {
    String field;
    String subfield;
    boolean selected;

    public Cartable() {

    }

    public Cartable(String field, String subfield, Boolean selected, UserProfile profile) {
        this.field = field;
        this.subfield = subfield;
        this.selected = selected;
    }

    public void setField(String field) {
        this.field = field;
    }
    public String getField(){
        return this.field;
    }

    public void setSubfield(String subfield) {
        this.subfield = subfield;
    }

    public String getDisplay() {
        return field;
    }

    public String getSubDisplay() {
        return subfield;
    }

    public boolean Carted() {
        return this.selected;
    }

    public void setCart(boolean selected) {
        this.selected = selected;
    }

    public boolean getCarted(String field) {
        if (field.equals(this.field)) {
            return this.selected;
        }
        return true;
    }
}


