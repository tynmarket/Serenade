package com.tynmarket.serenade.model.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tynmarket on 2018/02/06.
 */

public interface SpeakerDeckApi {
    @GET("{user}/{slide}")
    Call<ResponseBody> slideHtml(@Path("user") String user, @Path("slide") String slide);
}
