package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.ForoAdapter;
import ec.tecAzuayM5a.cuencananandroid.adaptador.ForoUsuarioAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foto;

public class ForoUsuarioActivity extends AppCompatActivity {
    private ListView listView;
    private List<Foro> foros;
    private ForoUsuarioAdapter adapter;
    private Button btnpublicar;
    private String long_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forousuario);

        foros = new ArrayList<>();
        adapter = new ForoUsuarioAdapter(this, foros);
        listView = findViewById(R.id.points_list);
        listView.setAdapter(adapter);
        long_id = getIntent().getStringExtra("id_usuario");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Aquí puedes manejar la selección de un elemento de la lista
            }
        });

        try {
            fetchForo(long_id);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private void fetchForo(String idUsuario) throws UnsupportedEncodingException {
        String baseUrl = "http://192.168.1.25:8080/api/foros/usuario/" + URLEncoder.encode(long_id, "UTF-8");

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                baseUrl,
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
                        Log.e("Foro", "Error al obtener datos: " + error.toString());
                        Toast.makeText(ForoUsuarioActivity.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
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
                String respuesta = jsonObject.getString("respuesta");
                String titulo  = jsonObject.getString("titulo");
                Long idFoto = jsonObject.getLong("idFoto");

                Foro foro = new Foro(idForo, idUsuario, respuesta, titulo, idFoto);
                foros.add(foro);
                fetchFoto(idFoto, foro);
            } catch (JSONException e) {
                Log.e("Foro", "Error parseando JSON", e);
            }
        }
        adapter.notifyDataSetChanged();  // Notificar adaptador después de actualizar la lista
    }

    private void fetchFoto(Long idFoto, Foro foro) {
        String url = "http://192.168.1.25:8080/api/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("fotoUrl");
                        foro.setFoto(new Foto(idFoto, fotoUrl));
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
