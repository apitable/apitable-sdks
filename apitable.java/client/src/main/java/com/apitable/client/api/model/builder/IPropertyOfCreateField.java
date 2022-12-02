package com.apitable.client.api.model.builder;

import com.apitable.client.api.model.field.property.BaseFieldProperty;
import com.apitable.client.api.model.field.property.EmptyProperty;

public interface IPropertyOfCreateField {

    <T extends BaseFieldProperty> IBuildCreateField<T> withProperty(T property);

    IBuildCreateField<EmptyProperty> withoutProperty();
}
