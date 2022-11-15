package com.example.gps;



import android.os.Bundle;
import android.net.wifi.WifiManager;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import android.net.wifi.ScanResult;

public class WifiScanner extends AppCompatActivity{
    private WifiManager wifiManager;
    private List<ScanResult> results;
}
