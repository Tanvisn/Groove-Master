package com.sismics.music.core.dao.dbi.criteria;

/**
 * Album criteria.
 *
 * @author jtremeaux
 */
public class AlbumCriteria extends BaseCriteria {
    /**
     * Directory ID.
     */
    private String directoryId;

    /**
     * Artist ID.
     */
    private String artistId;

    public String getDirectoryId() {
        return this.directoryId;
    }

    public AlbumCriteria setDirectoryId(String directoryId) {
        this.directoryId = directoryId;
        return this;
    }

    public String getArtistId() {
        return artistId;
    }

    public AlbumCriteria setArtistId(String artistId) {
        this.artistId = artistId;
        return this;
    }
}
