package com.traffic.zeeshan.scotlandtraffic;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainActivity mainActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        Resources.LoadResources(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(Resources.incidents);
        navigationView.getMenu().getItem(1).setChecked(Resources.roadworks);
        navigationView.getMenu().getItem(2).setChecked(Resources.future_roadworks);

        RssReader rssReader = new RssReader(this);
        rssReader.execute();
        mainActivityContext = this;
    }

    public void UpdateListView()
    {
        final RssData[] filteredList = PrepareRssList();

        ListAdapter myAdapter = new CustomAdapter(this, filteredList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Resources.selected_item = filteredList[position].id;
                        startActivity(new Intent(mainActivityContext, DetailsActivity.class));
                    }
                }
        );
    }

    private RssData[] PrepareRssList()
    {
        ArrayList<RssData> myData = new ArrayList<>();
        for (int i = 0; i < Resources.rssItemList.size(); i++)
        {
            if (Resources.rssItemList.get(i).type == RssData.RssDataType.INCIDENTS)
            {
                if (Resources.incidents)
                {
                    myData.add(Resources.rssItemList.get(i));
                }
            }
            else if (Resources.rssItemList.get(i).type == RssData.RssDataType.ROADWORKS)
            {
                if (Resources.roadworks)
                {
                    myData.add(Resources.rssItemList.get(i));
                }
            }
            else if (Resources.rssItemList.get(i).type == RssData.RssDataType.FUTURE_ROADWORKS)
            {
                if (Resources.future_roadworks)
                {
                    myData.add(Resources.rssItemList.get(i));
                }
            }
        }

        RssData[] filteredList = new RssData[myData.size()];
        for (int i = 0; i < filteredList.length; i++)
            filteredList[i] = myData.get(i);

        return filteredList;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId())
        {
            case R.id.incidents:
            case R.id.roadworks:
            case R.id.future_roadworks:
                if (!item.isChecked())
                    item.setChecked(true);
                else
                    item.setChecked(false);

                NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
                Resources.incidents = nav.getMenu().getItem(0).isChecked();
                Resources.roadworks = nav.getMenu().getItem(1).isChecked();
                Resources.future_roadworks = nav.getMenu().getItem(2).isChecked();
                UpdateListView();
                Resources.SaveResources(this);
                break;
            case R.id.navigation_about:
                drawer.closeDrawer(GravityCompat.START);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This app was created by Zeeshan Abid.\nThe app gets an rss feed from scotland traffic and displays roadworks, future roadworks and incidents.").setTitle("About");
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
            case R.id.navigation_refresh:
                drawer.closeDrawer(GravityCompat.START);
                RssReader rssReader = new RssReader(this);
                rssReader.execute();
                break;
            case R.id.animation:
                startActivity(new Intent(mainActivityContext, Animation.class));
                break;
        }
        return true;
    }
}
