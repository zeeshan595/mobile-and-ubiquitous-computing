package com.traffic.zeeshan.scotlandtraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private String marker_title, snippet;
    private float lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final DetailsActivity myContext = this;
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(myContext, MainActivity.class));
            }
        });

        TextView title = (TextView) findViewById(R.id.textview_title);
        TextView description = (TextView) findViewById(R.id.textview_description);
        TextView link = (TextView) findViewById(R.id.textview_link);
        TextView date = (TextView) findViewById(R.id.textview_date);

        RssData selected_item = Resources.rssItemList.get(Resources.selected_item);
        title.setText(selected_item.title);
        description.setText(selected_item.description);
        link.setText(selected_item.link);
        date.setText(selected_item.date);

        //setup for google maps
        marker_title = selected_item.title;
        snippet = selected_item.type.toString();
        lat = selected_item.geo[0];
        lng = selected_item.geo[1];

        //Google Maps Api
        SupportMapFragment mapFragment = (SupportMapFragment)this.getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map)
    {

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setTrafficEnabled(true);
        map.getUiSettings().setAllGesturesEnabled(false);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(lat, lng))
                .title(marker_title)
                .snippet(snippet)
        );
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
    }
}
