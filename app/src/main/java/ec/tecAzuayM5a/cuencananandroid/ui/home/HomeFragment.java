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
import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private FragmentHomeBinding binding;
    private List<PuntosDeInteres> puntosDeInteres;
    private GoogleMap myMap;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        puntosDeInteres = new ArrayList<>();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_container);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager().beginTransaction().replace(R.id.map_container, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);

        fetchPuntosDeInteres();

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

    private void fetchPuntosDeInteres() {
        String url = "http://192.168.100.196:8080/api/puntosinteres";

        RequestQueue queue = Volley.newRequestQueue(getContext());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("PuntosDeInteres", "Datos recibidos: " + response.toString());
                        parsePuntosDeInteres(response);
                        updateMapMarkers();
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
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int idPuntoInteres = jsonObject.getInt("idPuntoInteres");
                String nombre = jsonObject.getString("nombre").trim();
                double latitud = jsonObject.getDouble("latitud");
                double longitud = jsonObject.getDouble("longitud");

                PuntosDeInteres punto = new PuntosDeInteres(idPuntoInteres, nombre, latitud, longitud);
                puntosDeInteres.add(punto);
            } catch (JSONException e) {
                Log.e("PuntosDeInteres", "Error parseando JSON", e);
            }
        }
    }

    private void updateMapMarkers() {
        if (myMap != null) {
            myMap.clear();
            for (PuntosDeInteres punto : puntosDeInteres) {
                LatLng location = new LatLng(punto.getLatitud(), punto.getLongitud());
                myMap.addMarker(new MarkerOptions().position(location).title(punto.getNombre()));
            }
            if (!puntosDeInteres.isEmpty()) {
                LatLng firstLocation = new LatLng(puntosDeInteres.get(0).getLatitud(), puntosDeInteres.get(0).getLongitud());
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 12));
            }
        }
    }

    private void updateMapLocation(LatLng latLng, String title) {
        if (myMap != null) {
            myMap.addMarker(new MarkerOptions().position(latLng).title(title));
            myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.0f));
        }
    }
}





