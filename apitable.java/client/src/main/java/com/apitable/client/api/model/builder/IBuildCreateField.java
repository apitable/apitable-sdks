package com.apitable.client.api.model.builder;

import com.apitable.client.api.model.CreateFieldRequest;
import com.apitable.client.api.model.field.property.BaseFieldProperty;

public interface IBuildCreateField <T extends BaseFieldProperty>  {

    CreateFieldRequest<T> build();

}
