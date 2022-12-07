package com.example.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class WifiScan extends AppCompatActivity implements LocationListener {
    private WifiManager wifiManager;
    private ListView listView;
    private Button buttonScan;
    private int size = 0;
    private List<ScanResult> results;
    private ArrayList<String> arrayList = new ArrayList<>();
    private ArrayAdapter adapter;
    private double latitude, longitude;
    private LocationManager locationManager;

    //Activity scanning wifi AP and saving data in database
    // to access data from database:
    // DataBaseHelper dataBaseHelper = new DataBaseHelper(currentactivityclass.this);
    // List<LocationData> allDAta = dataBaseHelper.selectAll();
    // allDAta -> list of all of the saved elements in the database
    // details about the single element can be seen in LocationData class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_scan);
        buttonScan = findViewById(R.id.scanButton);
        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanWifi();
            }
        });
        listView = findViewById(R.id.wifilist);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()){
            Toast.makeText(this,"Wifi is disabled. We need to enable it", Toast.LENGTH_LONG).show();
            wifiManager.setWifiEnabled(true);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(WifiScan.this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(WifiScan.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
        //scanning location
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,this);
        scanWifi();
    }

    private void scanWifi(){
        arrayList.clear();
        registerReceiver(wifiReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();
        Toast.makeText(this,"Scanning WiFi...", Toast.LENGTH_SHORT).show();
    }

    //When receiving scanning results AP BSSID and current: latitude, longitude to the database
    BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //scanning results
            results = wifiManager.getScanResults();
            unregisterReceiver(this);

            for (ScanResult scanResult:results){
                arrayList.add(scanResult.SSID+"-"+scanResult.capabilities);
                adapter.notifyDataSetChanged();
            }
            //creating object to save in database
            ScanResult res = results.get(0);
            LocationData loc_data = new LocationData(latitude,longitude,res.BSSID);
            DataBaseHelper dataBaseHelper = new DataBaseHelper(WifiScan.this);
            dataBaseHelper.addOne(loc_data);
        }
    };

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        locationManager.removeUpdates(this);

    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}