package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.ForoAdapter;
import ec.tecAzuayM5a.cuencananandroid.adaptador.PuntosDeInteresAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;
import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;

public class EjemploActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;
    private List<Foro> foros;
    private ForoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ejemplo);

        foros = new ArrayList<>();
        adapter = new ForoAdapter(this, foros);
        ListView listView = findViewById(R.id.points_list);
        listView.setAdapter(adapter);
        try {
            fetchForo(null, null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
        try {
            fetchForo(null, null);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }
    private void fetchForo(String query, String category) throws UnsupportedEncodingException {
        String baseUrl = "http://192.168.0.123:8080/api/foros";
        StringBuilder urlBuilder = new StringBuilder(baseUrl);

        if (query != null && !query.isEmpty()) {
            urlBuilder.append("/respuesta/").append(URLEncoder.encode(query, "UTF-8"));
        }

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                urlBuilder.toString(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Foro", "Datos recibidos: " + response.toString());
                        parseForo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("PuntosDeInteres", "Error al obtener datos: " + error.toString());
                        Toast.makeText(EjemploActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonArrayRequest);
    }

    private void parseForo(JSONArray jsonArray) {
        foros.clear();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Long idForo = jsonObject.getLong("idForo");
                Long idUsuario = jsonObject.getLong("idUsuario");
                //String titulo = jsonObject.getString("titulo").trim();
                String respuesta = jsonObject.getString("respuesta");

                Foro foro = new Foro(idForo, idUsuario, respuesta);
                foros.add(foro);

            } catch (JSONException e) {
                Log.e("PuntosDeInteres", "Error parseando JSON", e);
            }
        }
    }
}
