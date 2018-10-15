package app.zingo.merabihar.WebApi;

import app.zingo.merabihar.Model.PackageDetails;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 04-09-2018.
 */

public interface PackageDetailsAPI {

    @GET("PackageDetails/{id}")
    Call<PackageDetails> getPackageById(@Path("id") int id);
}
