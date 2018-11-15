package com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dell.appcuxa.Application.ChatApplication;
import com.example.dell.appcuxa.CustomeView.RobBoldText;
import com.example.dell.appcuxa.CustomeView.RobEditText;
import com.example.dell.appcuxa.CustomeView.RobLightText;
import com.example.dell.appcuxa.CuxaAPI.CuXaAPI;
import com.example.dell.appcuxa.CuxaAPI.NetworkController;
import com.example.dell.appcuxa.Login.LoginView.MainActivity;
import com.example.dell.appcuxa.MainPage.Adapter.MessageAdapter;
import com.example.dell.appcuxa.MainPage.Adapter.MessageRoomChatAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.MainPageActivity;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentDieuKhoan;
import com.example.dell.appcuxa.ObjectModels.ChatObject;
import com.example.dell.appcuxa.ObjectModels.Message;
import com.example.dell.appcuxa.ObjectModels.MessageItem;
import com.example.dell.appcuxa.ObjectModels.ObjectChat;
import com.example.dell.appcuxa.ObjectModels.RoomSearchItem;
import com.example.dell.appcuxa.R;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChatRoom extends DialogFragment implements View.OnClickListener {
    public View mMainView;
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
    public FragmentChatRoom() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                Log.d("aaaaaaa","Connected");
                mSocket.on("on_message", new Emitter.Listener() {
                    @Override
                    public void call(Object... args) {
                        JSONObject object = (JSONObject) args[0];
                        Log.d("data_object",object.toString()+" - "+args.toString());
                        try {
                            String content = object.getString("content");
                            Log.d("sdfsdf",content);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMainView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        ((MainPageActivity) getActivity()).getmSocket().connect();
        ((MainPageActivity) getActivity()).getmSocket().on("on_message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject object = (JSONObject) args[0];
                Log.d("data_object",object.toString()+" - "+args.toString());
                try {
                    String content = object.getString("content");
                    String chatRoom = object.getString("chatRoom");
                    String type = object.getString("type");
                    MessageItem messageItem = new MessageItem(chatRoom,type,content);
                    chats.add(messageItem);
                    adapter.notifyDataSetChanged();
                    scrollToBottom();
                    Log.d("sdfsdf",content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        init();
        setHasOptionsMenu(true);


        return mMainView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
     /*   mSocket.disconnect();
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);*/
    }

    private void init() {
        mMessagesView = mMainView.findViewById(R.id.recChat);
        edtChatContent = mMainView.findViewById(R.id.edtChatContent);
        btnSendMess = mMainView.findViewById(R.id.btnSendMess);
        btnSendMess.setOnClickListener(this);
        tvNameUserChat = mMainView.findViewById(R.id.tvUserName);
        imgAvatar = mMainView.findViewById(R.id.imgAvatar);
        imgBack = mMainView.findViewById(R.id.imgBack);
        tvBegin = mMainView.findViewById(R.id.tvBegin);
        imgBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgBack:
                FragmentChatRoom.this.dismiss();
                break;
            case R.id.btnSendMess:
                String content = edtChatContent.getText().toString().trim();
                if(content.length()==0){
                    return;
                }
                Gson gson = new Gson();
                String json = gson.toJson(new MessageItem(chatObject.getId(), "text", content));
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ((MainPageActivity) getActivity()).getmSocket().emit("send_message", jsonObject);
                break;

        }
    }

    // This event fires 1st, before creation of fragment or any views
    // The onAttach method is called when the Fragment instance is associated with an Activity.
    // This does not mean the Activity is fully initialized.
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new MessageAdapter(context, mMessages);
        if (context instanceof Activity) {
            //this.listener = (MainActivity) context;
        }
    }

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    public ObjectChat setObject(ObjectChat objectChat, Activity activity) {
        this.activity = activity;
        this.chatObject = objectChat;
        ((MainPageActivity) activity).getmSocket().emit("join_room", objectChat.getId(),new Emitter.Listener() {

            @Override
            public void call(final Object... args) {
                String res = (String) args[0];
                Log.d("sadgsdf",args.toString());
                Toast.makeText(getActivity(), res, Toast.LENGTH_SHORT).show();
            }
        });
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<ChatObject> call = cuXaAPI.getListMess("Bearer " + AppUtils.getToken(activity), objectChat.getId());
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
                    adapter = new MessageRoomChatAdapter(getContext(), chats, avatarFriend);
                    LinearLayoutManager manager = new LinearLayoutManager(getContext());
                    manager.setReverseLayout(true);
                    mMessagesView.setLayoutManager(manager);
                    mMessagesView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ChatObject> call, Throwable t) {

            }
        });
        return objectChat;
    }
}
