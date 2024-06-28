package ec.tecAzuayM5a.cuencananandroid.ui.home;

import android.graphics.Color;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.databinding.FragmentHomeBinding;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

/*public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
        private List<TipoPuntoInteres> puntosDeInteres;
        private TipoPuntoInteresAdapter adapter;
        private EditText searchInput;
        private Spinner searchType;
        private TipoPuntoInteres selectedPunto;
        private GoogleMap myMap;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

            binding = FragmentHomeBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            puntosDeInteres = new ArrayList<>();
            adapter = new TipoPuntoInteresAdapter(getContext(), puntosDeInteres);
            ListView listView = binding.pointsList;
            listView.setAdapter(adapter);

            searchInput = binding.searchInput;
            searchType = binding.searchType;
            Button searchButton = binding.searchButton;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedPunto = puntosDeInteres.get(position);
                    highlightSelectedItem(listView, position);
                    updateMap(selectedPunto);
                }
            });

            Button btnVerEnMapa = binding.btnVerEnMapa;
            btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selectedPunto != null) {
                        updateMap(selectedPunto);
                    } else {
                        Toast.makeText(getContext(), "Por favor selecciona un punto de interés", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
            if (mapFragment == null) {
                mapFragment = SupportMapFragment.newInstance();
                getChildFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment).commit();
            }
            mapFragment.getMapAsync(this);

            searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String query = searchInput.getText().toString().trim();
                    String selectedType = searchType.getSelectedItem().toString();
                    fetchPuntosDeInteres(query, selectedType);
                }
            });

            fetchPuntosDeInteres(null, null);

            return root;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            binding = null;
        }

        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            myMap = googleMap;
            LatLng cuenca = new LatLng(-2.9001285, -79.00589649999999);
            updateMapLocation(cuenca, "Cuenca");
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
            String baseUrl = "http://192.168.0.123:8080/api/tipospuntosinteres";
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
                            Log.d("PuntosDeInteres", "Datos recibidos: " + response.toString());
                            parsePuntosDeInteres(response);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("PuntosDeInteres", "Error al obtener datos: " + error.toString());
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
                    Log.e("PuntosDeInteres", "Error parseando JSON", e);
                }
            }
            adapter.notifyDataSetChanged();
        }

        private void updateMap(TipoPuntoInteres punto) {
            if (myMap != null) {
               LatLng location = new LatLng(punto.getLatitud(), punto.getLongitud());
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(location).title(punto.getNombre()));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            }
        }

        private void updateMapLocation(LatLng latLng, String title) {
            if (myMap != null) {
                myMap.clear();
                myMap.addMarker(new MarkerOptions().position(latLng).title(title));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
            }
        }
    }
*/



