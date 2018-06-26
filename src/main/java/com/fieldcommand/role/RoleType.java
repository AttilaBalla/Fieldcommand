package com.fieldcommand.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum RoleType {

    ROLE_OWNER,
    ROLE_ADMIN,
    ROLE_DEVELOPER,
    ROLE_USER,
    ROLE_NEW,
    ROLE_DISABLED;

    private List<String> authorities;

    static {

        ROLE_OWNER.authorities = Arrays.asList(
                "AUTHORITY_ADMIN",
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT",
                "AUTHORITY_GR_ADMIN",
                "AUTHORITY_NEWS_CREATE",
                "AUTHORITY_NEWS_UPDATE",
                "AUTHORITY_NEWS_MODERATE",
                "AUTHORITY_NEWS_DELETE",
                "AUTHORITY_RELEASE_CREATE",
                "AUTHORITY_RELEASE_EDIT",
                "AUTHORITY_RELEASE_DELETE"
        );

        ROLE_ADMIN.authorities = Arrays.asList(
                "AUTHORITY_ADMIN",
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT",
                "AUTHORITY_GR_ADMIN",
                "AUTHORITY_NEWS_CREATE",
                "AUTHORITY_NEWS_UPDATE",
                "AUTHORITY_NEWS_MODERATE",
                "AUTHORITY_NEWS_DELETE",
                "AUTHORITY_RELEASE_CREATE",
                "AUTHORITY_RELEASE_EDIT",
                "AUTHORITY_RELEASE_DELETE"
        );

        ROLE_DEVELOPER.authorities = Arrays.asList(
                "AUTHORITY_GR_VIEW",
                "AUTHORITY_GR_COMMENT",
                "AUTHORITY_GR_ADMIN",
                "AUTHORITY_NEWS_CREATE",
                "AUTHORITY_NEWS_UPDATE",
                "AUTHORITY_NEWS_DELETE",
                "AUTHORITY_RELEASE_CREATE",
                "AUTHORITY_RELEASE_EDIT",
                "AUTHORITY_RELEASE_DELETE"
        );

        ROLE_USER.authorities = Arrays.asList(
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
