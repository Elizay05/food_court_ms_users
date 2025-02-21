package com.example.food_court.domain.util;

public class DomainConstants {

    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final Long ID_ROLE_ADMINISTRATOR = 1L;
    public static final Long ID_ROLE_OWNER = 2L;
    public static final long ID_ROLE_EMPLOYEE = 3L;
    public static final long ID_ROLE_CUSTOMER = 4L;
}
