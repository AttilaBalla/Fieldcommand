package com.fieldcommand.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RoleType {

    ROLE_ADMIN,
    ROLE_DEVELOPER,
    ROLE_USER,
    ROLE_NEW,
    ROLE_DISABLED;

    private List<String> authorities;

    static {
        ROLE_ADMIN.authorities = Arrays.asList(
                "AUTHORITY_SELF_LOGIN",
                "AUTHORITY_ADMIN_CHANGENAME",
                "AUTHORITY_ADMIN_CHANGEMAIL",
                "AUTHORITY_ADMIN_RESETPW",
                "AUTHORITY_ADMIN_ASSIGNROLE",
                "AUTHORITY_ADMIN_DISABLE",
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT",
                "AUTHORITY_GR_ADMIN",
                "AUTHORITY_NEWS_CREATE",
                "AUTHORITY_NEWS_EDIT",
                "AUTHORITY_NEWS_MODERATE",
                "AUTHORITY_NEWS_DELETE",
                "AUTHORITY_RELEASE_CREATE",
                "AUTHORITY_RELEASE_EDIT",
                "AUTHORITY_RELEASE_DELETE"
        );

        ROLE_DEVELOPER.authorities = Arrays.asList(
                "AUTHORITY_SELF_LOGIN",
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT",
                "AUTHORITY_GR_ADMIN",
                "AUTHORITY_NEWS_CREATE",
                "AUTHORITY_NEWS_EDIT",
                "AUTHORITY_NEWS_DELETE",
                "AUTHORITY_RELEASE_CREATE",
                "AUTHORITY_RELEASE_EDIT",
                "AUTHORITY_RELEASE_DELETE"
        );

        ROLE_USER.authorities = Arrays.asList(
                "AUTHORITY_SELF_LOGIN",
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT"
        );

        ROLE_NEW.authorities = new ArrayList<>();
        ROLE_DISABLED.authorities = new ArrayList<>();
    }

    public List<String> getAuthorities() {
        return authorities;
    }
}
