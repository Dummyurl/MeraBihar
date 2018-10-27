package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 03-09-2018.
 */

public class ItineraryDescription implements Serializable {

    @SerializedName("ItineraryDescriptionId")
    private int ItineraryDescriptionId;

    @SerializedName("Description")
    private String Description;

    @SerializedName("Time")
    private String Time;

    @SerializedName("itinerary")
    private Itinerary itinerary;

    @SerializedName("ItineraryId")
    private int ItineraryId;

    public int getItineraryDescriptionId() {
        return ItineraryDescriptionId;
    }

    public void setItineraryDescriptionId(int itineraryDescriptionId) {
        ItineraryDescriptionId = itineraryDescriptionId;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public Itinerary getItinerary() {
        return itinerary;
    }

    public void setItinerary(Itinerary itinerary) {
        this.itinerary = itinerary;
    }

    public int getItineraryId() {
        return ItineraryId;
    }

    public void setItineraryId(int itineraryId) {
        ItineraryId = itineraryId;
    }
}
