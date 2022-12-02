package com.apitable.client.api.model.field.property.option;

public class CurrencyFormat extends TypeFormat{

    private int precision;

    private String symbol;

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
