package com.gnepux.wsgo.constant;

/**
 * @author gnepux
 */
public class Constants {

    /**
     * Normal Close Code
     * https://tools.ietf.org/html/rfc6455#page-64
     */
    public static final int CODE_NORMAL_CLOSE = 1000;
    public static final int CODE_VALID_MAX = 4999;
    public static final int CODE_VALID_MIN = 1000;

    /**
     * default ping interval in ms
     */
    public static final long DEFAULT_PING_INTERVAL = 30 * 1000L;

    /**
     * default connect timeout in ms
     */
    public static final long DEFAULT_CONNECT_TIMEOUT = 6 * 1000L;

    /**
     * default read timeout in ms
     */
    public static final long DEFAULT_READ_TIMEOUT = 10 * 1000L;

    /**
     * default write timeout in ms
     */
    public static final long DEFAULT_WRITE_TIMEOUT = 10 * 1000L;
}
