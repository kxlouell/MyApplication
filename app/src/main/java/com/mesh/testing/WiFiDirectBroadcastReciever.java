package com.mesh.testing;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

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
        System.out.println("Running onRecieve method");
        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            System.out.println("Wifi p2p state changed");
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                Toast.makeText(context, "Wifi is ON", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Wifi is OFF", Toast.LENGTH_SHORT).show();
            }

        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            //do something
            System.out.println("Wifi p2p peers changed");
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
            if(mManager == null){
                return;
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mCallback = new ConnectivityManager.NetworkCallback(){
                    @Override
                    public void onLost(@NonNull Network network) {
                        Toast.makeText(context, "Connection Lost", Toast.LENGTH_SHORT).show();
                        mActivity.connectionStatus.setText("Device Disconnected");
                    }

                    @Override
                    public void onAvailable(@NonNull Network network) {
                        Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
                        mManager.requestConnectionInfo(mChannel,mActivity.connectionInfoListener);
                    }

                    @Override
                    public void onUnavailable() {
                        mActivity.connectionStatus.setText("Device Disconnected");
                    }
                };
            }else {
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
                if (networkInfo.isConnected()) {
                    mManager.requestConnectionInfo(mChannel, mActivity.connectionInfoListener);
                }else {
                    mActivity.connectionStatus.setText("Device Disconnected");
                }
            }


        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)){
            //do something
        }
    }
}
