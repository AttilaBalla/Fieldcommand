package com.fieldcommand.model;

public enum Privilege {

    ADD_USER_PRIVILEGE,
    ADD_BLOGPOST_PRIVILEGE,
    ADD_RELEASE_PRIVILEGE,

    // privileges to edit content submitted by other users
    EDIT_USER_PRIVILEGE,
    EDIT_BLOGPOST_PRIVILEGE,
    EDIT_RELEASE_PRIVILEGE,

    DELETE_USER_PRIVILEGE,
    DELETE_BLOGPOST_PRIVILEGE,
    DELETE_RELEASE_PRIVILEGE

}
