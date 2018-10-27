package app.zingo.merabihars.WebApi;

import java.util.ArrayList;

import app.zingo.merabihars.Model.Itinerary;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ItineraryAPI {

    @GET("Itineraries/{id}")
    Call<Itinerary> getItineraryById(@Path("id") int id);

    @GET("Itineraries")
    Call<ArrayList<Itinerary>> getItineraries();
}
