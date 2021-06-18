package Connect;

import android.content.Context;
import android.net.ConnectivityManager;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class CommonUtils {

    // for getting URL encoded string
    public static String getURLEncodedString(List<KeyValueTab> list) {

        String respString = "";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    respString = list.get(i).getKey() + "=" + list.get(i).getValue();
                } else {
                    respString = respString + "&" + list.get(i).getKey() + "=" + list.get(i).getValue();
                }
            }
        }

        return respString;

    }

    //for checking internet connection
    public static boolean checkInternetStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }


    //for executing the web requests
    public static String executeWebRequest(String reqUrl, String reqBody) throws IOException, NullPointerException{

        reqUrl = Constants.BASEURL + reqUrl;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType;
        RequestBody body;

        if (reqBody == null) {
            mediaType = MediaType.parse("text/plain");
            body = RequestBody.create(mediaType, "");
        } else {
            mediaType = MediaType.parse("application/x-www-form-urlencoded");
            body = RequestBody.create(mediaType, reqBody);
        }
        Request request = new Request.Builder()
                .url(reqUrl)
                .get()
                .addHeader("x-rapidapi-key", " ") //add key
                .addHeader("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();
    }
}

