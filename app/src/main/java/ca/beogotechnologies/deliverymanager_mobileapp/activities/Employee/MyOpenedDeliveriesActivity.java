package ca.beogotechnologies.deliverymanager_mobileapp.activities.employee;

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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.activities.common.DeliveriesAdapter;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.DeliveriesTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;
import ca.beogotechnologies.deliverymanager_mobileapp.util.DeliveryConstants;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class MyOpenedDeliveriesActivity extends AppCompatActivity implements DeliveriesTask.AsyncResponse {
    private ProgressDialog progressDialog;
    private List<Delivery> deliveries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deliveries);

        deliveries = new ArrayList<>();

        if (isOnline(this))
        {
            loadMyDeliveries();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private void loadMyDeliveries() {
        DeliveriesTask deliveriesTask = new DeliveriesTask(this, getApplicationContext());

        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");
        String userId = sharedpreferences.getString(CURRENT_USER_ID, "");

        deliveriesTask.execute(DeliveryConstants.STARTED, userSessionId, userId);

        progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Un moment...");
        progressDialog.show();
    }

    private void initListView() {
        ListView mDeliveries = (ListView) findViewById(R.id.lstDeliveries);

        mDeliveries.setAdapter(new DeliveriesAdapter(this, deliveries));
        mDeliveries.setTextFilterEnabled(true);

        mDeliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, open delivery details and user can finish his delivery
                Delivery selectedDelivery = deliveries.get(position);

                Intent intent;
                if (DeliveryConstants.STARTED.equals(selectedDelivery.getStatus()))
                {
                    intent = new Intent(getApplicationContext(), CompleteDeliveryActivity.class);
                    intent.putExtra("selectedDelivery", selectedDelivery);
                }
                else
                {
                    intent = new Intent(getApplicationContext(), StartDeliveryActivity.class);
                    intent.putExtra("selectedDelivery", selectedDelivery);
                }

                startActivity(intent);
            }
        });
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
                        Delivery delivery = new Gson().fromJson(object.toString(), Delivery.class);
                        this.deliveries.add(delivery);
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
                Toast.makeText(getApplicationContext(), "Pas de livraison encore", Toast.LENGTH_SHORT).show();
            }
        }
        catch (JSONException ex)
        {
            //TODO
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), EmployeeMenuActivity.class);
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
