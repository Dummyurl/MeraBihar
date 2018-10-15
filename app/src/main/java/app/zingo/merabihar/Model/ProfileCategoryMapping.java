package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 04-10-2018.
 */

public class ProfileCategoryMapping implements Serializable {

    @SerializedName("MappingId")
    private int MappingId	;

    @SerializedName("ProfileId")
    private int ProfileId;

    @SerializedName("CategoryId")
    private int CategoryId;

    public int getMappingId() {
        return MappingId;
    }

    public void setMappingId(int mappingId) {
        MappingId = mappingId;
    }

    public int getProfileId() {
        return ProfileId;
    }

    public void setProfileId(int profileId) {
        ProfileId = profileId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }
}
