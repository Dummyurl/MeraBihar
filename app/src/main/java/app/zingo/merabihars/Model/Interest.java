package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 03-10-2018.
 */

public class Interest implements Serializable {

    @SerializedName("ZingoInterestId")
    private int ZingoInterestId;
    @SerializedName("InterestName")
    private String InterestName;
    @SerializedName("Description")
    private String Description;

    public int getZingoInterestId() {
        return ZingoInterestId;
    }

    public void setZingoInterestId(int zingoInterestId) {
        ZingoInterestId = zingoInterestId;
    }

    public String getInterestName() {
        return InterestName;
    }

    public void setInterestName(String interestName) {
        InterestName = interestName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public boolean equals(Object anObject) {
        if (!(anObject instanceof Interest)) {
            return false;
        }
        Interest otherMember = (Interest)anObject;
        return otherMember.getZingoInterestId()==(getZingoInterestId());
    }
}
