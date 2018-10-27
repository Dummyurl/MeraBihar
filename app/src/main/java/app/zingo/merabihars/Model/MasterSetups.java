package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 17-09-2018.
 */

public class MasterSetups implements Serializable {

    @SerializedName("MasterSetupId")
    private int MasterSetupId;

    @SerializedName("UniqueId")
    private String UniqueId;

    @SerializedName("Title")
    private String Title;

    @SerializedName("SubTitle")
    private String SubTitle;

    @SerializedName("WelcomeMessageTitle")
    private String WelcomeMessageTitle;

    @SerializedName("WelcomeMessageDescription")
    private String WelcomeMessageDescription;

    @SerializedName("URL")
    private String URL;

    @SerializedName("masterImagesList")
    private ArrayList<MasterImages> masterImagesList;

    public int getMasterSetupId() {
        return MasterSetupId;
    }

    public void setMasterSetupId(int masterSetupId) {
        MasterSetupId = masterSetupId;
    }

    public String getUniqueId() {
        return UniqueId;
    }

    public void setUniqueId(String uniqueId) {
        UniqueId = uniqueId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTitle() {
        return SubTitle;
    }

    public void setSubTitle(String subTitle) {
        SubTitle = subTitle;
    }

    public String getWelcomeMessageTitle() {
        return WelcomeMessageTitle;
    }

    public void setWelcomeMessageTitle(String welcomeMessageTitle) {
        WelcomeMessageTitle = welcomeMessageTitle;
    }

    public String getWelcomeMessageDescription() {
        return WelcomeMessageDescription;
    }

    public void setWelcomeMessageDescription(String welcomeMessageDescription) {
        WelcomeMessageDescription = welcomeMessageDescription;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public ArrayList<MasterImages> getMasterImagesList() {
        return masterImagesList;
    }

    public void setMasterImagesList(ArrayList<MasterImages> masterImagesList) {
        this.masterImagesList = masterImagesList;
    }
}
