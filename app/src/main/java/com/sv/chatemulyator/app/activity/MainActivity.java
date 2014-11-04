package com.sv.chatemulyator.app.activity;

import android.app.NotificationManager;
import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ListView;
import com.sv.chatemulyator.app.R;
import com.sv.chatemulyator.app.db.MessageCursoreAdapter;
import com.sv.chatemulyator.app.db.MessageDBHelper;
import com.sv.chatemulyator.app.service.ChatService;

public class MainActivity extends ActionBarActivity implements AbsListView.OnScrollListener {

    public static final int OPEN_ACTIVITY_FROM_NOTIFICATION = 3;

    private ChatService s;
    private EditText message;
    private ListView list;
    private boolean scrollFlag = true;
    private MessageCursoreAdapter customAdapter;
    private MessageDBHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancel(OPEN_ACTIVITY_FROM_NOTIFICATION);
        databaseHelper = new MessageDBHelper(this);

        list = (ListView) findViewById(R.id.list);

        list.setOnScrollListener(this);
        message = (EditText) findViewById(R.id.message_text);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                customAdapter = new MessageCursoreAdapter(getApplicationContext(), databaseHelper.getAllData());
                list.setAdapter(customAdapter);
                list.setSelection(list.getCount() - 1);
            }
        });
        startService(new Intent(MainActivity.this, ChatService.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, new IntentFilter(ChatService.NOTIFICATION));
        Intent intent= new Intent(this, ChatService.class);
        bindService(intent, mConnection,
                Context.BIND_AUTO_CREATE);
        list.setSelection(list.getCount() - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
        unbindService(mConnection);
    }

    public void send(View view) {
        if (message.getText().toString().equals(""))
            return;

        s.publishResults(message.getText().toString(), 0);
        message.setText("");
    }

    private void autoScrollToNewMessage() {
        if(!scrollFlag)
            return;

        list.post(new Runnable() {
            @Override
            public void run() {
                list.setSelection(list.getCount() - 1);
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount - visibleItemCount - 2 > firstVisibleItem) {
            scrollFlag = false;
        } else {
            scrollFlag = true;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            ChatService.MyBinder b = (ChatService.MyBinder) binder;
            s = b.getService();
        }

        public void onServiceDisconnected(ComponentName className) {
            s = null;
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                autoScrollToNewMessage();
                customAdapter.changeCursor(databaseHelper.getAllData());
            }
        }
    };

}