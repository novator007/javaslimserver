/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver.domain;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * @author bfindeisen
 */
public class PlayableTags {

    public enum IDPREFIX {

        ALBUM("album"),
        ALBUM_ID("album_id"),
        ARTIST("artist"),
        ARTIST_ID("artist_id"),
        ARTWORK_TRACK_ID("artwork_track_id"),
        ARTWORK_URL("artwork_url"),
        BITRATE("bitrate"),
        BPM("bpm"),
        COMMENT("comment"),
        COMPOSER("composer"),
        COMPOSER_ID("band"),
        CONDUCTOR("conductor"),
        CONTRIBUTOR_ID("contributor_id"),
        CONTRIBUTOR("contributor"),
        COVER_ART("coverart"),
        DISC("disc"),
        DISC_COUNT("disccount"),
        DRM("drm"),
        DURATION("duration"),
        FILE_SIZE("filesize"),
        GENRE("genre"),
        GENRE_ID("genre_id"),
        ID("id"),
        LYRICS("lyrics"),
        MOD_TIME("modificationTime"),
        RATING("rating"),
        REMOTE("remote"),
        TAG_VERSION("tagversion"),
        TITLE("title"),
        TRACK("tracknum"),
        TRACK_ID("track_id"),
        TYPE("type"),
        URL("url"),
        YEAR("year"),
        COMPILATION("compilation");

        private String prefix;

        IDPREFIX(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    public static final HashMap<String, IDPREFIX> ID_MAP =
            new HashMap<String, IDPREFIX>();

    static {
        for (IDPREFIX prefix : EnumSet.allOf(IDPREFIX.class)) {
            ID_MAP.put(prefix.getPrefix(), prefix);
        }

    }

    /*
   g   genre               Genre name. Only if known.
   G 	genres              Genre names, separated by commas (only useful if the server is set to handle multiple items in tags).
   p 	genre_id            Genre ID. Only if known.
   P 	genre_id            Genre IDs, separated by commas (only useful if the server is set to handle multiple items in tags).
   a 	artist              Artist name.
   s 	artist_id           Artist ID.
   A 	<role>              For every artist role (one of "artist", "composer", "conductor", "band", "albumartist" or "trackartist"), a comma separated list of names.
   S 	<role>_ids          For each role as defined above, the list of ids.
   l 	album               Album name. Only if known.
   e 	album_id            Album ID. Only if known.
   d 	duration            Song duration in seconds.
   i 	disc                Disc number. Only if known.
   q 	disccount           Number of discs. Only if known.
   t 	tracknum            Track number. Only if known.
   y 	year                Song year. Only if known.
   m 	bpm                 Beats per minute. Only if known.
   k 	comment             Song comments, if any.
   o 	type                Content type. Only if known.
   v 	tagversion          Version of tag information in song file. Only if known.
   r 	bitrate             Song bitrate. Only if known.
   f 	filesize            Song file length in bytes. Only if known.
   j 	coverart            1 if coverart is available for this song. Not listed otherwise.
   J 	artwork_track_id    Identifier of the album track used by the server to display the album's artwork. Not listed if artwork is not available for this album.
   n 	modificationTime    Date and time song file was last changed.
   C 	compilation         1 if the album this track belongs to is a compilation
   Y 	replay_gain         Replay gain (in dB), if any
   X 	album_replay_gain   Replay gain of the album (in dB), if any
   R 	rating              Song rating, if known and greater than 0.
   T 	samplerate          Song sample rate (in KHz)
   I 	samplesize          Song sample size (in bits)
   u 	url                 Song file url.
   w 	lyrics              Lyrics. Only if known.
   x 	remote              If 1, this is a remote track.
   N 	remote_title        Title of the internet radio station.
   K 	artwork_url         A full URL to remote artwork. Only available for certain plugins such as Pandora and Rhapsody.
   B 	buttons             A hash with button definitions. Only available for certain plugins such as Pandora.
   L 	info_link           A custom link to use for trackinfo. Only available for certain plugins such as Pandora.
    */
    public static final String RETURN_TAGS = "gpasledtykruoxK";
    public static final Hashtable<String, String> TABLE = new Hashtable<String, String>();

    static {
        TABLE.put("g", IDPREFIX.GENRE.getPrefix());
        TABLE.put("p", IDPREFIX.GENRE_ID.getPrefix());
        TABLE.put("a", IDPREFIX.ARTIST.getPrefix());
        TABLE.put("s", IDPREFIX.ARTIST_ID.getPrefix());
        TABLE.put("l", IDPREFIX.ALBUM.getPrefix());
        TABLE.put("e", IDPREFIX.ALBUM_ID.getPrefix());
        TABLE.put("d", IDPREFIX.DURATION.getPrefix());
        TABLE.put("t", IDPREFIX.TRACK.getPrefix());
        TABLE.put("y", IDPREFIX.YEAR.getPrefix());
        TABLE.put("k", IDPREFIX.COMMENT.getPrefix());
        TABLE.put("r", IDPREFIX.BITRATE.getPrefix());
        TABLE.put("u", IDPREFIX.URL.getPrefix());
        TABLE.put("x", IDPREFIX.REMOTE.getPrefix());
        TABLE.put("K", IDPREFIX.ARTWORK_URL.getPrefix());
    }
}
