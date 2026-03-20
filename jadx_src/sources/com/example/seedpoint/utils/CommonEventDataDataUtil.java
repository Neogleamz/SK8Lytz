package com.example.seedpoint.utils;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import com.daimajia.numberprogressbar.BuildConfig;
import com.example.seedpoint.event.Location;
import com.example.seedpoint.event.NetworkInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class CommonEventDataDataUtil {
    public static Map<String, Object> createDefaultAttributes(ConnectivityManager connectivityManager, WifiManager wifiManager) {
        String str;
        boolean z4;
        NetworkCapabilities networkCapabilities;
        HashMap hashMap = new HashMap();
        hashMap.put("time_zone", TimeZone.getDefault().getID());
        str = "none";
        if (Build.VERSION.SDK_INT < 23 || (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) == null) {
            z4 = false;
        } else {
            z4 = networkCapabilities.hasTransport(1);
            str = networkCapabilities.hasTransport(1) ? "wifi" : "none";
            if (networkCapabilities.hasTransport(3)) {
                str = "ethernet";
            }
            if (networkCapabilities.hasTransport(0)) {
                str = "mobile";
            }
        }
        hashMap.put("network_type", str);
        hashMap.put("wifi", Boolean.valueOf(z4));
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        if (connectionInfo == null) {
            return hashMap;
        }
        String ssid = connectionInfo.getSSID();
        int rssi = connectionInfo.getRssi();
        hashMap.put("wifi_bssid", connectionInfo.getBSSID());
        hashMap.put("wifi_ssid", ssid);
        hashMap.put("wifi_signal", rssi + BuildConfig.FLAVOR);
        int ipAddress = connectionInfo.getIpAddress();
        hashMap.put("local_ip", ipAddress != 0 ? String.format("%d.%d.%d.%d", Integer.valueOf(ipAddress & 255), Integer.valueOf((ipAddress >> 8) & 255), Integer.valueOf((ipAddress >> 16) & 255), Integer.valueOf((ipAddress >> 24) & 255)) : null);
        return hashMap;
    }

    public static Location createLocation() {
        String id = TimeZone.getDefault().getID();
        Location location = new Location();
        location.setTime_zone(id);
        return location;
    }

    public static NetworkInfo createNetworkInfo(ConnectivityManager connectivityManager, WifiManager wifiManager) {
        String str;
        NetworkCapabilities networkCapabilities;
        boolean z4 = false;
        str = "none";
        if (Build.VERSION.SDK_INT >= 23 && (networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork())) != null) {
            boolean hasTransport = networkCapabilities.hasTransport(1);
            str = networkCapabilities.hasTransport(1) ? "wifi" : "none";
            if (networkCapabilities.hasTransport(3)) {
                str = "ethernet";
            }
            if (networkCapabilities.hasTransport(0)) {
                str = "mobile";
            }
            z4 = hasTransport;
        }
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        NetworkInfo networkInfo = new NetworkInfo();
        networkInfo.setWifi(z4);
        networkInfo.setNetwork_type(str);
        if (connectionInfo == null) {
            return networkInfo;
        }
        String ssid = connectionInfo.getSSID();
        int rssi = connectionInfo.getRssi();
        if (ssid == null) {
            ssid = "null";
        }
        networkInfo.setWifi_ssid(ssid);
        networkInfo.setWifi_signal(rssi + BuildConfig.FLAVOR);
        return networkInfo;
    }
}
