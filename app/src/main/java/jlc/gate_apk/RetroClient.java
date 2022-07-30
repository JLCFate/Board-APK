package jlc.gate_apk;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static RetroClient retroClient = null;
    private static RetroAPIs apis;

    private RetroClient(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RetroAPIs.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apis = retrofit.create(RetroAPIs.class);
    }

    public static synchronized RetroClient getInstance(){
        if(retroClient == null) {
            retroClient = new RetroClient();
        }
        return retroClient;
    }

    public RetroAPIs getMyApi() {
        return apis;
    }

}
