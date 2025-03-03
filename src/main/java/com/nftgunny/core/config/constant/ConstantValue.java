package com.nftgunny.core.config.constant;

import java.util.HashMap;
import java.util.Map;

public class ConstantValue {
    public static final String RPC_DOMAIN = "http://127.0.0.1:5050";

    public static final int FOREVER = Integer.MAX_VALUE;
    public static final int SIX_HOURS_MILLISECOND = 1000 * 60 * 60 * 6;
    public static final int SIX_HOURS_SECOND = 3600 * 6;

    public static final String ADMIN_AUTHOR = "hasAuthority('ADMIN')";
    public static final String PLAYER_AUTHOR = "hasAuthority('PLAYER')";

    public static final String REQUEST_ID = "request_id";
    public static final int DEFAULT_PAGE_SIZE = 20;

    public static final String INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error";
    public static final String UNAUTHORIZED_ERROR_MESSAGE = "Unauthorized error";
    public static final String ACCESS_ERROR_MESSAGE = "Access error";

    public static final int DEFAULT_CHAR_DAMAGE = 10;
    public static final int DEFAULT_CHAR_HP = 100;
    public static final int DEFAULT_CHAR_MP = 100;

    public static final int MAX_ACCESSORY_AMOUNT_PER_CHARACTER = 2;
    public static final int MAX_WEAPON_AMOUNT_PER_CHARACTER = 1;
    public static final int MAX_HAT_AMOUNT_PER_CHARACTER = 1;
    public static final int MAX_CLOTHES_AMOUNT_PER_CHARACTER = 1;

    public static final String DATETIME_ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static Map<String, String> SYSTEM_CONFIG = new HashMap<>();
}
