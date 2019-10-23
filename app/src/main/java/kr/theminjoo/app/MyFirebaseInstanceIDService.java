package kr.theminjoo.app;

import android.content.Context;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();
    public static Context mContext;

    String CNT_URL = "https://8f53014f.ngrok.io/device/cnt/";
    String REG_URL = "https://8f53014f.ngrok.io/device/regist/";

    private  MainActivity mainActivity;

    // 토큰 재생성
    @Override
    public void onTokenRefresh() {

        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token) {

        HttpURLConnection httpURLConnection;
        InputStream inputStream;
        BufferedReader bufferedReader;
        StringBuilder stringBuilder;
        String temp;
        URL url;
        try {

            if(FirebaseInstanceId.getInstance().getToken() != null){

                CNT_URL = CNT_URL+FirebaseInstanceId.getInstance().getToken();
                url = new URL(CNT_URL);
                Log.d(TAG, "CNT_URL : " + CNT_URL);

                httpURLConnection = (HttpURLConnection) url.openConnection();
                inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder = new StringBuilder();
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }

                Log.e("total", " : "+stringBuilder.toString().trim());
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString().trim());

                int total =  jsonObject.getInt("total");
                Log.e("total", " : "+total);
                if (total == 0 ) {

                    try {

                        REG_URL = REG_URL+FirebaseInstanceId.getInstance().getToken()+"?os=1";
                        url = new URL(REG_URL);

                        Log.d(TAG, "REG_URL : " + REG_URL);
                        httpURLConnection = (HttpURLConnection) url.openConnection();
                        inputStream = httpURLConnection.getInputStream();

                        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                        stringBuilder = new StringBuilder();

                        while ((temp = bufferedReader.readLine()) != null) {
                            stringBuilder.append(temp + "\n");
                        }

                        bufferedReader.close();
                        inputStream.close();
                        httpURLConnection.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}