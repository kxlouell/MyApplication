package com.mesh.testing.BroadCastReciever;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.mesh.testing.MainActivity;

public class WiFiDirectBroadcastReciever extends BroadcastReceiver {

    private WifiP2pManager mManager;
    private WifiP2pManager.Channel mChannel;
    private MainActivity mActivity;
    private ConnectivityManager mCManager;
    private ConnectivityManager.NetworkCallback mCallback;

    public WiFiDirectBroadcastReciever(WifiP2pManager mManager, WifiP2pManager.Channel mChannel, MainActivity mActivity, ConnectivityManager mCManager, ConnectivityManager.NetworkCallback mCallback) {
        this.mManager = mManager;
        this.mChannel = mChannel;
        this.mActivity = mActivity;
        this.mCManager = mCManager;
        this.mCallback = mCallback;
        System.out.println("Running BroadCast Reciever");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //do something
            Toast.makeText(context, "p2p peer changed action", Toast.LENGTH_SHORT).show();
            if (mManager != null) {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    mActivity.requestPermissions();
                    System.out.println("Permission Not Granted");
                }else {
                    System.out.println("Permission Granted");
                }
                System.out.println("requesting peer");
                mManager.requestPeers(mChannel, mActivity.peerListListener);
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)){
            //do something
            Toast.makeText(context, "Connection Changed Action", Toast.LENGTH_SHORT).show();
            if(mManager == null){
                Toast.makeText(context, "mManager is null", Toast.LENGTH_SHORT).show();
                return;
            }
            Toast.makeText(context, "mManager is  not null", Toast.LENGTH_SHORT).show();
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if (networkInfo.isConnected()) {
                    mManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
            }else {
                    mActivity.connectionStatus.setText("Device Disconnected");
            }



        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //do something
            Toast.makeText(context, "this device change action", Toast.LENGTH_SHORT).show();
        }
    }
}
