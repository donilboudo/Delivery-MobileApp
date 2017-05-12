package ca.beogotechnologies.deliverymanager_mobileapp.activities.admin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.DeliveryRequest;

class DeliveryRequestAdapter extends ArrayAdapter<DeliveryRequest> {
    private final Context context;
    private final List<DeliveryRequest> deliveryRequests;

    DeliveryRequestAdapter(Context context, List<DeliveryRequest> deliveryRequests) {
        super(context, R.layout.activity_my_deliveries_item, deliveryRequests);
        this.context = context;
        this.deliveryRequests = deliveryRequests;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_admin_delivery_requests_item, parent, false);

        DeliveryRequest request = deliveryRequests.get(position);

        TextView mSender = (TextView) rowView.findViewById(R.id.lblSender);
        String senderName = mSender.getText().toString() + " " + request.getSenderName();
        mSender.setText(senderName);

        TextView mReceiver = (TextView) rowView.findViewById(R.id.lblReceiver);
        String receiver = mReceiver.getText().toString() + " " + request.getReceiverName();
        mReceiver.setText(receiver);

        return rowView;
    }
}