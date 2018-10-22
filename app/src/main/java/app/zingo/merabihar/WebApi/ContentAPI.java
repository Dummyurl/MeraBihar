package app.zingo.merabihar.WebApi;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.Contents;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 16-10-2018.
 */

public interface ContentAPI {

    @GET("Contents/GetContentByCityId/{CityId}")
    Call<ArrayList<Contents>> getContentsByCityId(@Path("CityId") int id);

    @GET("Contents/GetContentByCategoryId/{CategoryId}")
    Call<ArrayList<Contents>> getContentsByCatId(@Path("CategoryId") int id);

    @GET("Contents/GetFavouriteContentByProfileId/{ProfileId}")
    Call<ArrayList<Contents>> getFavContentsByProfileId(@Path("ProfileId") int id);
}
