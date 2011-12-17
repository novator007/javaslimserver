/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.events.*;
import org.bff.slimserver.exception.SlimConnectionException;
import org.bff.slimserver.exception.SlimException;
import org.bff.slimserver.exception.SlimPlayerException;
import org.bff.slimserver.musicobjects.*;
import org.bff.slimserver.musicobjects.favorite.SlimFavorite;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Number of requests for a 60 seconds period:
 * before:Seconds:60
 * count:544
 * count:545
 * count:546
 * count:547
 * count:548
 * count:549
 * count:550
 * count:551
 * count:552
 * count:553
 * count:554
 * count:555
 * count:556
 * count:557
 * count:558
 * count:559
 * <p/>
 * after:
 * Seconds:60
 * count:326 command:00:00:00:00:00:01 status 0 0
 * count:327 command:00:00:00:00:00:01 playlist index ?
 * count:328 command:00:00:00:00:00:01 time ?
 *
 * @author bfindeisen
 */
public class EventListenerTest extends Base {

    public EventListenerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        getPlaylist().clear();
        deleteSavedPlaylists();
        Thread.sleep(1000);
        getListener().start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        getListener().stop();
    }

    @Before
    public void setUp() throws SlimException {
        if (getPlaylist().getItemCount() < 1) {
            getPlaylist().addAlbum(new ArrayList<SlimAlbum>(Controller.getInstance().getAlbums()).get(0));

        }
    }

    @After
    public void tearDown() {
    }

    private static void deleteSavedPlaylists() throws SlimException {
        for (SlimSavedPlaylist pl : getSavedPlaylistManager().getPlaylists()) {
            getSavedPlaylistManager().deleteSavedPlaylist(pl);
        }
    }

    @Test
    public void testListen() throws SlimException, IOException {
//        final SlimEventListener listener = new SlimEventListener(getPlayer());
//
//        new Thread(new Runnable() {
//
//            public void run() {
//                try {
//                    getListener().listen();
//
//                } catch (SlimConnectionException ex) {
//                    Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
//                } catch (IOException ex) {
//                    Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        }).start();
//
//        try {
//            Thread.sleep(120000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        getListener().stopListening();
    }

    boolean success = false;

    @Test
    public void testPlayerSleepStart() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getListener().addSleepChangeListener(new SleepChangeListener() {

            @Override
            public void sleepTimeChanged(SleepChangeEvent event) {
                switch (event.getId()) {
                    case SleepChangeEvent.SLEEP_STARTED:
                        success = true;
                        break;
                }
            }
        });


        getPlayer().setSleepTime(1000);

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getPlayer().setSleepTime(0);
        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerSleepStop() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getPlayer().setSleepTime(1000);

        getListener().addSleepChangeListener(new SleepChangeListener() {

            @Override
            public void sleepTimeChanged(SleepChangeEvent event) {
                switch (event.getId()) {
                    case SleepChangeEvent.SLEEP_STOPPED:
                        success = true;
                        break;
                }
            }
        });


        getPlayer().setSleepTime(0);

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerOn() throws SlimConnectionException, SlimPlayerException {
        getPlayer().powerOff();
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_ON:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().powerOn();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerOff() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_OFF:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().powerOff();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerPause() throws SlimConnectionException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_PAUSED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().play();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPlayer().pause();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerPlay() throws SlimConnectionException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_STARTED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().play();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerStop() throws SlimConnectionException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_STOPPED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().stop();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerSync() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_SYNCED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().syncPlayer(getPlayer());

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerUnSync() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getListener().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_UNSYNCED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().unsyncPlayer();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testRepeatOff() throws SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_OFF:
                        success = true;
                        break;
                }
            }
        });


        getPlaylist().repeatOff();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testRepeatItem() throws SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_ITEM:
                        success = true;
                        break;
                }
            }
        });


        getPlaylist().repeatItem();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testRepeatPlaylist() throws SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_PLAYLIST:
                        success = true;
                        break;
                }
            }
        });


        getPlaylist().repeatPlaylist();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerVolume() throws SlimConnectionException, SlimPlayerException {
        success = false;

        getListener().addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });

        getPlayer().setVolume(100);

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testDatabaseScanStart() throws SlimConnectionException {
        success = false;

        DatabaseScanListener dbl = new DatabaseScanListener() {

            @Override
            public void databaseScanEventReceived(DatabaseScanEvent event) {
                if (event.getId() == DatabaseScanEvent.SCAN_STARTED) {
                    success = true;
                }
            }
        };

        getListener().addDatabaseScanListener(dbl);

        getDatabase().rescan();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        count = 0;
        while (getDatabase().isRescanning() && count++ < 100) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getListener().removeDatabaseScanListener(dbl);

        Assert.assertTrue(success);
    }

    @Test
    public void testDatabaseScanStop() throws SlimConnectionException {
        success = false;

        DatabaseScanListener dbl = new DatabaseScanListener() {

            @Override
            public void databaseScanEventReceived(DatabaseScanEvent event) {
                if (event.getId() == DatabaseScanEvent.SCAN_ENDED) {
                    success = true;
                }
            }
        };

        getListener().addDatabaseScanListener(dbl);

        getDatabase().rescan();

        int count = 0;
        while (!success && count++ < 120) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getListener().removeDatabaseScanListener(dbl);

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistNew() throws SlimException {
        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_ADDED:
                        success = true;
                        break;
                }
            }
        });

        getSavedPlaylistManager().createEmptyPlaylist("Temp");

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistDelete() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_DELETED:
                        success = true;
                        break;
                }
            }
        });

        getSavedPlaylistManager().createEmptyPlaylist("Temp");

        deleteSavedPlaylists();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistRename() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_RENAMED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().renamePlaylist(pl, "Temp1");

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistAddItem() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_ADDED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        getSavedPlaylistManager().addPlaylistSong(pl, new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(0));

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistDeleteItem() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_DELETED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        SlimSong s = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(0);
        getSavedPlaylistManager().addPlaylistSong(pl, s);
        getSavedPlaylistManager().deletePlaylistSong(pl, s);

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistUpItem() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        SlimSong s1 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(0);
        SlimSong s2 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(1);
        getSavedPlaylistManager().addPlaylistSong(pl, s1);
        getSavedPlaylistManager().addPlaylistSong(pl, s2);

        getSavedPlaylistManager().movePlaylistSongUp(pl, s2);
        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistDownItem() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        SlimSong s1 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(0);
        SlimSong s2 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(1);
        getSavedPlaylistManager().addPlaylistSong(pl, s1);
        getSavedPlaylistManager().addPlaylistSong(pl, s2);

        getSavedPlaylistManager().movePlaylistSongDown(pl, s1);
        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistMoveItem() throws SlimException {
        deleteSavedPlaylists();

        success = false;

        getListener().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SlimSavedPlaylist pl = getSavedPlaylistManager().createEmptyPlaylist("Temp");
        SlimSong s1 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(0);
        SlimSong s2 = new ArrayList<SlimSong>(Controller.getInstance().getSongs()).get(1);
        getSavedPlaylistManager().addPlaylistSong(pl, s1);
        getSavedPlaylistManager().addPlaylistSong(pl, s2);

        getSavedPlaylistManager().movePlaylistSong(pl, s1, 2);
        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        deleteSavedPlaylists();

        Assert.assertTrue(success);
    }

    @Test
    public void testAddFavorite() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addFavoritesChangeListener(new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                switch (event.getId()) {
                    case FavoriteChangeEvent.FAVORITE_ADDED_REMOTELY:
                        success = true;
                        break;
                }
            }
        });


        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getFavoritePlugin().addFavorite(item);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testDeleteFavorite() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addFavoritesChangeListener(new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                switch (event.getId()) {
                    case FavoriteChangeEvent.FAVORITE_REMOVED_REMOTELY:
                        success = true;
                        break;
                }
            }
        });


        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        if (getFavoritePlugin().getFavorites().size() < 1) {
            getFavoritePlugin().addFavorite(item);
        }

        SlimFavorite fav = new ArrayList<SlimFavorite>(getFavoritePlugin().getFavorites()).get(0);
        getFavoritePlugin().deleteFavorite(fav);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testAddItem() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_ADDED:
                        success = true;
                        break;
                }
            }
        });

        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().addItem(item);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testDeleteItem() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_DELETED:
                        success = true;
                        break;
                }
            }
        });

        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().addItem(item);

        getPlaylist().removeItem(new ArrayList<SlimPlaylistItem>(getPlaylist().getItems()).get(0));

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testLoadItem() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_LOADED:
                        success = true;
                        break;
                }
            }
        });

        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().playItemClearPlaylist(item);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testInsertItem() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_INSERTED:
                        success = true;
                        break;
                }
            }
        });

        SlimPlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);
        getPlaylist().insertItem(item);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistCleared() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.PLAYLIST_CLEARED:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().clear();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistShuffleItems() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_ITEMS:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().shuffleItems();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistShuffleOff() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_OFF:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().shuffleOff();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistShuffleAlbums() throws SlimConnectionException, SlimException {
        success = false;

        getListener().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_ALBUMS:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().shuffleAlbums();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Assert.assertTrue(success);
    }
    /*
    @Test
    public void testAddPlayer() throws SlimConnectionException, SlimException {
    success = false;

    getListener().addPlayerChangeListener(new PlayerChangeListener() {

    @Override
    public void playerChanged(PlayerChangeEvent event) {
    switch (event.getId()) {
    case PlayerChangeEvent.PLAYER_ADDED:
    success = true;
    break;
    }
    }
    });

    System.out.println("--------------ADD PLAYER NOW--------------------");

    int count = 0;
    while (!success && count++ < 100) {
    try {
    Thread.sleep(500);
    } catch (InterruptedException ex) {
    Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    Assert.assertTrue(success);
    }

    @Test
    public void testDeletePlayer() throws SlimConnectionException, SlimException {
    success = false;

    getListener().addPlayerChangeListener(new PlayerChangeListener() {

    @Override
    public void playerChanged(PlayerChangeEvent event) {
    switch (event.getId()) {
    case PlayerChangeEvent.PLAYER_DELETED:
    success = true;
    break;
    }
    }
    });

    System.out.println("--------------REMOVE PLAYER NOW--------------------");

    int count = 0;
    while (!success && count++ < 100) {
    try {
    Thread.sleep(500);
    } catch (InterruptedException ex) {
    Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
    }
    }

    Assert.assertTrue(success);
    }
     */

    public static String formatTime(long seconds) {
        final int MINUTES_PER_HOUR = 60;
        final int SECONDS_PER_MINUTE = 60;
        final int NUM10 = 10;

        String label = "0:00";
        if (seconds > 0) {
            int hours = (int) (seconds / (MINUTES_PER_HOUR * SECONDS_PER_MINUTE));
            int minutes = ((int) (seconds - (hours * (MINUTES_PER_HOUR * SECONDS_PER_MINUTE)))) / SECONDS_PER_MINUTE;
            int secs = (int) seconds - (hours * (MINUTES_PER_HOUR * SECONDS_PER_MINUTE) + (minutes * SECONDS_PER_MINUTE));
            label = ((hours > 0 ? hours + ":" : "") + (minutes + ":") + (secs < NUM10 ? "0" + secs : Integer.toString(secs)));
        }
        return (label);
    }

// TODO add test methods here.
// The methods must be annotated with annotation @Test. For example:
//
// @Test
// public void hello() {}
}
