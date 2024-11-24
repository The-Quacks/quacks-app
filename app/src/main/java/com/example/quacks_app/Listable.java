package com.example.quacks_app;

public class Listable {
    String field;
    String subfield;

    public Listable(){

    }
    public Listable(String field, String subfield){
        this.field = field;
        this.subfield = subfield;
    }

    public void setField(String field) {
        this.field = field;
    }
    public void setSubfield(String subfield) {
        this.subfield = subfield;
    }

    public String getDisplay(){
        return field;
    }
    public String getSubDisplay(){
        return subfield;
    }


}
