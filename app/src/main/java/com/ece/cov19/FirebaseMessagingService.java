package com.ece.cov19;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.util.Log;


import androidx.core.app.NotificationCompat;

import com.ece.cov19.DataModels.UserDataModel;
import com.ece.cov19.RetroServices.RetroInstance;
import com.ece.cov19.RetroServices.RetroInterface;
import com.google.firebase.messaging.RemoteMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.ece.cov19.DataModels.LoggedInUserData.loggedInUserPhone;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private String requestedBy;
    private PendingIntent resultPendingIntent;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage);
    }

    public void showNotification(RemoteMessage remoteMessage) {
        int NOTIFICATION_ID = 0;
        String CHANNEL_ID = "Plasma+ Notification";
        CharSequence name = "Plasma+";
        String Description = "Plasma+ Notification channel";
        requestedBy = remoteMessage.getData().get("hidden");

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);


            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getData().get("body"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.help)
                .setAutoCancel(true);

        notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        Intent resultIntent = new Intent(this, DashboardActivity.class);

        if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.donor_profile_activity_notification_incoming_1))){
            resultIntent = new Intent(this, RequestsFromPatientsActivity.class);
        }
        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.donor_profile_activity_notification_accepted_1)) || remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.donor_profile_activity_notification_declined_1))){
            resultIntent = new Intent(this, PatientResponseActivity.class);
        }

        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_incoming_1))){
            resultIntent = new Intent(this, RequestsFromDonorsActivity.class);
        }
        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_accepted_1))){
            resultIntent = new Intent(this, DonorResponseActivity.class);
        }
        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_declined_1))){
            resultIntent = new Intent(this, DonorResponseActivity.class);
        }
        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_donated_1)) || remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_not_donated_1)) || remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_canceled_1))){
            if(requestedBy.equals("donor")){
                resultIntent = new Intent(this, RequestsFromDonorsActivity.class);
            }
            else if(requestedBy.equals("patient")){
                resultIntent = new Intent(this, DonorResponseActivity.class);
            }
        }

        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.donor_profile_activity_notification_confirmed_1)) || remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.donor_profile_activity_notification_not_confirmed_1))){
            if(requestedBy.equals("donor")){
                resultIntent = new Intent(this, PatientResponseActivity.class);
            }
            else if(requestedBy.equals("patient")){
                resultIntent = new Intent(this, RequestsFromPatientsActivity.class);
            }
        }


        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_auto_not_donated)) || remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_auto_declined))){
            if(requestedBy.equals("donor")){
                resultIntent = new Intent(this, PatientResponseActivity.class);
            }
            else if(requestedBy.equals("patient")){
                resultIntent = new Intent(this, RequestsFromPatientsActivity.class);
            }
        }

        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_no_response_from_donor))){
            if(requestedBy.equals("donor")){
                resultIntent = new Intent(this, PatientResponseActivity.class);
            }
            else if(requestedBy.equals("patient")){
                resultIntent = new Intent(this, RequestsFromPatientsActivity.class);
            }
        }

        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_no_response_from_patient))){
            if(requestedBy.equals("donor")){
                resultIntent = new Intent(this, PatientResponseActivity.class);
            }
            else if(requestedBy.equals("patient")){
                resultIntent = new Intent(this, RequestsFromPatientsActivity.class);
            }
        }
        else if(remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_profile_expire_1)) ||
                remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_profile_expire_2)) ||
                remoteMessage.getNotification().getTitle().equals(getResources().getString(R.string.patient_profile_activity_notification_profile_delete))){
            resultIntent = new Intent(this, MyPatientsActivity.class);
        }

        resultIntent.putExtra("notification","yes");

        RetroInterface retroInterface = RetroInstance.getRetro();
        Call<UserDataModel> checkNotification = retroInterface.deleteNotification(loggedInUserPhone);
        checkNotification.enqueue(new Callback<UserDataModel>() {
            @Override
            public void onResponse(Call<UserDataModel> call, Response<UserDataModel> response) {


            }

            @Override
            public void onFailure(Call<UserDataModel> call, Throwable t) {
            }
        });

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
        stackBuilder.addParentStack(DashboardActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token);
    }



}