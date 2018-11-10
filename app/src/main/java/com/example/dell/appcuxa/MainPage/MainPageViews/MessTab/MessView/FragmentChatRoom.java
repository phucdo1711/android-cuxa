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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

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
    RoomSearchItem roomSearchItem;

    public FragmentChatRoom() {

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
                try {
                    String content = object.getString("content");
                    Log.d("sdfsdf", content);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        init();
        setHasOptionsMenu(true);
        ((MainPageActivity) getActivity()).getmSocket().connect();
     /*   ChatApplication app = (ChatApplication) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();*/

        if (roomSearchItem != null) {
            Picasso.get().load(roomSearchItem.getLandLord().getPicture()).into(imgAvatar);
            tvNameUserChat.setText(roomSearchItem.getLandLord().getName());
        }
        if (chatObject != null) {
            for (int i = 0; i < chatObject.getUsers().length; i++) {
                if (!chatObject.getUsers()[i].getId().equals(AppUtils.getIdUser(getActivity()))) {
                    avatarFriend = chatObject.getUsers()[i].getPicture();
                    Picasso.get().load(chatObject.getUsers()[i].getPicture()).placeholder(R.drawable.default_image).into(imgAvatar);
                    tvNameUserChat.setText(chatObject.getUsers()[i].getName());
                }
            }
        }

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

    public RoomSearchItem dataObject(RoomSearchItem roomSearchItem) {
        this.roomSearchItem = roomSearchItem;
        return roomSearchItem;
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

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        Log.d(TAG, "connect");
                        isConnected = true;
                    }
                }
            });
        }
    };
    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "diconnected");
                    isConnected = false;
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Error connecting");
                }
            });
        }
    };
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    removeTyping(username);
                    addMessage(username, message);
                }
            });
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_joined, username));
                    addParticipantsLog(numUsers);
                }
            });
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    int numUsers;
                    try {
                        username = data.getString("username");
                        numUsers = data.getInt("numUsers");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                    addLog(getResources().getString(R.string.message_user_left, username));
                    addParticipantsLog(numUsers);
                    removeTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    addTyping(username);
                }
            });
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    try {
                        username = data.getString("username");
                    } catch (JSONException e) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }
                    removeTyping(username);
                }
            });
        }
    };

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            //mSocket.emit("stop typing");
        }
    };

    private void addMessage(String username, String message) {
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                .username(username).message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

   /* private void attemptSend() {
        if (null == roomSearchItem.getLandLord().getName()) return;
        if (!mSocket.connected()) return;

        mTyping = false;

        String message = edtChatContent.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            edtChatContent.requestFocus();
            return;
        }

        edtChatContent.setText("");
        addMessage(roomSearchItem.getLandLord().getName(), message);

        // perform the sending message attempt.
        mSocket.emit("new message", message);
    }*/

    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


    public ObjectChat setObject(ObjectChat objectChat, Activity activity) {
        this.chatObject = objectChat;
        CuXaAPI cuXaAPI = NetworkController.upload();
        Call<ChatObject> call = cuXaAPI.getListMess("Bearer " + AppUtils.getToken(activity), objectChat.getId());
        call.enqueue(new Callback<ChatObject>() {
            @Override
            public void onResponse(Call<ChatObject> call, Response<ChatObject> response) {
                if (response.isSuccessful()) {
                    ChatObject chatObject = response.body();
                    List<MessageItem> chats = new ArrayList<>(Arrays.asList(chatObject.getMessageItems()));
                    if (chats.size() > 0) {
                        tvBegin.setVisibility(View.GONE);
                    } else {
                        tvBegin.setVisibility(View.VISIBLE);
                    }
                    MessageRoomChatAdapter adapter = new MessageRoomChatAdapter(getContext(), chats, avatarFriend);
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
