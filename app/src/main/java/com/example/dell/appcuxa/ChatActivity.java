package com.example.dell.appcuxa;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.MainPage.Adapter.MessageRoomChatAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
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

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {
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
    List<MessageItem> mesList = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private Boolean isConnected = true;
    private ImageView imgBack;
    private RobLightText tvBegin;
    SwipeRefreshLayout refreshLayout;
    String avatarFriend = "";
    private Activity activity;
    List<MessageItem> chats = new ArrayList<>();
    MessageRoomChatAdapter adapter;
    public Socket mSocket;
    public long page = 1;
    CuXaAPI cuXaAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_chat_room);
        init();
        cuXaAPI = NetworkController.upload();
        String name = AppUtils.getName(ChatActivity.this);
        Log.d("nameuserr",name);
        String token = AppUtils.getToken(ChatActivity.this);
        if (token.equals("")) {
            Log.d("token_chat", "rỗng");
        } else {
            {
                try {
                    Log.d("tokennnn",token);
                    mSocket = IO.socket(Constants.CHAT_SERVER_URL + token);
                    Log.d("tokennn",token);
                    mSocket.connect();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page++;
                getMessage(page);
            }
        });

        mSocket.on("connect", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("on_connect", "Connected");

            }
        });

      /*  mSocket.on("on_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String jsonString = args[0].toString();
                Log.d(TAG, jsonString);
                final MessageItem chat = new Gson().fromJson(jsonString, MessageItem.class);
                Log.d(TAG,chat.toString());
            }
        });*/

        mSocket.on("on_message", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                    Log.d("on_connect1", args[0]+"");
                    runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject object = (JSONObject) args[0];
                        try {

                            String content = object.getString("content");
                            byte[] data = Base64.decode(content, Base64.DEFAULT);
                            String text = new String(data, "UTF-8");
                            byte[] bytes = content.getBytes("UTF-8"); // Charset to encode into
                            Log.d("on_connect96",new String(object.getString("content").getBytes("ISO-8859-1"), "UTF-8").replace("\\",""));
                            String chatRoom = object.getString("chatRoom");
                            String type = object.getString("type");
                            String idUser = object.getString("user");
                            String createdAt = object.getString("createdAt");
                            UserObject userObject = new UserObject();
                            userObject.setId(idUser);
                            MessageItem messageItem = new MessageItem(userObject, type, content, chatRoom);
                            messageItem.setCreatedAt(createdAt);
                            mesList.add(messageItem);
                            adapter.notifyDataSetChanged();
                            scrollToBottom();
                            Log.d("sdfsdf", content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("on_connect", e.toString());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        Intent intent = getIntent();
        chat = (ObjectChat) intent.getSerializableExtra("object");
        if (chat != null) {
            mSocket.emit("join_room", chat.getId());
            getMessage(page);
            for (int i = 0; i < chat.getUsers().length; i++) {
                if (!chat.getUsers()[i].getId().equals(AppUtils.getIdUser(this))) {
                    avatarFriend = chat.getUsers()[i].getPicture();
                    Picasso.get().load(chat.getUsers()[i].getPicture()).placeholder(R.drawable.default_image).into(imgAvatar);
                    tvNameUserChat.setText(chat.getUsers()[i].getName());
                }
            }
        }
    }

    public void getMessage(final long page){
        Call<ChatObject> call = cuXaAPI.getListMess("Bearer " + AppUtils.getToken(this), chat.getId(), Constants.LIMIT_MESSAGE, page);
        call.enqueue(new Callback<ChatObject>() {
            @Override
            public void onResponse(Call<ChatObject> call, Response<ChatObject> response) {
                if (response.isSuccessful()) {
                    ChatObject chatObject = response.body();
                    chats = new ArrayList<>(Arrays.asList(chatObject.getMessageItems()));
                    Collections.reverse(chats);
                    if (page==1&&chats.size() <= 0) {
                        tvBegin.setVisibility(View.VISIBLE);
                    } else {
                        tvBegin.setVisibility(View.GONE);
                    }
                    if(mesList.size()>0){
                        mesList.addAll(0,chats);
                    }else{
                        mesList.addAll(mesList.size(),chats);
                    }
                    adapter = new MessageRoomChatAdapter(getApplicationContext(), mesList, avatarFriend);
                    LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                /*    manager.setReverseLayout(true);
                    manager.setStackFromEnd(true);*/
                    mMessagesView.setLayoutManager(manager);
                    mMessagesView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    scrollToBottom();
                }
                refreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ChatObject> call, Throwable t) {
                refreshLayout.setRefreshing(false);
            }
        });
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mesList.size() - 1);
    }


    private void init() {
        refreshLayout = findViewById(R.id.refreshLayout);
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
                Intent intent = new Intent(this, MainPageActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSendMess:
                if (chat != null) {
                    String content = edtChatContent.getText().toString().trim();
                    byte[] data = new byte[0];
                    try {
                        data = content.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                    if (content.length() == 0) {
                        return;
                    }
                    Gson gson = new Gson();
                    String json = gson.toJson(new MessageItem(chat.getId(), "text", base64));
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(AppUtils.haveNetworkConnection(getApplicationContext())){
                        mSocket.emit("send_message", jsonObject);
                    }else{
                        Toast.makeText(this, "Kiểm tra lại kết nối mạng", Toast.LENGTH_SHORT).show();
                    }
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
