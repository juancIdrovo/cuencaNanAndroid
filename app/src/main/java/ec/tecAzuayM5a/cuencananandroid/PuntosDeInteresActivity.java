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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import ec.tecAzuayM5a.cuencananandroid.ip.ip;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.tecAzuayM5a.cuencananandroid.adaptador.PuntosDeInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foto;
import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;
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
    private List<PuntosDeInteres> puntosDeInteres;
    private PuntosDeInteresAdapter adapter;
    private EditText searchInput;
    private Spinner searchType;
    private PuntosDeInteres selectedPunto;
    private GoogleMap mMap;
    private List<TipoPuntoInteres> tipoPuntosInteresList;
    private ArrayAdapter<TipoPuntoInteres> tipoPuntoAdapter;
    private ListView pointsListView;
    ip ipo = new ip();
    String direccion = ipo.getIp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_puntos_de_interes);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        puntosDeInteres = new ArrayList<>();
        adapter = new PuntosDeInteresAdapter(this, puntosDeInteres);
        ListView listView = findViewById(R.id.points_list);
        listView.setAdapter(adapter);

        searchInput = findViewById(R.id.search_input);
        searchType = findViewById(R.id.search_type);
        Button searchButton = findViewById(R.id.search_button);

        Button buttonMapa = findViewById(R.id.button_mapa);
        Button buttonPuntos = findViewById(R.id.button_puntos);

        Button buttonEventos = findViewById(R.id.button_eventos);
        Button buttonForo = findViewById(R.id.button_foro);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPunto = puntosDeInteres.get(position);
                highlightSelectedItem(listView, position);
                updateMap(selectedPunto);
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
                try {
                    fetchPuntosDeInteres(query, selectedType);
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        ///btns nav///

        buttonForo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PuntosDeInteresActivity.this, ForoActivity.class));

            }
        });

        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PuntosDeInteresActivity.this, MapActivity.class));

            }
        });


        buttonPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PuntosDeInteresActivity.this, PuntosDeInteresActivity.class));

            }
        });

        buttonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PuntosDeInteresActivity.this, EventosActivity.class));

            }
        });

        try {
            fetchPuntosDeInteres(null, null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

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

    private void fetchPuntosDeInteres(String query, String category) throws UnsupportedEncodingException {
        String baseUrl = direccion +"/puntosinteres";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        if (query != null && !query.isEmpty()) {
            urlBuilder.append("/nombre/").append(URLEncoder.encode(query, "UTF-8"));
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
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                getDeviceLocation();
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
        Log.d("PuntosDeInteres", "Google Map está listo");
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();
    }

    public void updateMap(PuntosDeInteres punto) {
        Log.d("PuntosDeInteres", "Actualizando el mapa con el punto de interés: " + punto.getNombre());
        if (mMap != null) {
            LatLng location = new LatLng(punto.getLatitud(), punto.getLongitud());
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(location).title(punto.getNombre()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

    private void fetchCategorias(final List<PuntosDeInteres> puntos) {
        String url = direccion +"/tipospuntosinteres";

        RequestQueue queue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("PuntosDeInteres", "Categorías recibidas: " + response.toString());
                        Map<Long, String> categorias = new HashMap<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Long idTipoPuntoInteres = jsonObject.getLong("idtipospuntosinteres");
                                String categoria = jsonObject.getString("categoria");
                                categorias.put(idTipoPuntoInteres, categoria);
                            } catch (JSONException e) {
                                Log.e("PuntosDeInteres", "Error parseando JSON de categorías", e);
                            }
                        }
                        for (PuntosDeInteres punto : puntos) {
                            punto.setCategoria(categorias.get(punto.getIdTipoPuntoInteres()));
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PuntosDeInteres", "Error al obtener categorías: " + error.toString());
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void parsePuntosDeInteres(JSONArray jsonArray) {
        puntosDeInteres.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLong("id");
                Long idAdministrador = jsonObject.getLong("idAdministrador");
                Long idTipoPuntoInteres = jsonObject.getLong("idTipoPuntoInteres");
                Long idFoto = jsonObject.getLong("idFoto");
                String nombre = jsonObject.getString("nombre").trim();
                double latitud = jsonObject.getDouble("latitud");
                double longitud = jsonObject.getDouble("longitud");
                String descripcion = jsonObject.getString("descripcion");

                PuntosDeInteres punto = new PuntosDeInteres(id, idAdministrador, idTipoPuntoInteres, idFoto, nombre, latitud, longitud, null, descripcion);
                puntosDeInteres.add(punto);

                fetchFoto(idFoto, punto); // Llamar a fetchFoto para cargar la URL de la foto
            } catch (JSONException e) {
                Log.e("PuntosDeInteres", "Error parseando JSON", e);
            }
        }
        fetchCategorias(puntosDeInteres);
    }

    private void fetchFoto(Long idFoto, PuntosDeInteres punto) {
        String url = direccion + "/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("foto");
                        punto.setFoto(new Foto(idFoto, fotoUrl));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("PuntosDeInteres", "Error al obtener la foto: " + error.toString())
        );
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}



