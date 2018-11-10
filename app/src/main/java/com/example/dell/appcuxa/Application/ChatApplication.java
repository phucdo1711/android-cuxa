package com.example.dell.appcuxa.Application;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.example.dell.appcuxa.Utils.Constants;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;


import java.net.URISyntaxException;

public class ChatApplication extends Application {
    String token = AppUtils.getToken(this);
    public static final String TAG = ChatApplication.class
            .getSimpleName();

    private RequestQueue mRequestQueue;

    private static ChatApplication mInstance;
    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(Constants.CHAT_SERVER_URL+"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViYTRmMTk0YWRkZTcwMGZjYTRlYjgyMyIsImlhdCI6MTU0MTUwNjg5OX0.iHN8AF4LtVrJx6yOcnaRhBuoTi7Unic9xvboMfF39r4");
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return mSocket;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mSocket.connect();
    }

    public static synchronized ChatApplication getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }
}
