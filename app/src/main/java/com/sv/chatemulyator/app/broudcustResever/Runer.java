package com.sv.chatemulyator.app.broudcustResever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.sv.chatemulyator.app.service.ChatService;

/**
 * Created by Created by IntelliJ IDEA.
 * User: SV
 * Date: 03.11.2014
 * Time: 23:52
 * For the ChatEmulyator project.
 */
public class Runer extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        context.startService(new Intent(context, ChatService.class));
    }
}
