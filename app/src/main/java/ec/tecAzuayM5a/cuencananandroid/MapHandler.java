package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import androidx.annotation.NonNull;

public class MapHandler extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_map); // Apunta al layout correcto que contiene el mapa

        // Encuentra el fragmento del mapa y pásaselo a esta Activity
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;

        LatLng cuenca = new LatLng(-2.9001285, -79.00589649999999);

        // Marcador
        myMap.addMarker(new MarkerOptions().position(cuenca).title("Cuenca"));

        // Zoom
        float zoomLevel = 15.0f;
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cuenca, zoomLevel));
    }
}