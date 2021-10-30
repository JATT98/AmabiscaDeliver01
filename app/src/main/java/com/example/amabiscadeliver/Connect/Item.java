package com.example.amabiscadeliver.Connect;

public class Item {
    private String _nombre = "";
    private Integer _cantidad= 0;
    private Double _precio = 0.0;

    public Item(String _nombre, Integer _cantidad, Double _precio){
        this._nombre = _nombre;
        this._cantidad = _cantidad;
        this._precio = _precio;
    }

    public String get_nombre() {
        return _nombre;
    }

    public Integer get_cantidad() {
        return _cantidad;
    }

    public Double get_precio() {
        return _precio;
    }

}
