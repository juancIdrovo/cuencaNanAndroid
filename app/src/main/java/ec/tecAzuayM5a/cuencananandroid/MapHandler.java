package ec.tecAzuayM5a.cuencananandroid;

import androidx.annotation.NonNull;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapHandler implements OnMapReadyCallback {

    private GoogleMap myMap;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;


        LatLng cuenca = new LatLng(-2.9001285, -79.00589649999999);

        //Marcador
        myMap.addMarker(new MarkerOptions().position(cuenca).title("Cuenca"));


        //zoom
        float zoomLevel = 15.0f;
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cuenca, zoomLevel));
    }

    public void initializeMap(SupportMapFragment mapFragment) {
        mapFragment.getMapAsync(this);
    }
}
