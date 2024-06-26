package ec.tecAzuayM5a.cuencananandroid;

import static androidx.core.location.LocationManagerCompat.getCurrentLocation;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;



import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class PuntosDeInteresActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private boolean locationPermissionGranted;
    private List<TipoPuntoInteres> puntosDeInteres;
    private TipoPuntoInteresAdapter adapter;
    private EditText searchInput;
    private Spinner searchType;
    private TipoPuntoInteres selectedPunto;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_puntos_de_interes);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        puntosDeInteres = new ArrayList<>();
        adapter = new TipoPuntoInteresAdapter(this, puntosDeInteres);
        ListView listView = findViewById(R.id.points_list);
        listView.setAdapter(adapter);

        searchInput = findViewById(R.id.search_input);
        searchType = findViewById(R.id.search_type);
        Button searchButton = findViewById(R.id.search_button);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPunto = puntosDeInteres.get(position);
                highlightSelectedItem(listView, position);
                updateMap(selectedPunto);
            }
        });

        Button btnVerEnMapa = findViewById(R.id.btnVerEnMapa);

        btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPunto != null) {
                    updateMap(selectedPunto);
                } else {
                    Toast.makeText(PuntosDeInteresActivity.this, "Por favor selecciona un punto de interés", Toast.LENGTH_SHORT).show();
                }
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_pi);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString().trim();
                String selectedType = searchType.getSelectedItem().toString();
                fetchPuntosDeInteres(query, selectedType);
            }
        });

        fetchPuntosDeInteres(null, null);

        getLocationPermission();
    }

    private void highlightSelectedItem(ListView listView, int position) {
        for (int i = 0; i < listView.getChildCount(); i++) {
            if (position == i) {
                listView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            } else {
                listView.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }

    private void fetchPuntosDeInteres(String query, String type) {
        String baseUrl = "http://192.168.0.209:8080/api/tipospuntosinteres";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        if (query != null && !query.isEmpty()) {
            urlBuilder.append("/buscar?");
            try {
                switch (type) {
                    case "Nombre":
                        urlBuilder.append("nombre=").append(URLEncoder.encode(query, "UTF-8"));
                        break;
                    case "Descripción":
                        urlBuilder.append("descripcion=").append(URLEncoder.encode(query, "UTF-8"));
                        break;
                    case "Categoría":
                        urlBuilder.append("categoria=").append(URLEncoder.encode(query, "UTF-8"));
                        break;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlBuilder.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("PuntosDeInteres", "Datos recibidos: " + response.toString());
                        parsePuntosDeInteres(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PuntosDeInteres", "Error al obtener datos: " + error.toString());
                        Toast.makeText(PuntosDeInteresActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void parsePuntosDeInteres(JSONArray jsonArray) {
        puntosDeInteres.clear();
        if (jsonArray.length() == 0) {
            Toast.makeText(this, "No se encontraron puntos de interés", Toast.LENGTH_SHORT).show();
        }
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int id = jsonObject.getInt("idtipospuntosinteres");
                String nombre = jsonObject.getString("nombre").trim();
                String descripcion = jsonObject.getString("descripcion");
                String categoria = jsonObject.getString("categoria");
                JSONArray listaPuntosInteres = jsonObject.getJSONArray("listaPuntosInteres");
                double latitud = listaPuntosInteres.getJSONObject(0).getDouble("latitud");
                double longitud = listaPuntosInteres.getJSONObject(0).getDouble("longitud");

                TipoPuntoInteres punto = new TipoPuntoInteres(id, nombre, descripcion, categoria, latitud, longitud);
                puntosDeInteres.add(punto);
            } catch (JSONException e) {
                Log.e("PuntosDeInteres", "Error parseando JSON", e);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getDeviceLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                    getDeviceLocation();
                }
            }
        }
    }

    private void getDeviceLocation() {
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            Location lastKnownLocation = task.getResult();
                            if (mMap != null) {
                                LatLng currentLocation = new LatLng(lastKnownLocation.getLatitude(),
                                        lastKnownLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));

                                // Cargar el drawable personalizado
                                BitmapDescriptor customMarker = BitmapDescriptorFactory.fromResource(R.drawable.mi_ubi_pin);

                                mMap.addMarker(new MarkerOptions()
                                        .position(currentLocation)
                                        .title("Mi Ubicación")
                                        .icon(customMarker)); // Usar el drawable personalizado
                            }
                        } else {
                            Log.d("PuntosDeInteres", "Ubicación actual no disponible. Usando ubicación predeterminada.");
                            Log.e("PuntosDeInteres", "Exception: %s", task.getException());
                            LatLng defaultLocation = new LatLng(-34, 151);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));
                            mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Ubicación predeterminada"));
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("PuntosDeInteres", "Error obteniendo la ubicación: " + e.getMessage(), e);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();
    }

    private void updateMap(TipoPuntoInteres punto) {
        if (mMap != null) {
            LatLng location = new LatLng(punto.getLatitud(), punto.getLongitud());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title(punto.getNombre()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }
}

