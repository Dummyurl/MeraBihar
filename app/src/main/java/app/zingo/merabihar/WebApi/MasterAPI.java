package app.zingo.merabihar.WebApi;

import java.util.ArrayList;

import app.zingo.merabihar.Model.Category;
import app.zingo.merabihar.Model.MasterSetups;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 17-09-2018.
 */

public interface MasterAPI {

    @POST("MasterSetups")
    Call<MasterSetups> createContent(@Body MasterSetups body);

    @GET("MasterSetup/GetmasterSetupByUniqueId/{UniqueId}")
    Call<ArrayList<MasterSetups>> getContentByUniqueId(@Path("UniqueId") String id);
}
