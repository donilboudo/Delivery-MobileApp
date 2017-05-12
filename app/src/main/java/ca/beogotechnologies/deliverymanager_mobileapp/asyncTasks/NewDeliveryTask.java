package ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import ca.beogotechnologies.deliverymanager_mobileapp.util.EndpointConstants;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.JsonUtil.getPostDataString;

public class NewDeliveryTask extends AsyncTask<String, Void, String> {
    private static final String loginEndpointUrl = EndpointConstants.remoteEndpointUrl + "/mobile/newDelivery";

    public interface AsyncResponse {
        void processFinish(String output);
    }

    private AsyncResponse delegate = null;

    public NewDeliveryTask(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    protected void onPreExecute() {

    }

    protected String doInBackground(String... args) {
        try
        {
            //10.0.2.2 is the localhost of my computer
            URL url = new URL(loginEndpointUrl);

            JSONObject postDataParams = new JSONObject();
            postDataParams.put("userSessionId", args[0]);
            postDataParams.put("employeeId", args[1]);
            postDataParams.put("clientName", args[2]);
            postDataParams.put("sendDate", args[3]);
            postDataParams.put("receiverName", args[4]);
            postDataParams.put("receiverAddress", args[5]);
            postDataParams.put("senderReferences", args[6]);
            postDataParams.put("senderComments", args[7]);
            postDataParams.put("receiverNumber", args[8]);


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

            if (responseCode == HttpsURLConnection.HTTP_ACCEPTED)
            {
                return "" + responseCode;

            }
            else
            {
                return "";
            }
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