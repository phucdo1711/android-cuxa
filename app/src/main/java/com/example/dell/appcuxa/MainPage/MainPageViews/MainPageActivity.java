package com.example.dell.appcuxa.MainPage.MainPageViews;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.dell.appcuxa.Application.ChatApplication;
import com.example.dell.appcuxa.CustomeView.NonSwipeableViewPager;
import com.example.dell.appcuxa.Login.LoginView.MainActivity;
import com.example.dell.appcuxa.MainPage.Adapter.ViewPagerAdapter;
import com.example.dell.appcuxa.MainPage.MainPageViews.MessTab.MessView.FragmentMess;
import com.example.dell.appcuxa.MainPage.MainPageViews.NotiTab.NotiView.FragmentNotification;
import com.example.dell.appcuxa.MainPage.MainPageViews.ProfileTab.ProfileView.FragmentProfile;
import com.example.dell.appcuxa.MainPage.MainPageViews.SavedTab.SavedView.FragmentSaved;
import com.example.dell.appcuxa.R;
import com.example.dell.appcuxa.Utils.AppUtils;
import com.example.dell.appcuxa.Utils.Constants;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements View.OnClickListener{
    FragmentSaved fragmentSaved;
    FragmentProfile fragmentProfile;
    FragmentMess fragmentMess;
    FragmentSearch fragmentSearch;
    FragmentNotification fragmentNoti;
    @BindView(R.id.main_pager)
    public NonSwipeableViewPager viewPager;
    @BindView(R.id.bottom_navigation)
    public BottomNavigationView mBottomNav;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    MenuItem prevMenuItem;
    public Socket mSocket;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        ButterKnife.bind(this);
        sharedPreferences = getSharedPreferences("login_data",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        String token = AppUtils.getToken(MainPageActivity.this);
        /*if(!token.equals("")){
            {
                try {
                    mSocket = IO.socket(Constants.CHAT_SERVER_URL+ token);
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }
        }*/
        /*if(mSocket!=null){
            mSocket.connect();
            mSocket.on("connect", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Log.d("aaaaaaabc","Connected");
                }
            });
        }*/

        mBottomNav.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_search:
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_save:
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_mess:
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.action_noti:
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.action_profile:
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                }
                else
                {
                    mBottomNav.getMenu().getItem(0).setChecked(false);
                }
                Log.d("page", "onPageSelected: "+position);
                mBottomNav.getMenu().getItem(position).setChecked(true);
                prevMenuItem = mBottomNav.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setupViewPager(viewPager);

    }
    public Socket getmSocket(){
        return mSocket;
    }

    @Override
    protected void onStart() {
        super.onStart();
        String saveToken = sharedPreferences.getString("token","");
        String name = sharedPreferences.getString("name","");
        Log.d("nameuser",name);
        if(saveToken.equals("")){
            Intent intent = new Intent(MainPageActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){

        }
    }
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
        builder.setTitle("Close application?");
        builder.setMessage("This action will close your program on device.");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // do nothing
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                moveTaskToBack(true);
            }
        });
        builder.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragmentMess = new FragmentMess();
        fragmentNoti = new FragmentNotification();
        fragmentProfile = new FragmentProfile();
        fragmentSaved = new FragmentSaved();
        fragmentSearch = new FragmentSearch();
        adapter.addFragment(fragmentSearch);
        adapter.addFragment(fragmentSaved);
        adapter.addFragment(fragmentMess);
        adapter.addFragment(fragmentNoti);
        adapter.addFragment(fragmentProfile);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(5);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* mSocket.close();*/
    }

}
