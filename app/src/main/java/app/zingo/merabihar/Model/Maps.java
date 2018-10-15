package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class Maps implements Serializable {

    @SerializedName("MapId")
    private int MapId;
    @SerializedName("Logitude")
    private String Logitude;
    @SerializedName("Latitude")
    private String Latitude;
    @SerializedName("Location")
    private String Location;
    @SerializedName("activities")
    private ActivityModel activities;
    @SerializedName("ActivitiesId")
    private int ActivitiesId;

    public void setActivities(ActivityModel activities) {
        this.activities = activities;
    }

    public void setActivitiesId(int activitiesId) {
        ActivitiesId = activitiesId;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public void setLogitude(String logitude) {
        Logitude = logitude;
    }

    public void setMapId(int mapId) {
        MapId = mapId;
    }

    public ActivityModel getActivities() {
        return activities;
    }

    public int getActivitiesId() {
        return ActivitiesId;
    }

    public int getMapId() {
        return MapId;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLocation() {
        return Location;
    }

    public String getLogitude() {
        return Logitude;
    }


}
