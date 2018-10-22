package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by ZingoHotels Tech on 12-09-2018.
 */

public class UserProfile  implements Serializable {

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("Prefix")
    private String Prefix;

    @SerializedName("FullName")
    private String FullName;

    @SerializedName("Password")
    private String Password;

    @SerializedName("Gender")
    private String Gender;

    @SerializedName("Email")
    private String Email;

    @SerializedName("PhoneNumber")
    private String PhoneNumber;

    @SerializedName("UserRoleId")
    private int UserRoleId;

    @SerializedName("UserRoles")
    private UserRole UserRoles;

    @SerializedName("ProfilePhoto")
    private String ProfilePhoto;

    @SerializedName("blogList")
    private ArrayList<Blogs> blogList;

    @SerializedName("AuthType")
    private String AuthType;

    @SerializedName("AuthId")
    private String AuthId;

    @SerializedName("SignUpDate")
    private String SignUpDate;

    @SerializedName("Status")
    private String Status;

    @SerializedName("Views")
    private String Views;

    @SerializedName("WatchTime")
    private String WatchTime;

    @SerializedName("fitnessTracker")
    private String fitnessTracker;

    @SerializedName("contents")
    private ArrayList<Contents> contents;

    @SerializedName("comment")
    private ArrayList<Comments> comment;

    @SerializedName("ReferralCodeUsed")
    private String ReferralCodeUsed;

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public String getPrefix() {
        return Prefix;
    }

    public void setPrefix(String prefix) {
        Prefix = prefix;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public int getUserRoleId() {
        return UserRoleId;
    }

    public void setUserRoleId(int userRoleId) {
        UserRoleId = userRoleId;
    }

    public UserRole getUserRoles() {
        return UserRoles;
    }

    public void setUserRoles(UserRole userRoles) {
        UserRoles = userRoles;
    }

    public String getProfilePhoto() {
        return ProfilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        ProfilePhoto = profilePhoto;
    }

    public String getAuthType() {
        return AuthType;
    }

    public void setAuthType(String authType) {
        AuthType = authType;
    }

    public String getAuthId() {
        return AuthId;
    }

    public void setAuthId(String authId) {
        AuthId = authId;
    }

    public String getSignUpDate() {
        return SignUpDate;
    }

    public void setSignUpDate(String signUpDate) {
        SignUpDate = signUpDate;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public ArrayList<Blogs> getBlogList() {
        return blogList;
    }

    public void setBlogList(ArrayList<Blogs> blogList) {
        this.blogList = blogList;
    }

    public String getViews() {
        return Views;
    }

    public void setViews(String views) {
        Views = views;
    }

    public String getWatchTime() {
        return WatchTime;
    }

    public void setWatchTime(String watchTime) {
        WatchTime = watchTime;
    }

    public String getFitnessTracker() {
        return fitnessTracker;
    }

    public void setFitnessTracker(String fitnessTracker) {
        this.fitnessTracker = fitnessTracker;
    }

    public ArrayList<Contents> getContents() {
        return contents;
    }

    public void setContents(ArrayList<Contents> contents) {
        this.contents = contents;
    }

    public ArrayList<Comments> getComment() {
        return comment;
    }

    public void setComment(ArrayList<Comments> comment) {
        this.comment = comment;
    }

    public String getReferralCodeUsed() {
        return ReferralCodeUsed;
    }

    public void setReferralCodeUsed(String referralCodeUsed) {
        ReferralCodeUsed = referralCodeUsed;
    }


    public static Comparator compareProfile = new Comparator() {
        @Override
        public int compare(Object o, Object t1) {
            UserProfile profile = (UserProfile) o;
            UserProfile profile1 = (UserProfile) t1;
            return profile.getFullName().compareTo(profile1.getFullName());
        }
    };

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof UserProfile)) {
            return false;
        }
        UserProfile otherMember = (UserProfile)anObject;
        return otherMember.getProfileId()==(getProfileId());
    }
}
