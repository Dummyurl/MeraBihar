package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 17-09-2018.
 */

public class MasterImages implements Serializable {

    @SerializedName("MasterImagesId")
    private int MasterImagesId;

    @SerializedName("Title")
    private String Title;

    @SerializedName("Image")
    private String Image;

    public int getMasterImagesId() {
        return MasterImagesId;
    }

    public void setMasterImagesId(int masterImagesId) {
        MasterImagesId = masterImagesId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
