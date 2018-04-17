package com.example.user.myapplication;

import java.io.Serializable;
import java.util.List;


public class DataWrapper implements Serializable {

    private List<Double> lista;

    public DataWrapper(List<Double> data) {
        this.lista = data;
    }

    public List<Double> getParliaments() {
        return this.lista;
    }

}