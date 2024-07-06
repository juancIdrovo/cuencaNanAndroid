package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapa);

        requestQueue = Volley.newRequestQueue(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_pi);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fetchPuntosInteres();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void fetchPuntosInteres() {
        String urlPuntosInteres = "http://192.168.18.17:8080/api/puntosinteres";

        JsonArrayRequest jsonArrayRequestPuntosInteres = new JsonArrayRequest(
                Request.Method.GET,
                urlPuntosInteres,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responsePuntosInteres) {
                        Gson gson = new Gson();
                        Type listTypePuntosInteres = new TypeToken<List<PuntosDeInteres>>() {}.getType();
                        List<PuntosDeInteres> puntosInteresList = gson.fromJson(responsePuntosInteres.toString(), listTypePuntosInteres);

                        for (PuntosDeInteres puntoInteres : puntosInteresList) {
                            LatLng location = new LatLng(puntoInteres.getLatitud(), puntoInteres.getLongitud());
                            mMap.addMarker(new MarkerOptions().position(location).title(puntoInteres.getNombre()));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MapActivity.this, "Error al obtener los puntos de interés", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequestPuntosInteres);
    }
}

