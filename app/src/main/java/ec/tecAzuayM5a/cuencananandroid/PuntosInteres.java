package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

public class PuntosInteres extends Fragment {

    private List<TipoPuntoInteres> puntosDeInteres;
    private TipoPuntoInteresAdapter adapter;
    private EditText searchInput;
    private Spinner searchType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tipo_puntos_de_interes, container, false);

        puntosDeInteres = new ArrayList<>();
        adapter = new TipoPuntoInteresAdapter(getContext(), puntosDeInteres);
        ListView listView = rootView.findViewById(R.id.points_list);
        listView.setAdapter(adapter);

        searchInput = rootView.findViewById(R.id.search_input);
        searchType = rootView.findViewById(R.id.search_type);
        Button searchButton = rootView.findViewById(R.id.search_button);

        // Configurar los botones
        Button btnVerEnMapa = rootView.findViewById(R.id.btnVerEnMapa);
        Button btnAddPoint = rootView.findViewById(R.id.btnAddPoint);

        btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Función no implementada", Toast.LENGTH_SHORT).show();
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

        // Llamada para obtener datos de la API inicialmente
        fetchPuntosDeInteres(null, null);

        return rootView;
    }

    private void fetchPuntosDeInteres(String query, String type) {
        String baseUrl = "http://192.168.18.17:8080/api/tipospuntosinteres";
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

                Log.d("PuntosInteres", "Parseando - Nombre: " + nombre + ", Descripción: " + descripcion + ", Categoría: " + categoria);

                TipoPuntoInteres punto = new TipoPuntoInteres(id, nombre, descripcion, categoria);
                puntosDeInteres.add(punto);
            } catch (JSONException e) {
                Log.e("PuntosInteres", "Error parseando JSON", e);
            }
        }
        adapter.notifyDataSetChanged();
    }
}
