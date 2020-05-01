package com.gnepux.wsgo.retry;

/**
 * Retry strategy when reconnect.
 *
 * @author gnepux
 * @see DefaultRetryStrategy
 */
public interface RetryStrategy {

    /**
     * The relationship of retry count and delay time,
     * WsGo will call the method for every reconnect.
     *
     * @param retryCount The retry time that WsGo has retied.
     * @return The delay time in milliseconds.
     */
    long onRetry(long retryCount);

}
