package com.sismics.music.core.dao.dbi.criteria;

/**
 * Playlist criteria.
 *
 * @author jtremeaux
 */
public class PlaylistCriteria extends BaseCriteria {
    /**
     * Returns the default playlist.
     */
    private Boolean defaultPlaylist;

    public Boolean getDefaultPlaylist() {
        return this.defaultPlaylist;
    }

    public PlaylistCriteria setDefaultPlaylist(Boolean defaultPlaylist) {
        this.defaultPlaylist = defaultPlaylist;
        return this;
    }
}