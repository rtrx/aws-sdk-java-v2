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

package software.amazon.awssdk.core.retry.conditions;

import software.amazon.awssdk.annotations.SdkPublicApi;
import software.amazon.awssdk.core.retry.RetryPolicyContext;
import software.amazon.awssdk.core.retry.RetryUtils;
import software.amazon.awssdk.utils.ToString;

/**
 * A {@link RetryCondition} that will return true if the provided exception seems to be due to a clock skew between the
 * client and service.
 */
@SdkPublicApi
public final class RetryOnClockSkewCondition implements RetryCondition {
    private RetryOnClockSkewCondition() {}

    public static RetryOnClockSkewCondition create() {
        return new RetryOnClockSkewCondition();
    }

    @Override
    public boolean shouldRetry(RetryPolicyContext context) {
        return RetryUtils.isClockSkewException(context.exception());
    }

    @Override
    public String toString() {
        return ToString.create("RetryOnClockSkewCondition");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
