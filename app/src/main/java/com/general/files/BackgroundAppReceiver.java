package com.general.files;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.chegou.motorista.MainActivity;

/**
 * Created by Admin on 17-02-2017.
 */
public class BackgroundAppReceiver extends BroadcastReceiver {

    MyBackGroundService myBgService;
    MainActivity mainAct;
    Context mContext;

    public BackgroundAppReceiver(Context mContext) {
        this.mContext = mContext;

        if (mContext instanceof MyBackGroundService) {
            myBgService = (MyBackGroundService) this.mContext;
        }
        if (mContext instanceof MainActivity) {
            mainAct = (MainActivity) this.mContext;
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (myBgService != null) {
//            myBgService.configBackground();
        }
        if (mainAct != null) {
            mainAct.configBackground();
        }
    }
}