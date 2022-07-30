package jlc.gate_apk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.*;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkMAC();
    }

    public void checkMAC() {
        String macAddress = getMac();
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

    public String getMac() {
        try{
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            StringBuilder stringMac = new StringBuilder();
            for(NetworkInterface networkInterface : networkInterfaceList)
            {
                if(networkInterface.getName().equalsIgnoreCase("wlan0"))
                {
                    for(int i = 0 ;i <networkInterface.getHardwareAddress().length; i++){
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i]& 0xFF);
                        if(stringMacByte.length() == 1) stringMacByte = "0" + stringMacByte;
                        stringMac.append(stringMacByte.toUpperCase()).append(":");
                    }
                    break;
                }
            }
            stringMac.deleteCharAt(stringMac.length()-1);
            return stringMac.toString();
        }catch (SocketException e)
        {
            e.printStackTrace();
        }
        return  "0";
    }
}