package com.mapintegration.ionichms;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.location.FusedLocationProviderClient;
import com.huawei.hms.location.LocationServices;
import com.huawei.hms.maps.CameraUpdate;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.MapView;
import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;
import com.huawei.hms.maps.util.LogM;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapViewDemoActivity";

    private Marker mMarker;

    private FusedLocationProviderClient mFusedLocationProviderClient;

    private static final int REQUEST_CODE = 1;
    private HuaweiMap hMap;
    private MapView mMapView;
    private static final String[] RUNTIME_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET
    };

    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogM.d(TAG, "onCreate:hzj");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (!hasPermissions(this, RUNTIME_PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, RUNTIME_PERMISSIONS, REQUEST_CODE);
        }

        mMapView = findViewById(R.id.mapView);
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        //get map instance
        mMapView.getMapAsync(this);
        setLastLocation();
    }

    @Override
    public void onMapReady(HuaweiMap map) {
        //get map instance in a callback method
        hMap = map;
        hMap.setMyLocationEnabled(true);// Enable the my-location overlay.
        hMap.getUiSettings().setMyLocationButtonEnabled(true);// Enable the my-location icon.
        Marker mMarker;
        mMarker = hMap.addMarker(new MarkerOptions()
                        .position(new LatLng(19.432680, -99.134210)));
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void setLastLocation() {
        try {
            Task<Location> lastLocation = mFusedLocationProviderClient.getLastLocation();
            lastLocation.addOnSuccessListener(location -> {
                if (location == null) {
                    Log.i(TAG, "setLastLocation got null location");
                    return;
                }
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                LatLng latLng1 = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdateLocation = CameraUpdateFactory.newLatLng(latLng1);
                float zoom = 16.0f;
                CameraUpdate cameraUpdateZoom = CameraUpdateFactory.zoomTo(zoom);
                hMap.moveCamera(cameraUpdateLocation);
                hMap.moveCamera(cameraUpdateZoom);
            }).addOnFailureListener(e -> Log.e(TAG, "setLastLocation onFailure:" + e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, "setLastLocation exception:" + e.getMessage());
        }
    }

}
