package com.sismics.music.core.dao.dbi;

import com.sismics.music.core.dao.dbi.criteria.TrackCriteria;
import com.sismics.music.core.dao.dbi.dto.TrackDto;
import com.sismics.music.core.dao.dbi.mapper.TrackDtoMapper;
import com.sismics.music.core.dao.dbi.mapper.TrackMapper;
import com.sismics.music.core.model.dbi.Track;
import com.sismics.music.core.util.dbi.QueryParam;
import com.sismics.music.core.util.dbi.SortCriteria;
import com.sismics.util.context.ThreadLocalContext;
import com.sismics.util.dbi.BaseDao;
import com.sismics.util.dbi.filter.FilterCriteria;
import org.skife.jdbi.v2.Handle;

import java.sql.Timestamp;
import java.util.*;

/**
 * Track DAO.
 * 
 * @author jtremeaux
 */
public class TrackDao extends BaseDao<TrackDto, TrackCriteria> {
    @Override
    protected QueryParam getQueryParam(TrackCriteria criteria, FilterCriteria filterCriteria) 
    {
        StringBuilder sb = new StringBuilder(
                "select t.id as id, t.filename as fileName, t.title as title, t.year as year, t.genre as genre, t.length as length, t.bitrate as bitrate, t.number as trackOrder, t.vbr as vbr, t.format as format,");
        Map<String, Object> parameterMap = new HashMap<>();
        List<String> criteriaList = createCriteriaList(criteria, parameterMap);
        SortCriteria sortCriteria = createSortCriteria(criteria);

        return new QueryParam(sb.toString(), criteriaList, parameterMap, sortCriteria, filterCriteria,
                new TrackDtoMapper());
    }

    private List<String> createCriteriaList(TrackCriteria criteria, Map<String, Object> parameterMap) {
        List<String> criteriaList = new ArrayList<>();
        criteriaList.add("t.deletedate is null");

        if (criteria.getPlaylistId() != null) {
            addPlaylistCriteria(criteria, criteriaList, parameterMap);
        }

        if (criteria.getAlbumId() != null) {
            criteriaList.add("t.album_id = :albumId");
            parameterMap.put("albumId", criteria.getAlbumId());
        }

        if (criteria.getDirectoryId() != null) {
            criteriaList.add("alb.directory_id = :directoryId");
            parameterMap.put("directoryId", criteria.getDirectoryId());
        }

        if (criteria.getArtistId() != null) {
            criteriaList.add("a.id = :artistId");
            parameterMap.put("artistId", criteria.getArtistId());
        }

        if (criteria.getTitle() != null) {
            criteriaList.add("lower(t.title) like lower(:title)");
            parameterMap.put("title", criteria.getTitle());
        }

        if (criteria.getArtistName() != null) {
            criteriaList.add("lower(a.name) like lower(:artistName)");
            parameterMap.put("artistName", criteria.getArtistName());
        }

        if (criteria.getLike() != null) {
            criteriaList.add(
                    "(lower(t.title) like lower(:like) or lower(alb.name) like lower(:like) or lower(a.name) like lower(:like))");
            parameterMap.put("like", "%" + criteria.getLike() + "%");
        }

        if (criteria.getUserId() != null) {
            parameterMap.put("userId", criteria.getUserId());
        }

        return criteriaList;
    }

    private void addPlaylistCriteria(TrackCriteria criteria, List<String> criteriaList,
            Map<String, Object> parameterMap) {
        criteriaList.add("pt.track_id = t.id");
        criteriaList.add("pt.playlist_id = :playlistId");
        parameterMap.put("playlistId", criteria.getPlaylistId());
    }

    private SortCriteria createSortCriteria(TrackCriteria criteria) {
        if (criteria.getPlaylistId() != null) {
            return new SortCriteria(" order by pt.number asc");
        } else if (criteria.getLike() != null || criteria.getArtistId() != null) {
            return new SortCriteria(" order by alb.name, t.number, t.title asc");
        } else if (criteria.getRandom() != null && criteria.getRandom()) {
            return new SortCriteria(" order by rand()");
        } else {
            return new SortCriteria(" order by t.number, t.title asc");
        }
    }

    /**
     * Creates a new track.
     * 
     * @param track Track to create
     * @return Track ID
     */
    public String create(Track track) {
        track.setId(UUID.randomUUID().toString());
        track.setCreateDate(new Date());

        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("insert into " +
                "  t_track(id, album_id, artist_id, filename, title, titlecorrected, year, genre, length, bitrate, number, vbr, format, createdate)"
                +
                "  values(:id, :albumId, :artistId, :fileName, :title, :titleCorrected, :year, :genre, :length, :bitrate, :number, :vbr, :format, :createDate)")
                .bind("id", track.getId())
                .bind("albumId", track.getAlbumId())
                .bind("artistId", track.getArtistId())
                .bind("fileName", track.getFileName())
                .bind("title", track.getTitle())
                .bind("titleCorrected", track.getTitleCorrected())
                .bind("year", track.getYear())
                .bind("genre", track.getGenre())
                .bind("length", track.getLength())
                .bind("bitrate", track.getBitrate())
                .bind("number", track.getOrder())
                .bind("vbr", track.isVbr())
                .bind("format", track.getFormat())
                .bind("createDate", new Timestamp(track.getCreateDate().getTime()))
                .execute();

        return track.getId();
    }

    /**
     * Updates a track.
     * 
     * @param track Track to update
     * @return Updated track
     */
    public Track update(Track track) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update t_track t set " +
                " t.album_id = :albumId, " +
                " t.artist_id = :artistId, " +
                " t.filename = :fileName, " +
                " t.title = :title, " +
                " t.titlecorrected = :titleCorrected, " +
                " t.year = :year, " +
                " t.genre = :genre, " +
                " t.length = :length, " +
                " t.bitrate = :bitrate, " +
                " t.number = :number, " +
                " t.vbr = :vbr, " +
                " t.format = :format, " +
                " t.createdate = :createDate " +
                " where t.id = :id and t.deletedate is null")
                .bind("id", track.getId())
                .bind("albumId", track.getAlbumId())
                .bind("artistId", track.getArtistId())
                .bind("fileName", track.getFileName())
                .bind("title", track.getTitle())
                .bind("titleCorrected", track.getTitleCorrected())
                .bind("year", track.getYear())
                .bind("genre", track.getGenre())
                .bind("length", track.getLength())
                .bind("bitrate", track.getBitrate())
                .bind("number", track.getOrder())
                .bind("vbr", track.isVbr())
                .bind("format", track.getFormat())
                .bind("createDate", new Timestamp(track.getCreateDate().getTime()))
                .execute();

        return track;
    }

    /**
     * Gets an active track by its file name.
     * 
     * @param directoryId Directory ID
     * @param fileName    Track file name
     * @return Track
     */
    public Track getActiveByDirectoryAndFilename(String directoryId, String fileName) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        return handle.createQuery("select " + new TrackMapper().getJoinedColumns("t") +
                "  from t_track t, t_album a" +
                "  where t.filename = :fileName and t.deletedate is null " +
                "  and a.id = t.album_id and a.directory_id = :directoryId and a.deletedate is null")
                .bind("directoryId", directoryId)
                .bind("fileName", fileName)
                .mapTo(Track.class)
                .first();
    }

    /**
     * Gets active tracks included in a location.
     * 
     * @param directoryId Directory ID
     * @param location    Parent location
     * @return List of tracks
     */
    public List<Track> getActiveByDirectoryInLocation(String directoryId, String location) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        return handle.createQuery("select " + new TrackMapper().getJoinedColumns("t") +
                "  from t_track t, t_album a" +
                "  where locate(:location, t.filename) = 1 and t.deletedate is null " +
                "  and a.id = t.album_id and a.directory_id = :directoryId and a.deletedate is null")
                .bind("directoryId", directoryId)
                .bind("location", location)
                .mapTo(Track.class)
                .list();
    }

    /**
     * Gets an active track by its trackname.
     *
     * @param id Track ID
     * @return Track
     */
    public Track getActiveById(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        return handle.createQuery("select " + new TrackMapper().getJoinedColumns("t") +
                "  from t_track t " +
                "  where t.id = :id ")
                .bind("id", id)
                .mapTo(Track.class)
                .first();
    }

    /**
     * Deletes a track.
     *
     * @param id Track ID
     */
    public void delete(String id) {
        final Handle handle = ThreadLocalContext.get().getHandle();
        handle.createStatement("update t_track t" +
                "  set t.deletedate = :deleteDate" +
                "  where t.deletedate is null and t.id = :id ")
                .bind("id", id)
                .bind("deleteDate", new Timestamp(new Date().getTime()))
                .execute();
    }
}
