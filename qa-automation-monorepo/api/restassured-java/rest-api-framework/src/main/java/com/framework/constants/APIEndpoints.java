package com.framework.constants;

/**
 * Centralised API endpoint constants.
 * All paths are relative to the base URL + API version prefix.
 */
public final class APIEndpoints {

    private APIEndpoints() {}

    // ── Users ──────────────────────────────────────────────────────────────
    public static final String USERS          = "/users";
    public static final String USER_BY_ID     = "/users/{id}";

    // ── Posts ─────────────────────────────────────────────────────────────
    public static final String POSTS          = "/posts";
    public static final String POST_BY_ID     = "/posts/{id}";
    public static final String POSTS_BY_USER  = "/posts?userId={userId}";

    // ── Comments ──────────────────────────────────────────────────────────
    public static final String COMMENTS       = "/comments";
    public static final String COMMENT_BY_ID  = "/comments/{id}";

    // ── Auth ──────────────────────────────────────────────────────────────
    public static final String LOGIN          = "/auth/login";
    public static final String REFRESH_TOKEN  = "/auth/refresh";
    public static final String LOGOUT         = "/auth/logout";

    // ── Health ────────────────────────────────────────────────────────────
    public static final String HEALTH         = "/health";
}
