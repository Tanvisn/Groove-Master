package com.sismics.music.core.dao.dbi.dto;


public abstract class MusicDto {
    /**
     * ID.
     */
    protected String id;

    /**
     * Name.
     */
    protected String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
