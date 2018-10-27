package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public class Blogs implements Serializable {

    @SerializedName("BlogId")
    private int BlogId;

    @SerializedName("Title")
    private String Title;

    @SerializedName("ShortDesc")
    private String ShortDesc;

    @SerializedName("LongDesc")
    private String LongDesc;

    @SerializedName("CreateDate")
    private String CreateDate;

    @SerializedName("CreatedUser")
    private String CreatedUser;

    @SerializedName("UpdateDate")
    private String UpdateDate;

    @SerializedName("UpdatedUser")
    private String UpdatedUser;

    @SerializedName("ActivitiesId")
    private int ActivitiesId;

    @SerializedName("activities")
    private ActivityModel activities;

    @SerializedName("blogImages")
    private ArrayList<BlogImages> blogImages;

    @SerializedName("IsApproved")
    private boolean IsApproved;

    @SerializedName("profile")
    private UserProfile profile;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("Status")
    private String Status;

    public int getBlogId() {
        return BlogId;
    }

    public void setBlogId(int blogId) {
        BlogId = blogId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public void setShortDesc(String shortDesc) {
        ShortDesc = shortDesc;
    }

    public String getLongDesc() {
        return LongDesc;
    }

    public void setLongDesc(String longDesc) {
        LongDesc = longDesc;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getCreatedUser() {
        return CreatedUser;
    }

    public void setCreatedUser(String createdUser) {
        CreatedUser = createdUser;
    }

    public String getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(String updateDate) {
        UpdateDate = updateDate;
    }

    public String getUpdatedUser() {
        return UpdatedUser;
    }

    public void setUpdatedUser(String updatedUser) {
        UpdatedUser = updatedUser;
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

    public ArrayList<BlogImages> getBlogImages() {
        return blogImages;
    }

    public void setBlogImages(ArrayList<BlogImages> blogImages) {
        this.blogImages = blogImages;
    }

    public boolean isApproved() {
        return IsApproved;
    }

    public void setApproved(boolean approved) {
        IsApproved = approved;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
