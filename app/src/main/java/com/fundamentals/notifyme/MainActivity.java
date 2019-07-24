package com.fundamentals.notifyme;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// SOS: the tutorial also includes a DirectReply from within the notification...
public class MainActivity extends AppCompatActivity {

    private static final String PRIMARY_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".primary_notification_channel";
    private static final int NOTIFICATION_ID = 42;

    private NotificationManager mNotificationManager;
    private Button mButtonNotify;
    private Button mButtonUpdate;
    private Button mButtonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonNotify = findViewById(R.id.notify);
        mButtonUpdate = findViewById(R.id.update);
        mButtonCancel = findViewById(R.id.cancel);

        // SOS: After API 26, I MUST construct a notification channel, otherwise crash!
        createNotificationChannel();

        setButtonState(true, false, false);
    }

    private void createNotificationChannel() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            // Create a NotificationChannel
            NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID,
                    "Mascot Notification", NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        // the intent used when we click on the notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
                .setContentTitle("You've been notified!")
                .setContentText("This is your notification text.")
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
    }

    public void sendNotification(View view) {
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setButtonState(false, true, true);
    }

    // SOS: we use BigPictureStyle for the notification when we update it
    public void updateNotification(View view) {
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);

        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                .bigPicture(androidImage)
                .setBigContentTitle("Notification Updated!"));

        mNotificationManager.notify(NOTIFICATION_ID, notifyBuilder.build());

        setButtonState(false, false, true);
    }

    public void cancelNotification(View view) {
        mNotificationManager.cancel(NOTIFICATION_ID);

        setButtonState(true, false, false);
    }

    private void setButtonState(boolean notifyEnabled, boolean updateEnabled, boolean cancelEnabled) {
        mButtonNotify.setEnabled(notifyEnabled);
        mButtonUpdate.setEnabled(updateEnabled);
        mButtonCancel.setEnabled(cancelEnabled);
    }
}
