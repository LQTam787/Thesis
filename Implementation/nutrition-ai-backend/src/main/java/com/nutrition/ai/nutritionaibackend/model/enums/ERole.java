package com.nutrition.ai.nutritionaibackend.model.enums;

/**
 * The ERole enum defines the roles that a user can have within the system.
 * This is used for authorization and access control purposes.
 */
public enum ERole {
    /**
     * Standard user role with basic permissions.
     */
    ROLE_USER,

    /**
     * Administrator role with elevated permissions to manage the system.
     */
    ROLE_ADMIN
}
