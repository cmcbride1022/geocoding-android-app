package cmcbride.utexas.edu.geocodingapp;

import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PlotLocation extends FragmentActivity implements OnMarkerDragListener {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private ArrayList<Address> locations;
    private LatLng pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_location);

        /* get latitude and longitude of location */
        Bundle b = getIntent().getExtras();
        locations = b.getParcelableArrayList("locations");
        pos = new LatLng(locations.get(0).getLatitude(), locations.get(0).getLongitude());
        setUpMapIfNeeded(pos);

        /* listen for marker drag events; for reverse geocoding feature */
        mMap.setOnMarkerDragListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded(pos);
    }

    @Override
    public void onMarkerDragStart(Marker m) {
        m.hideInfoWindow();
    }

    @Override
    public void onMarkerDrag(Marker m) {
        /* make marker semi-transparent while dragging */
        m.setAlpha(0.5f);
    }

    @Override
    public void onMarkerDragEnd(Marker m) {
        m.setAlpha(1.0f);

        LatLng newPos = m.getPosition();
        Geocoder g = new Geocoder(getApplicationContext());

        try {
            List<Address> newAddr = g.getFromLocation(newPos.latitude, newPos.longitude, 2);
            m.setTitle(newAddr.get(0).getAddressLine(0));
            m.setSnippet("Postal code: " + newAddr.get(0).getPostalCode());
            m.showInfoWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded(LatLng pos) {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap(pos);
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap(LatLng pos) {
        mMap.addMarker(new MarkerOptions().position(pos)
                .title(locations.get(0).getAddressLine(0))
                .snippet("Postal code: " + locations.get(0).getPostalCode())
                .draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 9.0f));
    }

}
