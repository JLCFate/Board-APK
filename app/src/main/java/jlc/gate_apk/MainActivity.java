package jlc.gate_apk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkMAC();
    }

    public void checkMAC() {
        String macAddress = GetMAC.getMac();
        try {
            Call<Object> call = RetroClient.getInstance().getMyApi().isUserAuthorized(macAddress.toLowerCase());
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    Log.d("Code", String.valueOf(response.code()));
                    Intent act = new Intent(MainActivity.this, (response.code() == 200) ? ControlActivity.class:UnauthorizedActivity.class);
                    MainActivity.this.startActivity(act);
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            Log.d("Error", e.getMessage());
        }
    }
}