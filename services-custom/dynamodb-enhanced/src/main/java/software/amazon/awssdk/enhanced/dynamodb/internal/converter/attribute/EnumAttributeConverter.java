/*
 * Copyright Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package software.amazon.awssdk.enhanced.dynamodb.internal.converter.attribute;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.enhanced.dynamodb.AttributeConverter;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.mapper.AttributeValueType;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

/**
 * A converter between an {@link Enum} and {@link AttributeValue}.
 *
 * <p>
 * This stores values in DynamoDB as a string.
 *
 * <p>
 * This can be created via {@link #create(Class<T>)}.
 */
@SdkInternalApi
public class EnumAttributeConverter<T extends Enum<T>> implements AttributeConverter<T> {

    private final Class<T> enumClass;
    private final Map<String, T> enumValueMap;

    private EnumAttributeConverter(Class<T> enumClass) {
        this.enumClass = enumClass;

        Map<String, T> mutableEnumValueMap = new LinkedHashMap<>();
        Arrays.stream(enumClass.getEnumConstants())
              .forEach(enumConstant -> mutableEnumValueMap.put(enumConstant.toString(), enumConstant));

        this.enumValueMap = Collections.unmodifiableMap(mutableEnumValueMap);
    }

    public static <T extends Enum<T>> EnumAttributeConverter<T> create(Class<T> enumClass) {
        return new EnumAttributeConverter<>(enumClass);
    }

    @Override
    public AttributeValue transformFrom(T input) {
        return EnhancedAttributeValue.fromString(input.toString()).toAttributeValue();
    }

    @Override
    public T transformTo(AttributeValue input) {
        EnhancedAttributeValue converted = EnhancedAttributeValue.fromString(input.s());
        T returnValue = enumValueMap.get(converted.asString());

        if (returnValue == null) {
            throw new IllegalArgumentException(String.format("Unable to convert string value '%s' to enum type '%s'",
                                                             converted.asString(), enumClass));
        }

        return returnValue;
    }

    @Override
    public TypeToken<T> type() {
        return TypeToken.of(enumClass);
    }

    @Override
    public AttributeValueType attributeValueType() {
        return AttributeValueType.S;
    }
}
