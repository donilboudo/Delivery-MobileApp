package ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks;

import android.content.Context;
import android.content.SharedPreferences;
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

import ca.beogotechnologies.deliverymanager_mobileapp.util.DeliveryConstants;
import ca.beogotechnologies.deliverymanager_mobileapp.util.EndpointConstants;
import ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.JsonUtil.getPostDataString;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ROLE;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;

public class DeliveriesTask extends AsyncTask<String, Void, String> {
    private Context context;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate = null;

    public DeliveriesTask(AsyncResponse delegate, Context context) {
        this.delegate = delegate;
        this.context = context;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... args) {
        try
        {
            SharedPreferences sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String userRole = sharedpreferences.getString(CURRENT_USER_ROLE, "");

            String clientEndpointUrl;
            if (LoginUtil.USER_ROLE_EMPLOYEE.equals(userRole))
            {
                if (DeliveryConstants.STARTED.equals(args[0]))
                {
                    clientEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/myOpenedDeliveries";
                }
                else
                {
                    clientEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/myCompletedDeliveries";
                }
            }
            else
            {
                clientEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/openedDeliveries";
            }

            //10.0.2.2 is the localhost of my computer
            URL url = new URL(clientEndpointUrl);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("userSessionId", args[1]);
            if (LoginUtil.USER_ROLE_EMPLOYEE.equals(userRole))
            {
                postDataParams.put("userId", args[2]);
            }

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