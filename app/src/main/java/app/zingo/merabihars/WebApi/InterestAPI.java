package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Interest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by ZingoHotels Tech on 03-10-2018.
 */

public interface InterestAPI {

    @POST("interest")
    Call<Interest> postInterest(@Body Interest body);

    @DELETE("interest/{id}")
    Call<Interest> deleteIntrs(@Path("id") int id);

    @GET("interest")
    Call<ArrayList<Interest>> getInterest();

    @GET("interest/{id}")
    Call<Interest> getInterestById(@Path("id") int id);
}
