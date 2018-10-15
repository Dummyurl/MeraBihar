package app.zingo.merabihar.Model;

/**
 * Created by ZingoHotels Tech on 30-08-2018.
 */

public class NavBarItems {
    private String title;
    private int icon;

    public NavBarItems(String title, int icon)
    {
        this.title = title;
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }
}