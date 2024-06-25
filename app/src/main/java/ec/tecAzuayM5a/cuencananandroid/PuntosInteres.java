package ec.tecAzuayM5a.cuencananandroid;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

public class PuntosInteres extends Fragment implements OnMapReadyCallback {

    private List<TipoPuntoInteres> puntosDeInteres;
    private TipoPuntoInteresAdapter adapter;
    private EditText searchInput;
    private Spinner searchType;
    private TipoPuntoInteres selectedPunto;
    private GoogleMap myMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_puntos_interes, container, false);

        puntosDeInteres = new ArrayList<>();
        adapter = new TipoPuntoInteresAdapter(getContext(), puntosDeInteres);
        ListView listView = rootView.findViewById(R.id.points_list);
        listView.setAdapter(adapter);

        searchInput = rootView.findViewById(R.id.search_input);
        searchType = rootView.findViewById(R.id.search_type);
        Button searchButton = rootView.findViewById(R.id.search_button);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedPunto = puntosDeInteres.get(position);
                highlightSelectedItem(listView, position);
            }
        });

        // Configurar los botones
        Button btnVerEnMapa = rootView.findViewById(R.id.btnVerEnMapa);
        Button btnAddPoint = rootView.findViewById(R.id.btnAddPoint);

        btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPunto != null) {
                    // Obtén la latitud y longitud del punto seleccionado
                    double latitud = selectedPunto.getLatitud();
                    double longitud = selectedPunto.getLongitud();
                    String nombre = selectedPunto.getNombre();

                    // Crea un objeto LatLng con la nueva ubicación
                    LatLng nuevaUbicacion = new LatLng(latitud, longitud);

                    // Actualiza la ubicación del mapa
                    updateMapLocation(nuevaUbicacion, nombre);
                } else {
                    Toast.makeText(getContext(), "Por favor selecciona un punto de interés", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Función no implementada", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el botón de búsqueda
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = searchInput.getText().toString().trim();
                String selectedType = searchType.getSelectedItem().toString();
                fetchPuntosDeInteres(query, selectedType);
            }
        });

        // Inicializa el fragmento del mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.nav_fragmentmapa);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Llamada para obtener datos de la API inicialmente
        fetchPuntosDeInteres(null, null);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;

        // Set initial location to Cuenca
        LatLng cuenca = new LatLng(-2.9001285, -79.00589649999999);
        updateMapLocation(cuenca, "Cuenca");
    }

    private void updateMapLocation(LatLng latLng, String title) {
        if (myMap != null) {
            myMap.clear(); // Clear existing markers
            myMap.addMarker(new MarkerOptions().position(latLng).title(title));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
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

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlBuilder.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("PuntosInteres", "Datos recibidos: " + response.toString());
                        parsePuntosDeInteres(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PuntosInteres", "Error al obtener datos: " + error.toString());
                        Toast.makeText(getContext(), "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void parsePuntosDeInteres(JSONArray jsonArray) {
        puntosDeInteres.clear();
        if (jsonArray.length() == 0) {
            Toast.makeText(getContext(), "No se encontraron puntos de interés", Toast.LENGTH_SHORT).show();
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
                Log.e("PuntosInteres", "Error parseando JSON", e);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
