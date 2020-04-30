package com.gnepux.wsgo.retry;

public interface RetryStrategy {

    long onRetry(int retryCount);

}
