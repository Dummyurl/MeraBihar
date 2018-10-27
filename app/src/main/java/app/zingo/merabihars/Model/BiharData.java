package app.zingo.merabihars.Model;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class BiharData {

    String title;
    String desc;
    int imageId;

    public BiharData(String title,String desc, int imageId)
    {
        this.title = title;
        this.desc = desc;
        this.imageId = imageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
