package com.eventhub.constant;

import static com.eventhub.constant.ExceptionConstant.UTILITY_CLASS_INSTANTIATION_MESSAGE;

public final class SortConstant {
    public static final String START_DATE_TIME = "startDateTime";
    public static final String PRICE = "price";
    public static final String CREATED_AT = "createdAt";
    public static final String PRICE_PER_HOUR = "pricePerHour";

    private SortConstant() {
        throw new IllegalStateException(UTILITY_CLASS_INSTANTIATION_MESSAGE);
    }
}