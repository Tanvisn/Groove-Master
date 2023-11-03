package com.sismics.music.core.dao.dbi.criteria;

/**
 * Album criteria.
 *
 * @author jtremeaux
 */

public abstract class BaseCriteria {
    /**
     * ID field.
     */
    protected String id;

    /**
     * Name (like) field.
     */
    protected String nameLike;

    /**
     * User ID field.
     */
    protected String userId;

    public String getId() {
        return this.id;
    }

    public BaseCriteria setId(String id) {
        this.id = id;
        return this;
    }

    public String getNameLike() {
        return nameLike;
    }

    public BaseCriteria setNameLike(String nameLike) {
        this.nameLike = nameLike;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public BaseCriteria setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
