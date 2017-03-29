package ca.beogotechnologies.deliverymanager_mobileapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.MyDeliveriesTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;
import ca.beogotechnologies.deliverymanager_mobileapp.util.DeliveryConstants;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class MyCompletedDeliveriesActivity extends AppCompatActivity implements MyDeliveriesTask.AsyncResponse {
    private List<Delivery> deliveries;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_deliveries_history);

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
        MyDeliveriesTask deliveriesTask = new MyDeliveriesTask(this);

        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");
        String userId = sharedpreferences.getString(CURRENT_USER_ID, "");

        deliveriesTask.execute(DeliveryConstants.COMPLETED, userSessionId, userId);

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
        }
        catch (JSONException ex)
        {
            //TODO
        }
    }

    private void initListView() {
        ListView mDeliveries = (ListView) findViewById(R.id.lstDeliveries);

        mDeliveries.setAdapter(new MyDeliveriesAdapter(this, deliveries));
        mDeliveries.setTextFilterEnabled(true);

        mDeliveries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // When clicked, open delivery details and user can finish his delivery
                Delivery selectedDelivery = deliveries.get(position);

                Intent intent = new Intent(getApplicationContext(), CompletedDeliveryDetailsActivity.class);
                intent.putExtra("selectedDelivery", selectedDelivery);

                startActivity(intent);
            }
        });
    }
}
