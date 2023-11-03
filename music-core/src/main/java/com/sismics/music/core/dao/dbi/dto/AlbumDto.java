package com.sismics.music.core.dao.dbi.dto;

import java.util.Date;

/**
 * Album DTO.
 *
 * @author jtremeaux 
 */
public class AlbumDto extends MusicDto {
    /**
     * Album art ID.
     */
    private String albumArt;

    /**
     * Artist ID.
     */
    private String artistId;
    
    /**
     * Artist name.
     */
    private String artistName;
    
    /**
     * Last update date.
     */
    private Date updateDate;
    
    /**
     * User play count.
     */
    private Long userPlayCount;
    
    public String getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Long getUserPlayCount() {
        return userPlayCount;
    }

    public void setUserPlayCount(Long userPlayCount) {
        this.userPlayCount = userPlayCount;
    }
}
