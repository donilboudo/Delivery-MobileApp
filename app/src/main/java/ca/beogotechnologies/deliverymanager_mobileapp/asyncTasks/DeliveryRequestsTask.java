package ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ca.beogotechnologies.deliverymanager_mobileapp.util.EndpointConstants;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.JsonUtil.getPostDataString;

public class DeliveryRequestsTask extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate = null;

    public DeliveryRequestsTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... args) {
        try
        {
            String clientEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/deliveryRequests";

            //10.0.2.2 is the localhost of my computer
            URL url = new URL(clientEndpointUrl);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("userSessionId", args[0]);
            postDataParams.put("userId", args[1]);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(25000 /* milliseconds */);
            conn.setConnectTimeout(25000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

            //if the server respond by 200, we will get the data and use it
            if (responseCode == HttpsURLConnection.HTTP_OK)
            {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder("");
                String line;

                while ((line = in.readLine()) != null)
                {
                    sb.append(line);
                }

                in.close();

                return sb.toString();

            }

            return "";
        }
        catch (Exception e) //can be caused by no internet connexion
        {
            Log.println(Log.ERROR, "", e.getMessage());

            return "Exception: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }
}