package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 03-09-2018.
 */

public class Itinerary implements Serializable {

    @SerializedName("ItineraryId")
    private int ItineraryId;

    @SerializedName("Logitude")
    private String Logitude;

    @SerializedName("Latitude")
    private String Latitude;

    @SerializedName("Location")
    private String Location;

    @SerializedName("packageDetail")
    private PackageDetails packageDetail;

    @SerializedName("PackageDetailsId")
    private int PackageDetailsId;

    @SerializedName("itineraryDesc")
    private ArrayList<ItineraryDescription> itineraryDesc;

    public int getItineraryId() {
        return ItineraryId;
    }

    public void setItineraryId(int itineraryId) {
        ItineraryId = itineraryId;
    }

    public String getLogitude() {
        return Logitude;
    }

    public void setLogitude(String logitude) {
        Logitude = logitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public PackageDetails getPackageDetail() {
        return packageDetail;
    }

    public void setPackageDetail(PackageDetails packageDetail) {
        this.packageDetail = packageDetail;
    }

    public int getPackageDetailsId() {
        return PackageDetailsId;
    }

    public void setPackageDetailsId(int packageDetailsId) {
        PackageDetailsId = packageDetailsId;
    }

    public ArrayList<ItineraryDescription> getItineraryDesc() {
        return itineraryDesc;
    }

    public void setItineraryDesc(ArrayList<ItineraryDescription> itineraryDesc) {
        this.itineraryDesc = itineraryDesc;
    }
}
