package com.apitable.client.api.model.field;

/**
 * the enumerations are field types' values to create field.
 */
public enum FieldTypeEnum {
    /**
     * the request body field type's value to create apitable SingleText field.
     */
    SingleText("SingleText"),
    /**
     * the request body field type's value to create apitable Text field.
     */
    Text("Text"),
    /**
     * the request body field type's value to create apitable SingleSelect field.
     */
    SingleSelect("SingleSelect"),
    /**
     * the request body field type's value to create apitable MultiSelect field.
     */
    MultiSelect("MultiSelect"),
    /**
     * the request body field type's value to create apitable Number field.
     */
    Number("Number"),
    /**
     * the request body field type's value to create apitable Currency field.
     */
    Currency("Currency"),
    /**
     * the request body field type's value to create apitable Percent field.
     */
    Percent("Percent"),
    /**
     * the request body field type's value to create apitable DateTime field.
     */
    DateTime("DateTime"),
    /**
     * the request body field type's value to create apitable Attachment field.
     */
    Attachment("Attachment"),
    /**
     * the request body field type's value to create apitable Member field.
     */
    Member("Member"),
    /**
     * the request body field type's value to create apitable Checkbox field.
     */
    Checkbox("Checkbox"),
    /**
     * the request body field type's value to create apitable Rating field.
     */
    Rating("Rating"),
    /**
     * the request body field type's value to create apitable URL field.
     */
    URL("URL"),
    /**
     * the request body field type's value to create apitable Phone field.
     */
    Phone("Phone"),
    /**
     * the request body field type's value to create apitable Email field.
     */
    Email("Email"),
    /**
     * the request body field type's value to create apitable MagicLink field.
     */
    MagicLink("MagicLink"),
    /**
     * the request body field type's value to create apitable MagicLookUp field.
     */
    MagicLookUp("MagicLookUp"),
    /**
     * the request body field type's value to create apitable Formula field.
     */
    Formula("Formula"),
    /**
     * the request body field type's value to create apitable AutoNumber field.
     */
    AutoNumber("AutoNumber"),
    /**
     * the request body field type's value to create apitable CreatedTime field.
     */
    CreatedTime("CreatedTime"),
    /**
     * the request body field type's value of create apitable LastModifiedTime field.
     */
    LastModifiedTime("LastModifiedTime"),
    /**
     * the request body field type's value to create apitable CreatedBy field.
     */
    CreatedBy("CreatedBy"),
    /**
     * the request body field type's value to create apitable LastModifiedBy field.
     */
    LastModifiedBy("LastModifiedBy");

    private final String fieldType;

    FieldTypeEnum(String fieldType) {
        this.fieldType = fieldType;
    }

    public String getFieldType() {
        return fieldType;
    }
}
