package kr.co.dodoom.sacoop;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.WebView;

import com.google.firebase.messaging.RemoteMessage;

import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    Bitmap bigPicture;
    private WebView mWebView;
    static  int msgCnt = 0;

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String messagae = data.get("message");
        String imgurllink = data.get("imgurllink");
        String link = remoteMessage.getData().get("link");

        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        Log.d(TAG, "imgurl: " + imgurllink);

        //Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        MyFirebaseMessagingService.msgCnt ++;
        //MainActivity.updateIconBadge(context, MyFirebaseMessagingService.msgCnt);


        MainActivity.updateIconBadge(this, MyFirebaseMessagingService.msgCnt);
        sendNotification(title, messagae,imgurllink);



    }


    private void sendNotification(String title, String message, String myimgurl ) {

        Intent intent;
        /*
        if (linkurl!=null) {
            intent = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(linkurl));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //startActivity(new Intent(getApplicationContext(), MainActivity.class));

            startActivity(intent);

        } else {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }
*/
//테스트
        intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //CompatBuilder를 이용한 알림방식
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);

        notificationBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.sacoop_icon));
        notificationBuilder.setSmallIcon(R.drawable.sacoop_icon);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setContentIntent(pendingIntent);


        if(myimgurl!=null) {
            Log.d(TAG, "myimgurl:"+myimgurl+"/" );
            Log.d(TAG, "pushtype: bigPicture" );
            //이미지 온라인 링크를 가져와 비트맵으로 바꾼다.
            try {
                URL url = new URL(myimgurl);
                bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }


            notificationBuilder.setContentText("아래로 천천히 드래그 하세요.");

            //이미지를 보내는 스타일 사용하기
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPicture)
                    .setBigContentTitle(title)
                    .setSummaryText(message));



        }else if(message.length() > 100) {

            Log.d(TAG, "pushtype: BigTextStyle" );

            notificationBuilder.setContentText("아래로 천천히 드래그 하세요.");
            //BigTextStyle
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(message));

        }else{

            notificationBuilder.setContentTitle(title);
            Log.d(TAG, "pushtype: message" );
            notificationBuilder.setContentText(message);

        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int)(System.currentTimeMillis()/1000)  /* ID of notification */, notificationBuilder.build());
    }
}