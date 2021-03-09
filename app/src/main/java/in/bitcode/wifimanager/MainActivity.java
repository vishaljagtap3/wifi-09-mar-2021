package in.bitcode.wifimanager;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;

    BroadcastReceiver brWifi = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            List<ScanResult> results = wifiManager.getScanResults();
            for(ScanResult sr : results) {
                mt(sr.SSID + " " + sr.BSSID);
            }
        }
    };


    @SuppressLint({"WifiManagerLeak", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        @SuppressLint("MissingPermission")
        List<WifiConfiguration> networks = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration config : networks) {
            mt(config.networkId+"");
            mt(config.SSID);
            mt(config.BSSID);
            mt("Provider friendly name: " + config.providerFriendlyName);
            mt("Passwords: ");
            for(String pass : config.wepKeys) {
                mt(pass);
            }
        }

        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        int state = wifiManager.getWifiState();
        wifiManager.enableNetwork(1, true);
        wifiManager.disableNetwork(2);
        wifiManager.disconnect();


        //wifiManager.removeNetwork(wifiInfo.getNetworkId());

        WifiConfiguration wifiConfiguration = new WifiConfiguration();
        wifiConfiguration.SSID = "";
        wifiConfiguration.BSSID = "";
        wifiConfiguration.wepKeys = new String[] { "pass" };
        int networkId = wifiManager.addNetwork(wifiConfiguration);


        registerReceiver(
                brWifi,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        );

        wifiManager.startScan();

    }

    private void mt(String text) {
        Log.e("tag", text);

    }
}