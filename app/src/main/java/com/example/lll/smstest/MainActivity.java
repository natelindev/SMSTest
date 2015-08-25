package com.example.lll.smstest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView sender;
    private TextView content;
    private IntentFilter receiverFilter;
    private MessageReceiver messageReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sender = (TextView) findViewById(R.id.sender);
        content = (TextView) findViewById(R.id.content);
        receiverFilter = new IntentFilter();
        receiverFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        receiverFilter.setPriority(100);
        messageReceiver = new MessageReceiver();
        registerReceiver(messageReceiver,receiverFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(messageReceiver);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class MessageReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context,Intent intent){
            Bundle bundle = intent.getExtras();
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0;i < messages.length;i++){
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String address = messages[0].getOriginatingAddress();
                String fullMessage = "";
                for (SmsMessage message:messages){
                    fullMessage = message.getMessageBody();
                }
                sender.setText(address);
                content.setText(fullMessage);
                abortBroadcast();
            }

        }
    }
}
