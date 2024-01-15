package org.example;

public class Car {
    private String marca;
    private String model;
    private String an;
    private String combustibil;
    private String culoare;
    public Car(){};

    public String getAn() {
        return an;
    }

    public String getMarca() {
        return marca;
    }

    public String getCombustibil() {
        return combustibil;
    }

    public String getModel() {
        return model;
    }

    public void setAn(String an) {this.an = an;}

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setCombustibil(String combustibil) {
        this.combustibil = combustibil;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCuloare() {
        return culoare;
    }

    public void setCuloare(String culoare) {
        this.culoare = culoare;
    }
}
