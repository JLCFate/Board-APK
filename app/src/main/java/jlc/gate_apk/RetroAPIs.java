package jlc.gate_apk;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetroAPIs {
    String BASE_URL = "http://192.168.0.107:3000";

    @GET("users/{address}")
    Call<Object> isUserAuthorized(@Path("address") String address);

}
