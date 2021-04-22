package com.mesh.testing.AsyncTasks;

import android.os.AsyncTask;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import com.mesh.testing.MainActivity;

public class SendRecieve extends Thread {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private MainActivity mActivity;
    private Handler handler;

    public SendRecieve(Socket skt, MainActivity mActivity,Handler handler) {
        this.mActivity=mActivity;
        this.handler=handler;
        socket = skt;
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        int bytes;
        while (socket != null) {
            try {
                bytes = inputStream.read(buffer);
                if (bytes > 0) {
                    handler.obtainMessage(mActivity.MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}