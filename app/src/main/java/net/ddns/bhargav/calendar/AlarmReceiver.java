package net.ddns.bhargav.calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.Toast;

public class AlarmReceiver extends Service {
    public AlarmReceiver() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId){
        String message = intent.getStringExtra("MESSAGE");
        Toast.makeText(getBaseContext(),"Alarm...",Toast.LENGTH_LONG).show();
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final MediaPlayer mp = MediaPlayer.create(getBaseContext(),notification);
        mp.setLooping(true);
        mp.start();
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(message)
                .create();
        alertDialog.setCancelable(false);
        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mp.stop();
                AlarmReceiver.this.stopSelf();
            }
        });
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();
        return START_STICKY;
    }

}
