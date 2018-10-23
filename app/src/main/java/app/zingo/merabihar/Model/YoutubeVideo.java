package app.zingo.merabihar.Model;

/**
 * Created by ZingoHotels Tech on 23-10-2018.
 */

public class YoutubeVideo {

    String videoUrl;

    public YoutubeVideo() {

    }

    public YoutubeVideo(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
