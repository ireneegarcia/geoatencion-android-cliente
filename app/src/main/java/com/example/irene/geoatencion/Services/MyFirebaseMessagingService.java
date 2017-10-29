package com.example.irene.geoatencion.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.irene.geoatencion.MainActivity;
import com.example.irene.geoatencion.MapsFragment;
import com.example.irene.geoatencion.Model.NotificationFirebase;
import com.example.irene.geoatencion.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Irene on 2/10/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public static NotificationFirebase notification;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getNotification() != null) {

            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();

            Log.d("firebase", "NOTIFICACION RECIBIDA");
            Log.d("firebase", "Título: " + titulo);
            Log.d("firebase", "Texto: " + texto);

            //Opcional: mostramos la notificación en la barra de estado
            //showNotification(titulo, texto);
        }

        if(remoteMessage.getData() != null) {
            Log.d("firebase", "DATOS RECIBIDOS");
            Log.d("firebase", "Latitud: " + remoteMessage.getData().get("networkLatitude"));
            Log.d("firebase", "Logintud: " + remoteMessage.getData().get("networkLongitude"));
            Log.d("firebase", "Unidad: " + remoteMessage.getData().get("networkCode"));
            Log.d("firebase", "Status: " + remoteMessage.getData().get("status"));

            notification = new NotificationFirebase(remoteMessage.getData().get("networkLatitude"),
                    remoteMessage.getData().get("networkLongitude"),
                    remoteMessage.getData().get("networkAddress"),
                    remoteMessage.getData().get("networkCode"),
                    remoteMessage.getData().get("status"));
            MapsFragment.AgregarMarcadorPush(notification);
            String titulo = remoteMessage.getNotification().getTitle();
            String texto = remoteMessage.getNotification().getBody();
            //showNotification(titulo, texto);
        }
    }

    private void showNotification(String title, String text) {
        Intent i = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setAutoCancel(true)
                        .setSmallIcon(android.R.drawable.stat_sys_warning)
                        .setContentTitle(title)
                        .setContentText(text)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
