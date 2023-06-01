/*import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.miraclepack.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothConnection extends AppCompatActivity {

    private static final String TAG = "BluetoothConnection";

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private InputStream inputStream;
    private OutputStream outputStream;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String DEVICE_ADDRESS = "00:00:00:00:00:00"; // Replace with HC-05 address

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        Button connectButton = findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectToBluetooth();
            }
        });

        Button sendButton = findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData("Hello from Android!");
            }
        });
    }

    private void connectToBluetooth() {
        bluetoothDevice = bluetoothAdapter.getRemoteDevice(DEVICE_ADDRESS);
        ConnectBluetoothTask connectTask = new ConnectBluetoothTask();
        connectTask.execute();
    }

    private void sendData(String data) {
        try {
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "Error while sending data: " + e.getMessage());
        }
    }

    private class ConnectBluetoothTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                inputStream = bluetoothSocket.getInputStream();
                outputStream = bluetoothSocket.getOutputStream();
                Log.d(TAG, "Bluetooth connection established.");
            } catch (IOException e) {
                Log.e(TAG, "Error while establishing Bluetooth connection: " + e.getMessage());
            }
            return null;
        }
    }
}*/
