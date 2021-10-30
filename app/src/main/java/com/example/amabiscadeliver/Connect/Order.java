package com.example.amabiscadeliver.Connect;

public class Order {
    private Integer _codigo = 0;
    private String _fecha= "";
    private String _cliente = "";
    private String _telefono = "";
    private String _estado = "";

    public Order(Integer _codigo, String _fecha, String _cliente, String _telefono, String _estado){
        this._codigo = _codigo;
        this._fecha = _fecha;
        this._cliente = _cliente;
        this._telefono = _telefono;
        this._estado = _estado;
    }

    public String get_estado() {
        return _estado;
    }

    public void set_estado(String _estado) {
        this._estado = _estado;
    }

    public Integer get_codigo() {
        return _codigo;
    }

    public void set_codigo(Integer _codigo) {
        this._codigo = _codigo;
    }

    public String get_fecha() {
        return _fecha;
    }

    public void set_fecha(String _fecha) {
        this._fecha = _fecha;
    }

    public String get_cliente() {
        return _cliente;
    }

    public void set_cliente(String _cliente) {
        this._cliente = _cliente;
    }

    public String get_telefono() {
        return _telefono;
    }

    public void set_telefono(String _telefono) {
        this._telefono = _telefono;
    }
}
