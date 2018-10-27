package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 26-10-2018.
 */

public class VideoTrackers implements Serializable {

    @SerializedName("TrackerId")
    public int TrackerId;

    @SerializedName("VideoReferralCodeUsedToWatch")
    public String VideoReferralCodeUsedToWatch;

    @SerializedName("Profiles")
    public UserProfile Profiles;

    @SerializedName("ProfileId")
    public int ProfileId;

    @SerializedName("WatchedMinute")
    public String WatchedMinute;

    @SerializedName("Status")
    public String Status;

    @SerializedName("content")
    public Contents content;

    @SerializedName("ContentId")
    public int ContentId;


    public int getTrackerId() {
        return TrackerId;
    }

    public void setTrackerId(int trackerId) {
        TrackerId = trackerId;
    }

    public String getVideoReferralCodeUsedToWatch() {
        return VideoReferralCodeUsedToWatch;
    }

    public void setVideoReferralCodeUsedToWatch(String videoReferralCodeUsedToWatch) {
        VideoReferralCodeUsedToWatch = videoReferralCodeUsedToWatch;
    }

    public UserProfile getProfiles() {
        return Profiles;
    }

    public void setProfiles(UserProfile profiles) {
        Profiles = profiles;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getWatchedMinute() {
        return WatchedMinute;
    }

    public void setWatchedMinute(String watchedMinute) {
        WatchedMinute = watchedMinute;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Contents getContent() {
        return content;
    }

    public void setContent(Contents content) {
        this.content = content;
    }

    public int getContentId() {
        return ContentId;
    }

    public void setContentId(int contentId) {
        ContentId = contentId;
    }
}
