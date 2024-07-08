package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
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

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

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

        Button buttonMapa = findViewById(R.id.button_mapa);
        Button buttonPuntos = findViewById(R.id.button_puntos);
        Button buttonCasa = findViewById(R.id.button_casa);
        Button buttonEventos = findViewById(R.id.button_eventos);
        Button buttonForo = findViewById(R.id.button_foro);

       // buttonForo.setOnClickListener(new View.OnClickListener() {
         //   @Override
         //   public void onClick(View view) {

           //     startActivity(new Intent(MapActivity.this, RatePuntoDeInteresActivity.class));

          //  }
       // });

        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapActivity.this, MapActivity.class));

            }
        });

        buttonCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapActivity.this, PerfilUsuarioActivity.class));

            }
        });

        buttonPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapActivity.this, PuntosDeInteresActivity.class));

            }
        });

        buttonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(MapActivity.this, EventosActivity.class));

            }
        });



    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Hacer zoom en el punto específico
        LatLng cuencaLocation = new LatLng(-2.8974172, -79.0044893);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cuencaLocation, 15)); // Zoom level 15, ajusta según sea necesario
    }

    private void fetchPuntosInteres() {
        String urlPuntosInteres = "http://192.168.0.111:8080/api/puntosinteres";

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


