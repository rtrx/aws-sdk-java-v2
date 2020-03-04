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

package software.amazon.awssdk.enhanced.dynamodb.internal.converter.string;

import java.time.Period;

import software.amazon.awssdk.annotations.Immutable;
import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.annotations.ThreadSafe;
import software.amazon.awssdk.enhanced.dynamodb.TypeToken;
import software.amazon.awssdk.enhanced.dynamodb.internal.converter.StringConverter;

/**
 * A converter between {@link Period} and {@link String}.
 */
@SdkInternalApi
@ThreadSafe
@Immutable
public class PeriodStringConverter implements StringConverter<Period> {
    private PeriodStringConverter() { }

    public static PeriodStringConverter create() {
        return new PeriodStringConverter();
    }

    @Override
    public TypeToken<Period> type() {
        return TypeToken.of(Period.class);
    }

    @Override
    public Period fromString(String string) {
        return Period.parse(string);
    }
}
