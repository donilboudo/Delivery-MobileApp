package ca.beogotechnologies.deliverymanager_mobileapp.activities.admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.AssignDeliveryRequestTask;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.EmployeesTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.DeliveryRequest;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.User;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;

public class AssignDeliveryRequestActivity extends AppCompatActivity implements EmployeesTask.AsyncResponse, AssignDeliveryRequestTask.AsyncResponse {
    private DeliveryRequest deliveryRequest;
    private ProgressDialog progressDialog;
    private List<String> employeeNames;
    private Spinner mEmployees;
    private List<User> employees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_request_details);

        //get selected delivery request in extra data
        Intent intent = getIntent();
        deliveryRequest = (DeliveryRequest) intent.getSerializableExtra("selectedRequest");

        if (deliveryRequest != null)
        {
            this.employees = new ArrayList<>();
            this.employeeNames = new ArrayList<>();

            loadEmployees();

            progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Un moment...");
            progressDialog.show();
        }
    }

    private void loadEmployees() {
        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

        //call the client async task to fetch data from server
        EmployeesTask task = new EmployeesTask(this);
        task.execute(userSessionId);
    }

    private void initViews() {
        EditText mRequestDate = (EditText) findViewById(R.id.txtRequestDate);
        EditText mSenderName = (EditText) findViewById(R.id.txtSenderName);
        EditText mSenderNumber = (EditText) findViewById(R.id.txtSenderNumber);
        EditText mSenderAddress = (EditText) findViewById(R.id.txtSenderAddress);
        EditText mSenderComments = (EditText) findViewById(R.id.txtSenderComments);
        EditText mReceiverName = (EditText) findViewById(R.id.txtReceiverName);
        EditText mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);
        mEmployees = (Spinner) findViewById(R.id.spDeliver);

        mRequestDate.setText(deliveryRequest.getRequestDate().toString());
        mSenderName.setText(deliveryRequest.getSenderName());
        mSenderNumber.setText(deliveryRequest.getSenderNumber());
        mSenderAddress.setText(deliveryRequest.getSenderAddress());
        mSenderComments.setText(deliveryRequest.getSenderComments());
        mReceiverName.setText(deliveryRequest.getReceiverName());
        mReceiverAddress.setText(deliveryRequest.getReceiverAddress());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, employeeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mEmployees.setAdapter(adapter);
    }

    public void assignRequest(View view) {
        int item = mEmployees.getSelectedItemPosition();
        User employee = employees.get(item);

        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

        AssignDeliveryRequestTask task = new AssignDeliveryRequestTask(this);
        task.execute(userSessionId, deliveryRequest.getId(), employee.getId());


        Intent intent = new Intent(getApplicationContext(), DeliveryRequestsActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), DeliveryRequestsActivity.class);
        startActivity(intent);
    }

    @Override
    public void getEmployeesProcessFinish(String result) {
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
                        User employee = new Gson().fromJson(object.toString(), User.class);
                        this.employeeNames.add(employee.getFirstName() + " " + employee.getLastName());
                        this.employees.add(employee);
                    }
                    catch (Exception ex)
                    {
                        //TODO log errors
                    }

                }

                //now we will show the activity
                progressDialog.dismiss();

                initViews();
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void processFinish(String output) {

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), DeliveryRequestsActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            onBackPressed();
            //kill the activity
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
