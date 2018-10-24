package app.zingo.merabihar.WebApi;

import app.zingo.merabihar.Model.Comments;
import app.zingo.merabihar.Model.Contents;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by ZingoHotels Tech on 24-10-2018.
 */

public interface CommentAPI {

    @POST("CommentsForContents")
    Call<Comments> postComment(@Body Comments body);
}
