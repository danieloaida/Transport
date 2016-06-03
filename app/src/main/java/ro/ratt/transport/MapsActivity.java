package ro.ratt.transport;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DBHandler dbHandler;
    private InitDB initDB;
    private MapHandler mapHandler;
    private DrawerLayout mDrawerLayout;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView mDrawerList;
    private GoogleMap mMap;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private List<MapStation> mapStationList;
    private List<LineInfo> lstLineAvl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapStationList = new ArrayList<MapStation>();
        lstLineAvl = new ArrayList<LineInfo>();
        dbHandler = new DBHandler(this, null, null, 1);
        initDB = new InitDB(this, dbHandler);

        initDB.StartInit();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ExpandableListView) findViewById(R.id.left_drawer);
        // Set the adapter for the list view
       // mDrawerList.setAdapter(new ArrayAdapter<String>(this,
              //  R.layout.drawer_list_item, mPlanetTitles));

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild, mapStationList);

        mDrawerList.setAdapter(listAdapter);

        mDrawerList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        mDrawerList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        mDrawerList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        mDrawerList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();

                String LineName = listDataChild.get(
                        listDataHeader.get(groupPosition)).get(
                        childPosition);

                if (lstLineAvl.contains(new LineInfo(LineName, "route1"))){
                    mapHandler.removeLineStations(LineName);
                    mapHandler.addLineStations(LineName, "route2");
                    lstLineAvl.remove(new LineInfo(LineName, "route1"));
                            lstLineAvl.add(new LineInfo(LineName, "route2"));
                    v.setBackgroundColor(Color.GREEN);
                } else {
                    if (lstLineAvl.contains(new LineInfo(LineName, "route2"))) {
                        mapHandler.removeLineStations(LineName);
                        lstLineAvl.remove(new LineInfo(LineName, "route2"));
                        v.setBackgroundColor(Color.WHITE);
                    } else {
                        mapHandler.addLineStations(LineName, "route1");
                        lstLineAvl.add(new LineInfo(LineName, "route1"));
                        v.setBackgroundColor(Color.CYAN);
                    }
                }
                return false;
            }
        });

    }


    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Tramvai");
        listDataHeader.add("Trolebuz");
        listDataHeader.add("Expres");
        listDataHeader.add("Bus");

        // Adding child data
        List<String> tram = new ArrayList<String>();
        tram.addAll(dbHandler.getListOfTransport(1));

        List<String> trolley = new ArrayList<String>();
        trolley.addAll(dbHandler.getListOfTransport(2));

        List<String> express = new ArrayList<String>();
        express.addAll(dbHandler.getListOfTransport(3));

        List<String> bus = new ArrayList<String>();
        bus.addAll(dbHandler.getListOfTransport(4));

        listDataChild.put(listDataHeader.get(0), tram); // Header, Child data
        listDataChild.put(listDataHeader.get(1), trolley);
        listDataChild.put(listDataHeader.get(2), express);
        listDataChild.put(listDataHeader.get(3), bus);
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mapHandler = new MapHandler(mapStationList,mMap,dbHandler);

        mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapt(this));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(13);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(45.755,21.229);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        mMap.animateCamera(zoom);
        try {
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException e){}

        if (mMap != null)
        {
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
            {
                @Override
                public boolean onMarkerClick(com.google.android.gms.maps.model.Marker marker)
                {
                    marker.showInfoWindow();
                    return true;
                }
            });
        }

    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            try {
                selectItem(position);
            }catch (IOException e) {
            } catch (XmlPullParserException e) {
            }
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) throws XmlPullParserException, IOException{
        
        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }



}
