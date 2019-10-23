package kr.theminjoo.app;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.webkit.WebView;

import com.google.firebase.messaging.RemoteMessage;
import kr.theminjoo.app.R;

import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    Bitmap bigPicture;
    private WebView mWebView;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    static  int msgCnt = 0;

    // 메시지 수신
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i(TAG, "onMessageReceived");

        Map<String, String> data = remoteMessage.getData();
        String title = data.get("title");
        String messagae = data.get("body");
        String imgurllink = data.get("imgurllink");
        String link = data.get("link");

        MyFirebaseMessagingService.msgCnt ++;

        MainActivity.updateIconBadge(this, MyFirebaseMessagingService.msgCnt);
        sendNotification(title, messagae,imgurllink,link);
    }


    private void sendNotification(String title, String message, String myimgurl , String link) {

        Intent intent;

        intent = new Intent(this, SplashActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("url", link);
        intent.putExtras(bundle);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //CompatBuilder를 이용한 알림방식
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int)(System.currentTimeMillis()/1000) /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.minjoo_icon))
        .setSmallIcon(R.drawable.minjoo_icon_small)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        .setSound(defaultSoundUri)
        .setContentIntent(pendingIntent);

        //OREO API 26 이상에서는 채널 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            //builder.setSmallIcon(R.drawable.ic_launcher_foreground); //mipmap 사용시 Oreo 이상에서 시스템 UI 에러남
            CharSequence channelName  = "더불어민주당 채널";
            String description = "";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName , importance);
            channel.setDescription(description);

            // 노티피케이션 채널을 시스템에 등록
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);

        }

        if(myimgurl!=null) {
            //이미지 온라인 링크를 가져와 비트맵으로 바꾼다.
            try {
                URL url = new URL(myimgurl);
                bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }


        builder.setContentText("아래로 천천히 드래그 하세요.");

            //이미지를 보내는 스타일 사용하기
        builder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bigPicture)
                    .setBigContentTitle(title)
                    .setSummaryText(message));

        }else if(message.length() > 100) {

            builder.setContentText("아래로 천천히 드래그 하세요.");
            //BigTextStyle
            builder.setStyle(new NotificationCompat.BigTextStyle()
                    .setBigContentTitle(title)
                    .bigText(message));

        }else{

            builder.setContentTitle(title);
            builder.setContentText(message);

        }

        notificationManager.notify((int)(System.currentTimeMillis()/1000)  /* ID of notification */, builder.build());
    }
}