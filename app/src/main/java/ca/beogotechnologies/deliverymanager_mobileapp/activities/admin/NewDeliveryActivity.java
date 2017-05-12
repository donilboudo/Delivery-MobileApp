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
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.activities.employee.EmployeeMenuActivity;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.ClientsTask;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.EmployeesTask;
import ca.beogotechnologies.deliverymanager_mobileapp.asyncTasks.NewDeliveryTask;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Client;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.User;

import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.CURRENT_USER_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.MyPREFERENCES;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.LoginUtil.USER_SESSION_ID;
import static ca.beogotechnologies.deliverymanager_mobileapp.util.NetworkHelper.isOnline;

public class NewDeliveryActivity extends AppCompatActivity implements ClientsTask.AsyncResponse,
        NewDeliveryTask.AsyncResponse, EmployeesTask.AsyncResponse {
    private Spinner mClients;
    private EditText mSendDate;
    private EditText mReceiverName;
    private EditText mReceiverAddress;
    private EditText mSenderReferences;
    private EditText mSenderComments;
    private EditText mReceiverNumber;

    private ProgressDialog progressDialog;

    private boolean isClientsLoad = false;
    private boolean isDeliversLoad = false;

    private List<String> clients;
    private List<User> delivers;
    private List<String> deliversNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isOnline(this))
        {
            clients = new ArrayList<>();
            delivers = new ArrayList<>();
            deliversNames = new ArrayList<>();

            loadClients();

            loadDelivers();

            progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Un moment...");
            progressDialog.show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Pas de connexion a internet", Toast.LENGTH_LONG).show();
        }
    }

    private void loadDelivers() {
        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

        //call the client async task to fetch data from server
        EmployeesTask task = new EmployeesTask(this);
        task.execute(userSessionId);
    }

    private void loadClients() {
        //get user session id in shared preferences
        SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");

        //call the client async task to fetch data from server
        ClientsTask task = new ClientsTask(this);
        task.execute(userSessionId);
    }

    private void initViews() {
        if (isClientsLoad && isDeliversLoad)
        {
            setContentView(R.layout.activity_admin_new_delivery);

            Spinner mDelivers = (Spinner) findViewById(R.id.spDeliver);

            mClients = (Spinner) findViewById(R.id.spSender);
            mSendDate = (EditText) findViewById(R.id.txtSendDate);
            mReceiverName = (EditText) findViewById(R.id.txtReceiver);
            mReceiverAddress = (EditText) findViewById(R.id.txtReceiverAddress);
            mSenderReferences = (EditText) findViewById(R.id.txtSenderReferences);
            mSenderComments = (EditText) findViewById(R.id.txtSenderComments);
            mReceiverNumber = (EditText) findViewById(R.id.txtReceiverNumber);

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.CANADA_FRENCH);
            mSendDate.setText(sdf.format(new Date()));

            ArrayAdapter<String> clientAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clients);
            clientAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mClients.setAdapter(clientAdapter);

            ArrayAdapter<String> deliverAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, deliversNames);
            deliverAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mDelivers.setAdapter(deliverAdapter);
        }
    }

    public void saveNewDelivery(View view) {
        String sendDate = mSendDate.getText().toString();
        String client = mClients.getSelectedItem().toString();
        String receiver = mReceiverName.getText().toString();
        String receiverAddress = mReceiverAddress.getText().toString();
        String senderReferences = mSenderReferences.getText().toString();
        String senderComments = mSenderComments.getText().toString();
        String receiverNumber = mReceiverNumber.getText().toString();

        if (validData(sendDate, client, receiver, receiverAddress, senderReferences, senderComments))
        {
            //get user session id in shared preferences
            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            String userSessionId = sharedpreferences.getString(USER_SESSION_ID, "");
            String userId = sharedpreferences.getString(CURRENT_USER_ID, "");

            if (!userSessionId.isEmpty() && (!sendDate.isEmpty() && !client.isEmpty() && !receiver.isEmpty()))
            {
                progressDialog = new ProgressDialog(this, R.style.DeliveryManager);
                progressDialog.setIndeterminate(true);
                progressDialog.setMessage("Un moment...");
                progressDialog.show();

                NewDeliveryTask task = new NewDeliveryTask(this);
                task.execute(userSessionId,
                        userId,
                        client,
                        sendDate,
                        receiver,
                        receiverAddress,
                        senderReferences,
                        senderComments,
                        receiverNumber);
            }
        }
    }

    private boolean validData(String sendDate, String client, String receiver, String receiverAddress, String senderReferences, String senderComments) {
        boolean isValid = true;

        if (receiver == null || receiver.isEmpty())
        {
            mReceiverName.setError("Ne peut pas etre vide");
            isValid = false;
        }

        return isValid;
    }

    public void cancel(View view) {
        Intent intent = new Intent(getApplicationContext(), AdminMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void processFinish(String result) {
        if (result != null && !result.isEmpty())
        {
            progressDialog.dismiss();

            if (result.equals(Integer.toString(HttpsURLConnection.HTTP_ACCEPTED)))
            {
                Toast.makeText(getApplicationContext(), "Nouvelle livraison bien crée", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), EmployeeMenuActivity.class);
                startActivity(intent);
            }
        }
        else
        {
            //Toast
            Toast.makeText(getApplicationContext(), "Problème survenue lors de la création de la demande", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void getClientsProcessFinish(String result) {
        //here we receive the result from the server. Because we receive a json string, we need to parse it
        //and store the result in the clients collection to be use by the spinner
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
                        Client client = new Gson().fromJson(object.toString(), Client.class);
                        this.clients.add(client.getName());
                    }
                    catch (Exception ex)
                    {
                        //TODO log errors
                    }

                }

                //now we will show the activity
                progressDialog.dismiss();

                isClientsLoad = true;

                initViews();
            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
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
                        User deliver = new Gson().fromJson(object.toString(), User.class);
                        this.deliversNames.add(deliver.getFirstName() + " " + deliver.getLastName());
                        this.delivers.add(deliver);
                    }
                    catch (Exception ex)
                    {
                        //TODO log errors
                    }

                }

                //now we will show the activity
                progressDialog.dismiss();

                isDeliversLoad = true;

                initViews();
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
