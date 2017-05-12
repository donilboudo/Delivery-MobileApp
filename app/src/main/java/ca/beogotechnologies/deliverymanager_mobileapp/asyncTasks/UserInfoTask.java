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

/**
 * Created by fabrice on 2017-03-14.
 */

public class UserInfoTask extends AsyncTask<String, Void, String> {
    private static final String updateUserEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/updateUser";

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private UserInfoTask.AsyncResponse delegate = null;

    public UserInfoTask(UserInfoTask.AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... args) {
        try
        {
            //10.0.2.2 is the localhost of my computer
            URL url = new URL(updateUserEndpointUrl);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("userSessionId", args[0]);
            postDataParams.put("userId", args[1]);
            postDataParams.put("email", args[2]);
            postDataParams.put("phone", args[3]);
            postDataParams.put("userName", args[4]);
            postDataParams.put("password", args[5]);

            Log.e("params", postDataParams.toString());

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(25000 /* milliseconds */);
            conn.setConnectTimeout(25000 /* milliseconds */);
            conn.setRequestMethod("POST");
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

            return "";
        }
        catch (Exception e)
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
