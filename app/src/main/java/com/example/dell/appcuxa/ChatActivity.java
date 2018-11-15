package com.example.dell.appcuxa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.MessageAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.MessageRoomChatAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.FragmentChatRoom;
import com.example.dell.appcuxa.ObjectModels.ChatObject;
import com.example.dell.appcuxa.ObjectModels.Message;
import com.example.dell.appcuxa.ObjectModels.MessageItem;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.ObjectModels.UserObject;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.example.dell.appcuxa.Utils.Constants;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener{
    public ImageView btnSendMess;
    ImageView imgAvatar;
    RobEditText edtChatContent;
    RobBoldText tvNameUserChat;
    ObjectChat chatObject;
    private RecyclerView mMessagesView;

    private static final String TAG = "FragmentChatRoom";

    private static final int REQUEST_LOGIN = 0;
    private List<Message> mMessages = new ArrayList<Message>();
    private static final int TYPING_TIMER_LENGTH = 600;
    // private Socket mSocket;
    ObjectChat chat;
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private Boolean isConnected = true;
    private ImageView imgBack;
    private RobLightText tvBegin;
    String avatarFriend = "";
    private Activity activity;
    List<MessageItem> chats = new ArrayList<>();
    MessageRoomChatAdapter adapter;
    public Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_room);
        init();
        {
            try {
                mSocket = IO.socket(Constants.CHAT_SERVER_URL+ "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjViOTE4NGI5MTQ3NzVmNzRmOTgxNjg0NCIsImlhdCI6MTU0MTk0MjY4NH0.4hlQffEnJQmZq_Pxe7LPh9wCNqunXXcbjC8Fq-wvAKU");
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mSocket.connect();

        mSocket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("on_connect","Connected");

            }
        });

        mSocket.on("on_message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.d("on_connect1",args[0].toString());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = (JSONObject) args[0];
                        try {
                            String content = object.getString("content");
                            String chatRoom = object.getString("chatRoom");
                            String type = object.getString("type");
                            String idUser = object.getString("user");
                            UserObject userObject = new UserObject();
                            userObject.setId(idUser);
                            MessageItem messageItem = new MessageItem(userObject,type,content,chatRoom);
                            chats.add(messageItem);
                            adapter.notifyDataSetChanged();
                            scrollToBottom();
                            Log.d("sdfsdf",content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("on_connect",e.toString());
                        }
                    }
                });


            }
        });
        Intent intent = getIntent();
        chat = (ObjectChat) intent.getSerializableExtra("object");
        if(chat!=null){
            mSocket.emit("join_room", "5be82db0a5a54f6a33cb15bb");
            CuXaAPI cuXaAPI = NetworkController.upload();
            Call<ChatObject> call = cuXaAPI.getListMess("Bearer " + AppUtils.getToken(this), chat.getId());
            call.enqueue(new Callback<ChatObject>() {
                @Override
                public void onResponse(Call<ChatObject> call, Response<ChatObject> response) {
                    if (response.isSuccessful()) {
                        ChatObject chatObject = response.body();
                        chats = new ArrayList<>(Arrays.asList(chatObject.getMessageItems()));
                        if (chats.size() > 0) {
                            tvBegin.setVisibility(View.GONE);
                        } else {
                            tvBegin.setVisibility(View.VISIBLE);
                        }
                        adapter = new MessageRoomChatAdapter(getApplicationContext(), chats, avatarFriend);
                        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                        mMessagesView.setLayoutManager(manager);
                        mMessagesView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<ChatObject> call, Throwable t) {

                }
            });
                for (int i = 0; i < chat.getUsers().length; i++) {
                    if (!chat.getUsers()[i].getId().equals(AppUtils.getIdUser(this))) {
                        avatarFriend = chat.getUsers()[i].getPicture();
                        Picasso.get().load(chat.getUsers()[i].getPicture()).placeholder(R.drawable.default_image).into(imgAvatar);
                        tvNameUserChat.setText(chat.getUsers()[i].getName());
                    }
                }
        }



    }
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(chats.size() - 1);
    }


    private void init() {
        mMessagesView = findViewById(R.id.recChat);
        edtChatContent = findViewById(R.id.edtChatContent);
        btnSendMess = findViewById(R.id.btnSendMess);
        btnSendMess.setOnClickListener(this);
        tvNameUserChat = findViewById(R.id.tvUserName);
        imgAvatar = findViewById(R.id.imgAvatar);
        imgBack = findViewById(R.id.imgBack);
        tvBegin = findViewById(R.id.tvBegin);
        imgBack.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                Intent intent = new Intent(this,MainPageActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSendMess:
                if(chat!=null){
                    String content = edtChatContent.getText().toString().trim();
                    if(content.length()==0){
                        return;
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(new MessageItem(chat.getId(), "text", content));
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit("send_message", jsonObject);
                    edtChatContent.setText("");
                }

                break;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }
}
