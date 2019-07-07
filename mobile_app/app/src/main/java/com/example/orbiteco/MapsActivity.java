package com.example.orbiteco;

import androidx.fragment.app.FragmentActivity;

import android.media.Image;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Places;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;
import android.content.pm.PackageManager;
import android.util.Log;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;

import android.location.Location;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Button;
import android.content.Context;
import android.widget.TextView;
import android.os.Handler;

import org.json.*;

import com.example.orbiteco.*;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.model.Marker;

import java.io.*;
import java.net.*;
import java.util.*;
import java.lang.Math.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private WifiManager wifiManager;

    private static int DEFAULT_ZOOM = 17; // 17

    private static int tokenAmount;

    private ArrayList<User> mUsers;
    private ArrayList<Shop> mShops;

    private User mUser = null;

    private LatLng mUserPosition = new LatLng(37.563525, -122.324947);

    private boolean wifiConnected = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        createMap();

        mUsers = new ArrayList<>();
        mShops = new ArrayList<>();

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        User user1 = new User("V", "2077", "jonhny@sylverhand.ca", 5);
        User user2 = new User("Valentine", "qwerty", "valentine@sylverhand.ca", 50);
        User user3 = new User("Arthur", "12345", "kingofengland@sylverhand.ca",2);

        user1.setmInfos("Italian", 4);
        user1.setmInfos("Fast_food", 0);
        user1.setmInfos("Shop", 2);

        user2.setmInfos("Chinese", 4);
        user2.setmInfos("Shoe", 0);
        user2.setmInfos("Fast_food", 3);

        user3.setmInfos("Japanese", 4);
        user3.setmInfos("Clothing", 0);
        user3.setmInfos("Shop", 1);

        mUsers.add(user1);
        mUsers.add(user2);
        mUsers.add(user3);

        connectUser("V");

        mShops.add(new Shop("Tomatina", "Italian", R.drawable.scr8, 37.565245, -122.321162));
        mShops.add(new Shop("Jack in the Box", "Fast_food", R.drawable.scr9, 37.567172, -122.320467));
        mShops.add(new Shop("KFC", "Fast_food", R.drawable.scr6, 37.566405, -122.321158));
        mShops.add(new Shop("Himawari", "Japanese", R.drawable.scr7, 37.565957, -122.323766));
        mShops.add(new Shop("Rave Burger", "Fast_food", R.drawable.scr5, 37.564626, -122.323567));
        mShops.add(new Shop("Gyu-Kaku", "Japanese", R.drawable.scr4, 37.564669, -122.322976));

        mShops.add(new Shop("Ajisen", "Japanese", R.drawable.scr3, 37.565337, -122.322976));
        mShops.add(new Shop("Shabuway", "Japanese", R.drawable.scr11, 37.564699, -122.323958));
        mShops.add(new Shop("ABC Bakery and Cafe", "Chinese", R.drawable.scr1, 37.565188, -122.322928));

        mShops.add(new Shop("Old Town Sushi", "Japanese", R.drawable.scr7, 37.567007, -122.324134));
        mShops.add(new Shop("Chase Bank", "Bank", R.drawable.scr2, 37.563133, -122.323604));
        mShops.add(new Shop("B Street Books", "Shop", R.drawable.scr8, 37.565598, -122.322122));

        mShops.add(new Shop("Walgreens", "Shop", R.drawable.scr1, 37.565089, -122.323432));
        mShops.add(new Shop("Launderland", "Laundry", R.drawable.scr10, 37.567116, -122.319422));
        mShops.add(new Shop("Footwear etc.", "Shoe", R.drawable.scr6, 37.563833, -122.323471));
        mShops.add(new Shop("Okasyon USA", "Clothing", R.drawable.scr12, 37.563645, -122.324525));
    }

    private boolean connectUser(String username) {
        for(User user : mUsers) {
            if (user.getUsername() == username) {
                mUser = user;
                tokenAmount = user.getTokenAmount();
                setTokenTextValue(tokenAmount);
                return true;
            }
        }
        return false;
    }


    private StringBuffer getURLContent(String theURL) {
        StringBuffer buffer = new StringBuffer();
        try {
            URL url = new URL(theURL);
            InputStream is = url.openStream();
            int ptr = 0;

            while ((ptr = is.read()) != -1) {
                buffer.append((char)ptr);
            }
            is.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        return buffer;
    }


    private void sendPost(String urlStr, final String receiver_key, final String sender_key, final int amount, final String time) {
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, urlStr, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("tx_receiver", receiver_key); //Add the data you'd like to send to the server.
                MyData.put("tx_sender", sender_key);
                MyData.put("tx_amt", Integer.toString(amount));
                MyData.put("tx_type", "1");
                MyData.put("tx_time", time);
                return MyData;
            }
        };

        MyRequestQueue.add(MyStringRequest);
    }

    private void connectToWifi(String networkSSID, String networkPassword) {
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = String.format("\"%s\"", networkSSID);
        conf.preSharedKey = String.format("\"%s\"", networkPassword);

        int netId = wifiManager.addNetwork(conf);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }

    // Todo: Forget the network instead of turning the wifi down
    private void disconnectWifi(String networkSSID) {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(false);
        }

        for (WifiConfiguration tmp : wifiManager.getConfiguredNetworks()) {
            if (tmp.SSID.equals(String.format("\"%s\"", networkSSID))) {
                int netId = tmp.networkId;
                wifiManager.removeNetwork(netId);
            }
        }
    }

    private void setMarkerDragListener(GoogleMap map) {
        map.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                mUserPosition = marker.getPosition();
                computeAds();
            }
        });
    }

    private void computeAds() {
        for(Shop shop : mShops) {
            LatLng shopPosition = new LatLng(shop.mLatitude, shop.mLongitude);
            mMap.addMarker(new MarkerOptions().position(shopPosition).title(shop.mName+", "+shop.mType));
            Double distance;
            distance = Math.hypot(shopPosition.latitude - mUserPosition.latitude, shopPosition.longitude - mUserPosition.longitude);
            System.out.println("------ D: " + distance);
            mUser.computeScore(distance, shop);
        }

        Collections.sort(mShops);
        Collections.reverse(mShops);

        TextView t1= findViewById(R.id.title1);
        TextView t2= findViewById(R.id.title2);
        TextView t3= findViewById(R.id.title3);
        TextView t4= findViewById(R.id.title4);
        TextView d1= findViewById(R.id.description1);
        TextView d2= findViewById(R.id.description2);
        TextView d3= findViewById(R.id.description3);
        TextView d4= findViewById(R.id.description4);
        ImageButton i1= findViewById(R.id.img1);
        ImageButton i2= findViewById(R.id.img2);
        ImageButton i3= findViewById(R.id.img3);
        ImageButton i4= findViewById(R.id.img4);


        t1.setText(mShops.get(0).mName);
        t2.setText(mShops.get(1).mName);
        t3.setText(mShops.get(2).mName);
        t4.setText(mShops.get(3).mName);
        d1.setText(new String(mShops.get(0).mType + ", " + mShops.get(0).mPhoneNumber));
        d2.setText(new String(mShops.get(1).mType + ", " + mShops.get(1).mPhoneNumber));
        d3.setText(new String(mShops.get(2).mType + ", " + mShops.get(2).mPhoneNumber));
        d4.setText(new String(mShops.get(3).mType + ", " + mShops.get(3).mPhoneNumber));
        i1.setImageResource(mShops.get(0).mImg);
        i2.setImageResource(mShops.get(1).mImg);
        i3.setImageResource(mShops.get(2).mImg);
        i4.setImageResource(mShops.get(3).mImg);
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

        MarkerOptions markerPosUser = new MarkerOptions().position(mUserPosition).title("My position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).draggable(true);
        mMap.addMarker(markerPosUser);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(mUserPosition));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                mUserPosition, DEFAULT_ZOOM));

        setMarkerDragListener(mMap);

        for(Shop shop : mShops) {
            LatLng shopPosition = new LatLng(shop.mLatitude, shop.mLongitude);
            mMap.addMarker(new MarkerOptions().position(shopPosition).title(shop.mName+", "+shop.mType));
        }

        computeAds();
    }

    public boolean decrementToken() {
        if (tokenAmount <= 0) {
            return false;
        } else {
            tokenAmount -= 1;
            setTokenTextValue(tokenAmount);
            return true;
        }
    }

    public void setTokenTextValue(int value) {
        Button bToken = findViewById(R.id.token);
        bToken.setText(value+" OET");
    }

    private Handler handler = new Handler();


    private Runnable runnable;

    public void onButtonConnectPushed(View view) {
        Button button = findViewById(R.id.connect_button);
        if (tokenAmount == 0) {
            return;
        }
        if (!wifiConnected) {
            wifiConnected = true;
            button.setBackgroundColor(getResources().getColor(R.color.blue));
            button.setText("CONNECTED");
            connectToWifi("draper-university", "duhl3arner!");

            runnable = new Runnable() {
                @Override
                public void run() {
                    /* do what you need to do */
                    if (wifiConnected) {
                        sendPost("http://10.10.1.156:3000/transaction/new", "0x7d8ebe5087774332496ef9259a7c5518041abcbf", "0x81a774b8947679ae8deaec6492b27a134d1e85a9", 1, "null");
                        boolean hasTokens = decrementToken();
                        if (!hasTokens) {
                            broke();
                        }
                    }
                    /* and here comes the "trick" */
                    handler.postDelayed(this, 2000);
                }
            };


            handler.postDelayed(runnable, 10);
        } else {
            wifiConnected = false;
            button.setBackgroundColor(getResources().getColor(R.color.green));
            button.setText("CONNECT");
            disconnectWifi("draper-university");
            handler.removeCallbacks(runnable);
        }
    }

    public void broke() {
        Button button = findViewById(R.id.connect_button);
        button.setBackgroundColor(getResources().getColor(R.color.red));
        button.setText("BUY TOKENS");
        wifiConnected = false;
        disconnectWifi("draper-university");
        handler.removeCallbacks(runnable);
    }

    public void onAD1Click(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mShops.get(0).mLatitude, mShops.get(0).mLongitude)));
    }

    public void onAD2Click(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mShops.get(1).mLatitude, mShops.get(1).mLongitude)));
    }

    public void onAD3Click(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mShops.get(2).mLatitude, mShops.get(2).mLongitude)));
    }

    public void onAD4Click(View view) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(mShops.get(3).mLatitude, mShops.get(3).mLongitude)));
    }

    public void createMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public void onProfileClick(View view) {
        SupportMapFragment f = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (f != null)
            getSupportFragmentManager().beginTransaction().remove(f).commit();
        setContentView(R.layout.user_profile);
        ((SeekBar)findViewById(R.id.fast_food_bar)).setProgress(mUser.getmInfos("Fast_food")-1);
        ((SeekBar)findViewById(R.id.japanese_bar)).setProgress(mUser.getmInfos("Japanese")-1);
        ((SeekBar)findViewById(R.id.chinese_bar)).setProgress(mUser.getmInfos("Chinese")-1);
        ((SeekBar)findViewById(R.id.bank_bar)).setProgress(mUser.getmInfos("Bank")-1);
        ((SeekBar)findViewById(R.id.shop_bar)).setProgress(mUser.getmInfos("Shop")-1);
        ((SeekBar)findViewById(R.id.laundry_bar)).setProgress(mUser.getmInfos("Laundry")-1);
        ((SeekBar)findViewById(R.id.shoe_bar)).setProgress(mUser.getmInfos("Shoe")-1);
        ((SeekBar)findViewById(R.id.clothing_bar)).setProgress(mUser.getmInfos("Clothing")-1);
        ((SeekBar)findViewById(R.id.italian_bar)).setProgress(mUser.getmInfos("Italian")-1);

        SeekBar s = (SeekBar) findViewById(R.id.fast_food_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Fast_food", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        s = (SeekBar) findViewById(R.id.japanese_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Japanese", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.chinese_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Chinese", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.bank_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Bank", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.shop_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Shop", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.laundry_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Laundry", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.shoe_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Shoe", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.clothing_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Clothing", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        s = (SeekBar) findViewById(R.id.italian_bar);
        s.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar,
                                          int progress, boolean fromUser) {
                mUser.setmInfos("Italian", progress+1);
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}@Override public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    public void onBackClick(View view) {
        setContentView(R.layout.activity_maps);
        createMap();
        computeAds();
        setTokenTextValue(tokenAmount);
    }
}
