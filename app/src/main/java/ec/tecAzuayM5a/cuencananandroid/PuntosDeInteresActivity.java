package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

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



public class PuntosDeInteresActivity extends AppCompatActivity {

    private List<TipoPuntoInteres> puntosDeInteres;
    private TipoPuntoInteresAdapter adapter;
    private EditText searchInput;
    private Spinner searchType;
    private TipoPuntoInteres selectedPunto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_puntos_de_interes);

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
                // Opcional: Cambiar color del elemento seleccionado
                highlightSelectedItem(listView, position);
            }
        });

        // Configurar los botones
        Button btnVerEnMapa = findViewById(R.id.btnVerEnMapa);
        Button btnAddPoint = findViewById(R.id.btnAddPoint);

        btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPunto != null) {
                    // Obtén la latitud y longitud del punto seleccionado
                    double latitud = selectedPunto.getLatitud();
                    double longitud = selectedPunto.getLongitud();

                    // Crea un URI para Google Maps
                    String uri = String.format("geo:%f,%f?q=%f,%f(%s)", latitud, longitud, latitud, longitud, selectedPunto.getNombre());

                    // Crea un Intent para abrir Google Maps
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");

                    // Verifica si hay una aplicación para manejar el Intent
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    } else {
                        Toast.makeText(PuntosDeInteresActivity.this, "Google Maps no está instalado", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PuntosDeInteresActivity.this, "Por favor selecciona un punto de interés", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PuntosDeInteresActivity.this, "Función no implementada", Toast.LENGTH_SHORT).show();
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
}
