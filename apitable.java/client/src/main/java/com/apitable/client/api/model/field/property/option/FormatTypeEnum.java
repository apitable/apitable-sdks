package com.apitable.client.api.model.field.property.option;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FormatTypeEnum {
    /**
     * Format about DateTime
     * @see DateTimeFormat
     */
    DateTime("DateTime"),
    /**
     * Format about Number
     * @see NumberFormat
     */
    Number("Number"),
    /**
     * Format about Percent
     * @see PercentFormat
     */
    Percent("Percent"),
    /**
     * Format about Currency
     * @see CurrencyFormat
     */
    Currency("Currency");

    private final String value;

    FormatTypeEnum(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
