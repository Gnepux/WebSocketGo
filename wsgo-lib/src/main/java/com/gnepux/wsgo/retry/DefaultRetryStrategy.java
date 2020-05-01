package com.gnepux.wsgo.retry;

/**
 * @author gnepux
 */
public class DefaultRetryStrategy implements RetryStrategy {
    @Override
    public long onRetry(long retryCount) {
        long delay;
        if (retryCount < 2) {
            delay = 0;
        } else if (retryCount <= 5) {
            delay = 2 * 1000L;
        } else {
            delay = 30 * 1000L;
        }
        return delay;
    }
}
