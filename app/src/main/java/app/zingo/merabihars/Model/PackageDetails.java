package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class PackageDetails implements Serializable {
    @SerializedName("PackageDetailsId")
    private int PackageDetailsId;

    @SerializedName("Name")
    private String  Name;

    @SerializedName("Description")
    private String Description;

    @SerializedName("TimeSlot")
    private String TimeSlot;

    @SerializedName("DeclaredRateForChild")
    private int DeclaredRateForChild;

    @SerializedName("SellRateForChild")
    private int SellRateForChild;

    @SerializedName("DeclaredRate")
    private int DeclaredRate;

    @SerializedName("SellRate")
    private int SellRate;

    @SerializedName("ExtraCharges")
    private int ExtraCharges;

    @SerializedName("Discount")
    private int Discount;

    @SerializedName("DiscountAmount")
    private int DiscountAmount;

    @SerializedName("TotalAmount")
    private int TotalAmount;

    @SerializedName("ActivitiesId")
    private int ActivitiesId;

    @SerializedName("activities")
    private ActivityModel activities;

    @SerializedName("GSTPercentage")
    private int GSTPercentage;

    @SerializedName("GSTValue")
    private int GSTValue;

    @SerializedName("NetRate")
    private int NetRate;

    @SerializedName("itinerary")
    private ArrayList<Itinerary> itinerary;

    public int getPackageDetailsId() {
        return PackageDetailsId;
    }

    public void setPackageDetailsId(int packageDetailsId) {
        PackageDetailsId = packageDetailsId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getTimeSlot() {
        return TimeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        TimeSlot = timeSlot;
    }

    public int getDeclaredRateForChild() {
        return DeclaredRateForChild;
    }

    public void setDeclaredRateForChild(int declaredRateForChild) {
        DeclaredRateForChild = declaredRateForChild;
    }

    public int getSellRateForChild() {
        return SellRateForChild;
    }

    public void setSellRateForChild(int sellRateForChild) {
        SellRateForChild = sellRateForChild;
    }

    public int getDeclaredRate() {
        return DeclaredRate;
    }

    public void setDeclaredRate(int declaredRate) {
        DeclaredRate = declaredRate;
    }

    public int getSellRate() {
        return SellRate;
    }

    public void setSellRate(int sellRate) {
        SellRate = sellRate;
    }

    public int getExtraCharges() {
        return ExtraCharges;
    }

    public void setExtraCharges(int extraCharges) {
        ExtraCharges = extraCharges;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public int getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(int discountAmount) {
        DiscountAmount = discountAmount;
    }

    public int getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getActivitiesId() {
        return ActivitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        ActivitiesId = activitiesId;
    }

    public ActivityModel getActivities() {
        return activities;
    }

    public void setActivities(ActivityModel activities) {
        this.activities = activities;
    }

    public int getGSTPercentage() {
        return GSTPercentage;
    }

    public void setGSTPercentage(int GSTPercentage) {
        this.GSTPercentage = GSTPercentage;
    }

    public int getGSTValue() {
        return GSTValue;
    }

    public void setGSTValue(int GSTValue) {
        this.GSTValue = GSTValue;
    }

    public int getNetRate() {
        return NetRate;
    }

    public void setNetRate(int netRate) {
        NetRate = netRate;
    }

    public ArrayList<Itinerary> getItinerary() {
        return itinerary;
    }

    public void setItinerary(ArrayList<Itinerary> itinerary) {
        this.itinerary = itinerary;
    }
}

