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

package software.amazon.awssdk.services.kinesis;

import static software.amazon.awssdk.core.client.config.SdkClientOption.RETRY_MODE;

import software.amazon.awssdk.annotations.SdkInternalApi;
import software.amazon.awssdk.awscore.retry.AwsRetryPolicy;
import software.amazon.awssdk.core.client.config.SdkClientConfiguration;
import software.amazon.awssdk.core.retry.RetryMode;
import software.amazon.awssdk.core.retry.RetryPolicy;
import software.amazon.awssdk.core.retry.RetryPolicyContext;
import software.amazon.awssdk.core.retry.conditions.AndRetryCondition;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;

/**
 * Default retry policy for the Kinesis Client. Disables retries on subscribe-to-shard.
 */
@SdkInternalApi
final class KinesisRetryPolicy {
    private KinesisRetryPolicy() {}

    public static RetryPolicy defaultPolicy(SdkClientConfiguration config) {
        RetryMode retryMode = config.option(RETRY_MODE);
        RetryPolicy baseRetryPolicy = retryMode == null ? AwsRetryPolicy.defaultRetryPolicy()
                                                        : AwsRetryPolicy.forRetryMode(retryMode);

        return baseRetryPolicy.toBuilder()
                              .retryCondition(AndRetryCondition.create(KinesisRetryPolicy::isNotSubscribeToShard,
                                                                       AwsRetryPolicy.defaultRetryCondition()))
                              .build();
    }

    private static boolean isNotSubscribeToShard(RetryPolicyContext context) {
        return !(context.originalRequest() instanceof SubscribeToShardRequest);
    }
}

