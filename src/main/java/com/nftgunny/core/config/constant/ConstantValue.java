package com.nftgunny.core.config.constant;

public class ConstantValue {
    public final static String RPC_DOMAIN = "http://127.0.0.1:5050";

    public static final int FOREVER = Integer.MAX_VALUE;
    public static final int SIX_HOURS_MILLISECOND = 1000 * 60 * 60 * 6;
    public static final int SIX_HOURS_SECOND = 3600 * 6;

    public static final String REQUEST_ID = "request_id";
    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized error";
    public static final String ACCESS_ERROR_MESSAGE = "Access error";

    public static final String DATETIME_ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
}
