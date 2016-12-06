package com.traffic.zeeshan.scotlandtraffic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<RssData>{
    CustomAdapter(Context context, RssData[] resource)
    {

        super(context, R.layout.custom_row, resource);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater myInflater = LayoutInflater.from(getContext());
        View customView = myInflater.inflate(R.layout.custom_row, parent, false);

        RssData singleItem = getItem(position);
        TextView myText = (TextView) customView.findViewById(R.id.customText);
        ImageView myImage = (ImageView) customView.findViewById(R.id.customImage);

        myText.setText(singleItem.title);
        switch (singleItem.type) {
            case INCIDENTS:
                myImage.setImageResource(R.drawable.icon_incidents);
                break;
            case ROADWORKS:
                myImage.setImageResource(R.drawable.icon_roadworks);
                break;
            case FUTURE_ROADWORKS:
                myImage.setImageResource(R.drawable.icon_future_roadworks);
                break;
        }

        return customView;
    }
}