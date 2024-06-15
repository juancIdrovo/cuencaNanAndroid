package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.TipoPuntoInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

public class PuntosDeInteresActivity extends AppCompatActivity {

    private List<TipoPuntoInteres> puntosDeInteres;
    private TipoPuntoInteresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tipo_puntos_de_interes);

        puntosDeInteres = new ArrayList<>();
        adapter = new TipoPuntoInteresAdapter(this, puntosDeInteres);

        ListView listView = findViewById(R.id.points_list);
        listView.setAdapter(adapter);

        Button btnVerEnMapa = findViewById(R.id.btnVerEnMapa);
        Button btnAddPoint = findViewById(R.id.btnAddPoint);

        btnVerEnMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para ver en el mapa
                Toast.makeText(PuntosDeInteresActivity.this, "Función no implementada", Toast.LENGTH_SHORT).show();
            }
        });

        btnAddPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para añadir un punto de interés
                Toast.makeText(PuntosDeInteresActivity.this, "Función no implementada", Toast.LENGTH_SHORT).show();
            }
        });

        fetchPuntosDeInteres();
    }

    private void fetchPuntosDeInteres() {
        String url = "http://192.168.18.17:8080/api/tipospuntosinteres";
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parsePuntosDeInteres(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PuntosDeInteresActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void parsePuntosDeInteres(JSONArray jsonArray) {
        puntosDeInteres.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long id = jsonObject.getLong("idtipospuntosinteres");
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                String categoria = jsonObject.getString("categoria");

                Log.d("PuntosDeInteres", "Nombre: " + nombre + ", Descripción: " + descripcion + ", Categoría: " + categoria);

                TipoPuntoInteres punto = new TipoPuntoInteres(id, nombre, descripcion, categoria);
                puntosDeInteres.add(punto);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
