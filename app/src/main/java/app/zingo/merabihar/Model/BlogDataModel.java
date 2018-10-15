package app.zingo.merabihar.Model;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class BlogDataModel implements Serializable {

    String blogName;
    String blogDescription;
    int blogImageId;
    String interest;

    public String getBlogName() {
        return blogName;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public int getBlogImageId() {
        return blogImageId;
    }

    public void setBlogImageId(int blogImageId) {
        this.blogImageId = blogImageId;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }
}
