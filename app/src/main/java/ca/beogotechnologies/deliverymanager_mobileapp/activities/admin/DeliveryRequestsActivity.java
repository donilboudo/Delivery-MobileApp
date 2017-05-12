package ca.beogotechnologies.deliverymanager_mobileapp.activities.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.DeliveryRequestsTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.DeliveryRequest;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class DeliveryRequestsActivity extends AppCompatActivity implements DeliveryRequestsTask.AsyncResponse {
    private List<DeliveryRequest> deliveryRequets;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delivery_requests);

        deliveryRequets = new ArrayList<>();

        if (isOnline(this))
        {
            loadDeliveryRequests();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private void loadDeliveryRequests() {
        DeliveryRequestsTask task = new DeliveryRequestsTask(this);

        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");
        String userId = sharedpreferences.getString(CURRENT_USER_ID, "");

        task.execute(userSessionId, userId);

        progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Un moment...");
        progressDialog.show();
    }

    @Override
    public void processFinish(String result) {
        if (result != null && !result.isEmpty())
        {
            progressDialog.dismiss();

            populateDeliveries(result);
        }
    }

    private void populateDeliveries(String result) {
        try
        {
            JSONArray jsonArray = new JSONArray(result);
            if (jsonArray.length() > 0)
            {
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject object = jsonArray.getJSONObject(i);
                    try
                    {
                        String id = object.getString("id");

                        Date requestDate;
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.FRENCH);
                        long requestDateMillistones = object.getLong("requestDate");
                        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("Canada/Central"));
                        calendar.setTimeInMillis(requestDateMillistones);

                        requestDate = calendar.getTime();

                        String senderName = object.getString("senderName");
                        String senderNumber = object.getString("senderNumber");
                        String senderAddress = object.getString("senderAddress");
                        String receiverName = object.getString("receiverName");
                        String receiverAddress = object.getString("receiverAddress");
                        String senderComments = object.getString("senderComments");
                        String status = object.getString("status");
                        String packageType = object.getString("packageType");

                        DeliveryRequest request = new DeliveryRequest();
                        request.setId(id);
                        request.setRequestDate(requestDate);
                        request.setSenderName(senderName);
                        request.setSenderNumber(senderNumber);
                        request.setSenderAddress(senderAddress);
                        request.setSenderComments(senderComments);
                        request.setReceiverName(receiverName);
                        request.setReceiverAddress(receiverAddress);
                        request.setPackageType(packageType);
                        request.setStatus(status);
                        request.setDeliverId(null);
                        request.setDeliverName(null);
                        request.setDeliverNumber(null);

                        this.deliveryRequets.add(request);
                    }
                    catch (JsonSyntaxException ex)
                    {
                        //TODO log errors
                    }

                }

                initListView();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Pas de demandes de livraison", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException ex)
        {
            //TODO
        }
    }

    private void initListView() {
        ListView mDeliveries = (ListView) findViewById(R.id.lstDeliveries);

        mDeliveries.setAdapter(new DeliveryRequestAdapter(this, deliveryRequets));
        mDeliveries.setTextFilterEnabled(true);

        mDeliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, open delivery details and user can finish his delivery
                DeliveryRequest selectedDelivery = deliveryRequets.get(position);

                Intent intent = new Intent(getApplicationContext(), AssignDeliveryRequestActivity.class);
                intent.putExtra("selectedRequest", selectedDelivery);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            //kill activity
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
