package com.sismics.music.core.dao.dbi.dto;

/**
 * Playlist DTO.
 *
 * @author jtremeaux
 */
public class PlaylistDto extends MusicDto{
    
    /**
     * Owner user ID.
     */
    private String userId;

    /**
     * Number of tracks in the playlist.
     */
    private Long playlistTrackCount;

    /**
     * Number of plays in the playlist.
     */
    private Long userTrackPlayCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getPlaylistTrackCount() {
        return playlistTrackCount;
    }

    public void setPlaylistTrackCount(Long playlistTrackCount) {
        this.playlistTrackCount = playlistTrackCount;
    }

    public Long getUserTrackPlayCount() {
        return userTrackPlayCount;
    }

    public void setUserTrackPlayCount(Long userTrackPlayCount) {
        this.userTrackPlayCount = userTrackPlayCount;
    }
}
