package org.bff.squeezeserver;

import org.bff.squeezeserver.domain.XMLPluginItem;
import org.bff.squeezeserver.domain.podcast.Podcast;
import org.bff.squeezeserver.domain.podcast.PodcastAudioDetails;
import org.bff.squeezeserver.exception.NetworkException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bill
 * Date: 2/21/12
 * Time: 9:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Category(IntegrationTest.class)
public class PodcasterTest extends BaseTest {
    private static final Podcast podcastJavaPosse = new Podcast("", "The Java Posse");

    static {
        podcastJavaPosse.setCount(0);
        podcastJavaPosse.setLength(0);
        podcastJavaPosse.setRating(0);
        podcastJavaPosse.setSqueezeType(XMLPluginItem.SQUEEZE_TYPE.PODCAST);
        podcastJavaPosse.setTitle("The Java Posse");
        podcastJavaPosse.setTrack(0);
        podcastJavaPosse.setUrl("http://feeds.feedburner.com/javaposse");
        podcastJavaPosse.setYear(null);
        podcastJavaPosse.setAudio(false);
        podcastJavaPosse.setContainsItems(true);
        podcastJavaPosse.setRemote(false);
        podcastJavaPosse.setError(false);
    }

    @Test
    public void testGetPodcasts() throws NetworkException {
        List<Podcast> podcasts = new ArrayList<Podcast>(getPodcaster().getAllPodcasts());
        boolean contains = false;
        Podcast podcast = null;
        for (Podcast pod : podcasts) {
            if (pod.getName().equals(podcastJavaPosse.getName())) {
                contains = true;
                podcast = pod;
            }
        }
        Assert.assertTrue(contains);

        Assert.assertEquals(podcast.getName(), podcastJavaPosse.getName());
        Assert.assertEquals(podcast.getCount(), podcastJavaPosse.getCount());
        Assert.assertEquals(podcast.getImageUrl(), podcastJavaPosse.getImageUrl());
        Assert.assertEquals(podcast.getLength(), podcastJavaPosse.getLength());
        Assert.assertEquals(podcast.getRating(), podcastJavaPosse.getRating());
        Assert.assertEquals(podcast.getSmallIconURL(), podcastJavaPosse.getSmallIconURL());
        Assert.assertEquals(podcast.getSqueezeType(), podcastJavaPosse.getSqueezeType());
        Assert.assertEquals(podcast.getTitle(), podcastJavaPosse.getTitle());
        Assert.assertEquals(podcast.getTrack(), podcastJavaPosse.getTrack());
        Assert.assertEquals(podcast.getUrl(), podcastJavaPosse.getUrl());
        Assert.assertEquals(podcast.getYear(), podcastJavaPosse.getYear());
        Assert.assertEquals(podcast.isAudio(), podcastJavaPosse.isAudio());
        Assert.assertEquals(podcast.isContainsItems(), podcastJavaPosse.isContainsItems());
        Assert.assertEquals(podcast.isRemote(), podcastJavaPosse.isRemote());
        Assert.assertEquals(podcast.isError(), podcastJavaPosse.isError());
    }

    @Test
    public void testPodcastAudioDetails() throws NetworkException {
        List<Podcast> podcasts = new ArrayList<Podcast>(getPodcaster().getAllPodcasts());
        Podcast podcast = null;
        for (Podcast pod : podcasts) {
            if (pod.getName().equals(podcastJavaPosse.getName())) {
                podcast = pod;
            }
        }

        PodcastAudioDetails audioDetails = getPodcaster().getPodcastAudioDetails(podcast);


    }

    @Test
    public void testGetPodcast() throws NetworkException {
        List<Podcast> podcasts = new ArrayList<Podcast>(getPodcaster().getAllPodcasts());
        Podcast podcast = null;
        for (Podcast pod : podcasts) {
            if (pod.getName().equals(podcastJavaPosse.getName())) {
                podcast = pod;
            }
        }

        getPodcaster().getPodcasts(podcast);
    }
}
