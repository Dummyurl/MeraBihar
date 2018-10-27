package app.zingo.merabihars.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public class BlogImages  implements Serializable {

    @SerializedName("BlogImagesId")
    private int BlogImagesId;

    @SerializedName("blogs")
    private Blogs blogs;

    @SerializedName("BlogId")
    private int BlogId;

    @SerializedName("Image")
    private String Image;

    public int getBlogImagesId() {
        return BlogImagesId;
    }

    public void setBlogImagesId(int blogImagesId) {
        BlogImagesId = blogImagesId;
    }

    public Blogs getBlogs() {
        return blogs;
    }

    public void setBlogs(Blogs blogs) {
        this.blogs = blogs;
    }

    public int getBlogId() {
        return BlogId;
    }

    public void setBlogId(int blogId) {
        BlogId = blogId;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
