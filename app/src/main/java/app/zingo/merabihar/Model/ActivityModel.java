package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 29-08-2018.
 */

public class ActivityModel implements Serializable {

    @SerializedName("ActivitiesId")
    private int ActivitiesId;

    @SerializedName("ActivityName")
    private String ActivityName;

    @SerializedName("Description")
    private String Description;

    @SerializedName("subCategories")
    private SubCategories subCategories;

    @SerializedName("SubCategoriesId")
    private int SubCategoriesId;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Availability")
    private int Availability;

    @SerializedName("ValidFrom")
    private String ValidFrom;

    @SerializedName("ValidTo")
    private String ValidTo;

    @SerializedName("Review")
    private String Review;

    @SerializedName("Ratings")
    private double Ratings;

    @SerializedName("Address")
    private String Address;

    @SerializedName("OtherDetails")
    private String OtherDetails;

    @SerializedName("HighlightsOfTheActivity")
    private String HighlightsOfTheActivity;

    @SerializedName("Languagues")
    private String Languagues;

    @SerializedName("AboutTheActivity")
    private String AboutTheActivity;

    @SerializedName("WhatWeWillDo")
    private String WhatWeWillDo;

    @SerializedName("WhatIWillProvide")
    private String WhatIWillProvide;

    @SerializedName("WhoCanCome")
    private String WhoCanCome;

    @SerializedName("Notes")
    private String Notes;

    @SerializedName("Location")
    private String Location;

    @SerializedName("maps")
    private ArrayList<Maps> maps;//datatype

    @SerializedName("WhereWeWillMeet")
    private String WhereWeWillMeet;

    @SerializedName("GroupSizeALlowed")
    private String GroupSizeALlowed;

    @SerializedName("GuestRequirements")
    private String GuestRequirements;

    @SerializedName("GovernmentIDRequired")
    private String GovernmentIDRequired;

    @SerializedName("FlexibleCancellationPolicy")
    private String FlexibleCancellationPolicy;

    @SerializedName("activityImages")
    private ArrayList<ActivityImages> activityImages;//datatype

    @SerializedName("packageDetails")
    private ArrayList<PackageDetails> packageDetails;

    @SerializedName("blogsList")
    private ArrayList<Blogs> blogsList;

    @SerializedName("DisplayPrice")
    private int DisplayPrice;

    @SerializedName("SellingPrice")
    private int SellingPrice;

    @SerializedName("DiscountPercentage")
    private double DiscountPercentage;

    @SerializedName("DiscountPrice")
    private int DiscountPrice;

    @SerializedName("interests")
    private int interests;



    public ArrayList<PackageDetails> getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(ArrayList<PackageDetails> packageDetails) {
        this.packageDetails = packageDetails;
    }

    public int getActivitiesId() {
        return ActivitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        ActivitiesId = activitiesId;
    }

    public String getActivityName() {
        return ActivityName;
    }

    public void setActivityName(String activityName) {
        ActivityName = activityName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getDisplayPrice() {
        return DisplayPrice;
    }

    public void setDisplayPrice(int displayPrice) {
        DisplayPrice = displayPrice;
    }

    public int getSellingPrice() {
        return SellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        SellingPrice = sellingPrice;
    }

    public double getDiscountPercentage() {
        return DiscountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        DiscountPercentage = discountPercentage;
    }

    public int getDiscountPrice() {
        return DiscountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        DiscountPrice = discountPrice;
    }

    public SubCategories getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(SubCategories subCategories) {
        this.subCategories = subCategories;
    }

    public int getSubCategoriesId() {
        return SubCategoriesId;
    }

    public void setSubCategoriesId(int subCategoriesId) {
        SubCategoriesId = subCategoriesId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public int getAvailability() {
        return Availability;
    }

    public void setAvailability(int availability) {
        Availability = availability;
    }

    public String getValidFrom() {
        return ValidFrom;
    }

    public void setValidFrom(String validFrom) {
        ValidFrom = validFrom;
    }

    public String getValidTo() {
        return ValidTo;
    }

    public void setValidTo(String validTo) {
        ValidTo = validTo;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }

    public double getRatings() {
        return Ratings;
    }

    public void setRatings(double ratings) {
        Ratings = ratings;
    }

    public int getInterests() {
        return interests;
    }

    public void setInterests(int interests) {
        this.interests = interests;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOtherDetails() {
        return OtherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        OtherDetails = otherDetails;
    }

    public String getHighlightsOfTheActivity() {
        return HighlightsOfTheActivity;
    }

    public void setHighlightsOfTheActivity(String highlightsOfTheActivity) {
        HighlightsOfTheActivity = highlightsOfTheActivity;
    }

    public String getLanguagues() {
        return Languagues;
    }

    public void setLanguagues(String languagues) {
        Languagues = languagues;
    }

    public String getAboutTheActivity() {
        return AboutTheActivity;
    }

    public void setAboutTheActivity(String aboutTheActivity) {
        AboutTheActivity = aboutTheActivity;
    }

    public String getWhatWeWillDo() {
        return WhatWeWillDo;
    }

    public void setWhatWeWillDo(String whatWeWillDo) {
        WhatWeWillDo = whatWeWillDo;
    }

    public String getWhatIWillProvide() {
        return WhatIWillProvide;
    }

    public void setWhatIWillProvide(String whatIWillProvide) {
        WhatIWillProvide = whatIWillProvide;
    }

    public String getWhoCanCome() {
        return WhoCanCome;
    }

    public void setWhoCanCome(String whoCanCome) {
        WhoCanCome = whoCanCome;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public ArrayList<Maps> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Maps> maps) {
        this.maps = maps;
    }

    public String getWhereWeWillMeet() {
        return WhereWeWillMeet;
    }

    public void setWhereWeWillMeet(String whereWeWillMeet) {
        WhereWeWillMeet = whereWeWillMeet;
    }

    public String getGroupSizeALlowed() {
        return GroupSizeALlowed;
    }

    public void setGroupSizeALlowed(String groupSizeALlowed) {
        GroupSizeALlowed = groupSizeALlowed;
    }

    public String getGuestRequirements() {
        return GuestRequirements;
    }

    public void setGuestRequirements(String guestRequirements) {
        GuestRequirements = guestRequirements;
    }

    public String getGovernmentIDRequired() {
        return GovernmentIDRequired;
    }

    public void setGovernmentIDRequired(String governmentIDRequired) {
        GovernmentIDRequired = governmentIDRequired;
    }

    public String getFlexibleCancellationPolicy() {
        return FlexibleCancellationPolicy;
    }

    public void setFlexibleCancellationPolicy(String flexibleCancellationPolicy) {
        FlexibleCancellationPolicy = flexibleCancellationPolicy;
    }


    public ArrayList<ActivityImages> getActivityImages() {
        return activityImages;
    }

    public void setActivityImages(ArrayList<ActivityImages> activityImages) {
        this.activityImages = activityImages;
    }

    public ArrayList<Blogs> getBlogsList() {
        return blogsList;
    }

    public void setBlogsList(ArrayList<Blogs> blogsList) {
        this.blogsList = blogsList;
    }
}
