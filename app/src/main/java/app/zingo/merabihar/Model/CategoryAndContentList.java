package app.zingo.merabihar.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ZingoHotels Tech on 22-10-2018.
 */

public class CategoryAndContentList implements Serializable {

    @SerializedName("categories")
    public Category categories;

    @SerializedName("contentList")
    public ArrayList<Contents> contentList;


    public Category getCategories() {
        return categories;
    }

    public void setCategories(Category categories) {
        this.categories = categories;
    }

    public ArrayList<Contents> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<Contents> contentList) {
        this.contentList = contentList;
    }
}
