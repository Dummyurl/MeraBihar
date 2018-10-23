package app.zingo.merabihar.WebApi;

import app.zingo.merabihar.Model.InterestContentMapping;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 23-10-2018.
 */

public interface InterestContentAPI {

    @POST("InterestContentMappings")
    Call<InterestContentMapping> postInterestContent(@Body InterestContentMapping body);
}
