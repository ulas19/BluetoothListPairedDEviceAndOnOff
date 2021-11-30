package com.ulas.testproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.ulas.testproject.databinding.ActivityMainBinding;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding ;
    BluetoothAdapter myBluetoothAdapter ;
    Intent btEnablingIntent ;
    int requestCodeForEnable = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        btEnablingIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothOnMethod();
        bluetoothOffMethod();
        exeButton();
    }

    private void exeButton() {
        binding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Set<BluetoothDevice> bt = myBluetoothAdapter.getBondedDevices();
                String[] strings = new String[bt.size()];
                int index = 0;

                if (bt.size()>0){
                    for (BluetoothDevice device :bt){
                        strings[index] = device.getName();
                        index ++ ;
                    }
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,strings);
                    binding.listView.setAdapter(arrayAdapter);
                }
            }
        });
    }

    private void bluetoothOffMethod() {
        binding.btnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBluetoothAdapter.isEnabled()){
                    myBluetoothAdapter.disable();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == requestCodeForEnable){
            if (resultCode == RESULT_OK){
                Toast.makeText(getApplicationContext(), "Bluetooth is enabled...", Toast.LENGTH_SHORT).show();
            }
            else if (resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "Bluetooth Enabling is cancelled...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void bluetoothOnMethod() {
        binding.btnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myBluetoothAdapter == null){
                    Toast.makeText(getApplicationContext(), "Bluetooth does not suport on this Device", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (!myBluetoothAdapter.isEnabled()){
                        startActivityForResult(btEnablingIntent,requestCodeForEnable);
                    }
                }
            }
        });
    }
}