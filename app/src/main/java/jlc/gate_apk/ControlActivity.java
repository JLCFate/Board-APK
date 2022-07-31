package jlc.gate_apk;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;
import java.util.Collections;

public class ControlActivity extends AppCompatActivity {

    private Socket socket;
    private String macValue;

    {
        try {
            macValue = GetMAC.getMac().toLowerCase();
            IO.Options options = IO.Options.builder()
                    .setExtraHeaders(Collections.singletonMap("X-Address", Collections.singletonList(macValue)))
                    .build();
            socket = IO.socket("http://192.168.0.107:4001", options);

        }catch(URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
        Button frontGate = findViewById(R.id.Front_gate);
        Button backGate = findViewById(R.id.Back_gate);
        frontGate.setOnClickListener(gateOnClick);
        backGate.setOnClickListener(gateOnClick);
        socket.on("failed", onFailure);
        socket.connect();
    }

    private final Emitter.Listener onFailure = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            socket.disconnect();
        }
    };

    @SuppressLint("NonConstantResourceId")
    private final OnClickListener gateOnClick = view -> {
        String gate = "";
        switch(view.getId()) {
            case R.id.Front_gate:
                gate = "front";
                break;
            case R.id.Back_gate:
                gate = "back";
                break;
        }
        socket.emit("open", "{\"user_mac\": \""+macValue+"\", \"gate\": \""+gate+"\"}");
    };
}