package com.apitable.client.api.model.field.property.option;

public class Format<T extends TypeFormat> {

    private FormatTypeEnum type;

    private T format;

    public FormatTypeEnum getType() {
        return type;
    }

    public void setType(FormatTypeEnum type) {
        this.type = type;
    }

    public T getFormat() {
        return format;
    }

    public void setFormat(T format) {
        this.format = format;
    }
}
