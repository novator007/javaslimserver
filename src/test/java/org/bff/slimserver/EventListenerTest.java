/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.slimserver;

import org.bff.slimserver.events.*;
import org.bff.slimserver.exception.ConnectionException;
import org.bff.slimserver.exception.SqueezeException;
import org.bff.slimserver.exception.PlayerException;
import org.bff.slimserver.domain.*;
import org.bff.slimserver.domain.favorite.Favorite;
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
        getPLAYLIST().clear();
        deleteSavedPlaylists();
        Thread.sleep(1000);
        getLISTENER().start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        getLISTENER().stop();
    }

    @Before
    public void setUp() throws SqueezeException {
        if (getPLAYLIST().getItemCount() < 1) {
            getPLAYLIST().addAlbum(new ArrayList<Album>(Controller.getInstance().getAlbums()).get(0));

        }
    }

    @After
    public void tearDown() {
    }

    private static void deleteSavedPlaylists() throws SqueezeException {
        for (SavedPlaylist pl : getSAVED_PLAYLIST_MANAGER().getPlaylists()) {
            getSAVED_PLAYLIST_MANAGER().deleteSavedPlaylist(pl);
        }
    }

    @Test
    public void testListen() throws SqueezeException, IOException {
//        final EventListener listener = new EventListener(getPLAYER());
//
//        new Thread(new Runnable() {
//
//            public void run() {
//                try {
//                    getLISTENER().listen();
//
//                } catch (ConnectionException ex) {
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
//        getLISTENER().stopListening();
    }

    boolean success = false;

    @Test
    public void testPlayerSleepStart() throws ConnectionException, PlayerException {
        success = false;

        getLISTENER().addSleepChangeListener(new SleepChangeListener() {

            @Override
            public void sleepTimeChanged(SleepChangeEvent event) {
                switch (event.getId()) {
                    case SleepChangeEvent.SLEEP_STARTED:
                        success = true;
                        break;
                }
            }
        });


        getPLAYER().setSleepTime(1000);

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getPLAYER().setSleepTime(0);
        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerSleepStop() throws ConnectionException, PlayerException {
        success = false;

        getPLAYER().setSleepTime(1000);

        getLISTENER().addSleepChangeListener(new SleepChangeListener() {

            @Override
            public void sleepTimeChanged(SleepChangeEvent event) {
                switch (event.getId()) {
                    case SleepChangeEvent.SLEEP_STOPPED:
                        success = true;
                        break;
                }
            }
        });


        getPLAYER().setSleepTime(0);

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
    public void testPlayerOn() throws ConnectionException, PlayerException {
        getPLAYER().powerOff();
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_ON:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().powerOn();

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
    public void testPlayerOff() throws ConnectionException, PlayerException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_OFF:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().powerOff();

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
    public void testPlayerPause() throws ConnectionException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_PAUSED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().play();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        getPLAYER().pause();

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
    public void testPlayerPlay() throws ConnectionException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_STARTED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().play();

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
    public void testPlayerStop() throws ConnectionException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_STOPPED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().stop();

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
    public void testPlayerSync() throws ConnectionException, PlayerException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_SYNCED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().syncPlayer(getPLAYER());

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
    public void testPlayerUnSync() throws ConnectionException, PlayerException {
        success = false;

        getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

            @Override
            public void playerChanged(PlayerChangeEvent event) {
                switch (event.getId()) {
                    case PlayerChangeEvent.PLAYER_UNSYNCED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYER().unsyncPlayer();

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
    public void testRepeatOff() throws SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_OFF:
                        success = true;
                        break;
                }
            }
        });


        getPLAYLIST().repeatOff();

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
    public void testRepeatItem() throws SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_ITEM:
                        success = true;
                        break;
                }
            }
        });


        getPLAYLIST().repeatItem();

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
    public void testRepeatPlaylist() throws SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.REPEAT_PLAYLIST:
                        success = true;
                        break;
                }
            }
        });


        getPLAYLIST().repeatPlaylist();

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
    public void testPlayerVolume() throws ConnectionException, PlayerException {
        success = false;

        getLISTENER().addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });

        getPLAYER().setVolume(100);

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
    public void testDatabaseScanStart() throws ConnectionException {
        success = false;

        DatabaseScanListener dbl = new DatabaseScanListener() {

            @Override
            public void databaseScanEventReceived(DatabaseScanEvent event) {
                if (event.getId() == DatabaseScanEvent.SCAN_STARTED) {
                    success = true;
                }
            }
        };

        getLISTENER().addDatabaseScanListener(dbl);

        getDATABASE().rescan();

        int count = 0;
        while (!success && count++ < 10) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        count = 0;
        while (getDATABASE().isRescanning() && count++ < 100) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getLISTENER().removeDatabaseScanListener(dbl);

        Assert.assertTrue(success);
    }

    @Test
    public void testDatabaseScanStop() throws ConnectionException {
        success = false;

        DatabaseScanListener dbl = new DatabaseScanListener() {

            @Override
            public void databaseScanEventReceived(DatabaseScanEvent event) {
                if (event.getId() == DatabaseScanEvent.SCAN_ENDED) {
                    success = true;
                }
            }
        };

        getLISTENER().addDatabaseScanListener(dbl);

        getDATABASE().rescan();

        int count = 0;
        while (!success && count++ < 120) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(EventListenerTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        getLISTENER().removeDatabaseScanListener(dbl);

        Assert.assertTrue(success);
    }

    @Test
    public void testSavedPlaylistNew() throws SqueezeException {
        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_ADDED:
                        success = true;
                        break;
                }
            }
        });

        getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");

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
    public void testSavedPlaylistDelete() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_DELETED:
                        success = true;
                        break;
                }
            }
        });

        getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");

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
    public void testSavedPlaylistRename() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.PLAYLIST_RENAMED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        getSAVED_PLAYLIST_MANAGER().renamePlaylist(pl, "Temp1");

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
    public void testSavedPlaylistAddItem() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_ADDED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, new ArrayList<Song>(Controller.getInstance().getSongs()).get(0));

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
    public void testSavedPlaylistDeleteItem() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_DELETED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        Song s = new ArrayList<Song>(Controller.getInstance().getSongs()).get(0);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s);
        getSAVED_PLAYLIST_MANAGER().deletePlaylistSong(pl, s);

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
    public void testSavedPlaylistUpItem() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        Song s1 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(0);
        Song s2 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s2);

        getSAVED_PLAYLIST_MANAGER().movePlaylistSongUp(pl, s2);
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
    public void testSavedPlaylistDownItem() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        Song s1 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(0);
        Song s2 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s2);

        getSAVED_PLAYLIST_MANAGER().movePlaylistSongDown(pl, s1);
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
    public void testSavedPlaylistMoveItem() throws SqueezeException {
        deleteSavedPlaylists();

        success = false;

        getLISTENER().addSavedPlaylistChangeListener(new SavedPlaylistChangeListener() {

            @Override
            public void playlistChanged(SavedPlaylistChangeEvent event) {
                switch (event.getId()) {
                    case SavedPlaylistChangeEvent.ITEM_MOVED:
                        success = true;
                        break;
                }
            }
        });

        SavedPlaylist pl = getSAVED_PLAYLIST_MANAGER().createEmptyPlaylist("Temp");
        Song s1 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(0);
        Song s2 = new ArrayList<Song>(Controller.getInstance().getSongs()).get(1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s1);
        getSAVED_PLAYLIST_MANAGER().addPlaylistSong(pl, s2);

        getSAVED_PLAYLIST_MANAGER().movePlaylistSong(pl, s1, 2);
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
    public void testAddFavorite() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addFavoritesChangeListener(new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                switch (event.getId()) {
                    case FavoriteChangeEvent.FAVORITE_ADDED_REMOTELY:
                        success = true;
                        break;
                }
            }
        });


        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getFAVORITE_PLUGIN().addFavorite(item);

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
    public void testDeleteFavorite() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addFavoritesChangeListener(new FavoriteChangeListener() {

            @Override
            public void favoritesChanged(FavoriteChangeEvent event) {
                switch (event.getId()) {
                    case FavoriteChangeEvent.FAVORITE_REMOVED_REMOTELY:
                        success = true;
                        break;
                }
            }
        });


        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        if (getFAVORITE_PLUGIN().getFavorites().size() < 1) {
            getFAVORITE_PLUGIN().addFavorite(item);
        }

        Favorite fav = new ArrayList<Favorite>(getFAVORITE_PLUGIN().getFavorites()).get(0);
        getFAVORITE_PLUGIN().deleteFavorite(fav);

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
    public void testAddItem() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_ADDED:
                        success = true;
                        break;
                }
            }
        });

        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPLAYLIST().addItem(item);

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
    public void testDeleteItem() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_DELETED:
                        success = true;
                        break;
                }
            }
        });

        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPLAYLIST().addItem(item);

        getPLAYLIST().removeItem(new ArrayList<PlaylistItem>(getPLAYLIST().getItems()).get(0));

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
    public void testLoadItem() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_LOADED:
                        success = true;
                        break;
                }
            }
        });

        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);

        getPLAYLIST().playItemClearPlaylist(item);

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
    public void testInsertItem() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.ITEM_INSERTED:
                        success = true;
                        break;
                }
            }
        });

        PlayableItem item = Controller.getInstance().getDatabaseSongs().get(0);
        getPLAYLIST().insertItem(item);

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
    public void testPlaylistCleared() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.PLAYLIST_CLEARED:
                        success = true;
                        break;
                }
            }
        });

        getPLAYLIST().clear();

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
    public void testPlaylistShuffleItems() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_ITEMS:
                        success = true;
                        break;
                }
            }
        });

        getPLAYLIST().shuffleItems();

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
    public void testPlaylistShuffleOff() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_OFF:
                        success = true;
                        break;
                }
            }
        });

        getPLAYLIST().shuffleOff();

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
    public void testPlaylistShuffleAlbums() throws ConnectionException, SqueezeException {
        success = false;

        getLISTENER().addPlaylistChangeListener(new PlaylistChangeListener() {

            @Override
            public void playlistChanged(PlaylistChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistChangeEvent.SHUFFLE_ALBUMS:
                        success = true;
                        break;
                }
            }
        });

        getPLAYLIST().shuffleAlbums();

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
    public void testAddPlayer() throws ConnectionException, SqueezeException {
    success = false;

    getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

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
    public void testDeletePlayer() throws ConnectionException, SqueezeException {
    success = false;

    getLISTENER().addPlayerChangeListener(new PlayerChangeListener() {

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
