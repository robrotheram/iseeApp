package iseeapp.co.iseeapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;



import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by robert on 13/06/2014.
 */
public class BackgroundTask extends AsyncTask<String, Integer, Double> {


    private HttpResponse response;
    private InputStream is = null;
    private String json;
    private HttpClient client;
    private JSONObject jObj =null;
    private NetworkInterface networkFeedbackresponse;
    private List<NameValuePair> nameValuePairs;
    private HttpResponse httpResponse;

    public BackgroundTask(NetworkInterface networkFeedbackresponse,List<NameValuePair> nameValuePairs, HttpClient client){
        this.nameValuePairs = nameValuePairs;
        this.client = client;
        this.networkFeedbackresponse = networkFeedbackresponse;

    }


    @Override
    protected Double doInBackground(String... params) {
        // TODO Auto-generated method stub
        postData();
        return null;
    }

    protected void onPostExecute(Double result){

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
                Log.i("log_tag", "Line reads: " + line);
            }
            is.close();
            json = sb.toString();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            Log.d("log_tag","Raw jason ="+json);
            jObj = new JSONObject(json);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jObj = new JSONObject(json);
            networkFeedbackresponse.onPostComplete(jObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            httpResponse.getEntity().consumeContent();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    protected void onProgressUpdate(Integer... progress){

    }

    public void postData() {
        // Create a new HttpClient and Post Header

        HttpPost httppost = new HttpPost("http://iseeapp.co/api/api.php");

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httpResponse =  client.execute(httppost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();


        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

}
