package ca.beogotechnologies.deliverymanager_mobileapp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.beogotechnologies.deliverymanager_mobileapp.R;
import ca.beogotechnologies.deliverymanager_mobileapp.domain.Delivery;

class MyDeliveriesAdapter extends ArrayAdapter<Delivery> {
    private final Context context;
    private final List<Delivery> deliveries;

    MyDeliveriesAdapter(Context context, List<Delivery> deliveries) {
        super(context, R.layout.activity_my_deliveries_item, deliveries);
        this.context = context;
        this.deliveries = deliveries;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.activity_my_deliveries_item, parent, false);

        Delivery delivery = deliveries.get(position);

        TextView mSender = (TextView) rowView.findViewById(R.id.lblSender);
        String sender = mSender.getText().toString() + " " + delivery.getClient().getName();
        mSender.setText(sender);

        TextView mReceiver = (TextView) rowView.findViewById(R.id.lblReceiver);
        String receiver = mReceiver.getText().toString() + " " + delivery.getReceiver();
        mReceiver.setText(receiver);

        return rowView;
    }
}