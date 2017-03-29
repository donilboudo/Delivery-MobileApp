package ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ca.beogotechnologies.deliverymanager_mobileapp.activities.LoginActivity;
import ca.beogotechnologies.deliverymanager_mobileapp.util.EndpointConstants;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.JsonUtil.getPostDataString;

public class LoginTask extends AsyncTask<String, Void, String> {

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate = null;

    public LoginTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... args) {
        try
        {
            //10.0.2.2 is the localhost of my computer
            String loginEndpointUrl = EndpointConstants.remoteEndpointUrl + "/loginFromMobile";
            URL url = new URL(loginEndpointUrl);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("userName", args[0]);
            postDataParams.put("password", args[1]);
            Log.e("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();

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
            else
            {
                return "";
            }
        }
        catch (ConnectException e)
        {
            Log.println(Log.ERROR, "", e.getMessage());

            return LoginActivity.CONNEXION_REFUSED;
        }
        catch (Exception e)
        {
            Log.println(Log.ERROR, "", e.getMessage());

            return LoginActivity.CONNEXION_REFUSED;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        delegate.processFinish(result);
    }

}