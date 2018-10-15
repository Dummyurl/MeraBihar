package app.zingo.merabihar.CustomInterface;

import java.util.Comparator;

import app.zingo.merabihar.Model.PackageDetails;

/**
 * Created by ZingoHotels Tech on 01-09-2018.
 */

public class SortPackageDetails implements Comparator<PackageDetails>
{

    @Override
    public int compare(PackageDetails o1, PackageDetails o2) {
        if(o1.getSellRate() > o2.getSellRate())
        {
            return 1;
        }
        else if(o1.getSellRate() == o2.getSellRate())
        {
            return 0;
        }
        else
        {
            return -1;
        }
    }
}