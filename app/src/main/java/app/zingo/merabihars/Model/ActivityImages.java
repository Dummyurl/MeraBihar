package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class ActivityImages implements Serializable {
    @SerializedName("ActivityImagesId")
    private int ActivityImagesId;
    @SerializedName("Images")
    private String Images;
    @SerializedName("activities")
    private ActivityModel activities;
    @SerializedName("ActivitiesId")
    private int ActivitiesId;

    public int getActivityImagesId() {
        return ActivityImagesId;
    }

    public void setActivityImagesId(int activityImagesId) {
        ActivityImagesId = activityImagesId;
    }

    public String getImages() {
        return Images;
    }

    public void setImages(String images) {
        Images = images;
    }

    public ActivityModel getActivities() {
        return activities;
    }

    public void setActivities(ActivityModel activities) {
        this.activities = activities;
    }

    public int getActivitiesId() {
        return ActivitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        ActivitiesId = activitiesId;
    }
}
