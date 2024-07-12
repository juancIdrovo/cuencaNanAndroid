package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.adaptador.ComentariosAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.Comentario;
import ec.tecAzuayM5a.cuencananandroid.ip.ip;

public class ComentariosActivity extends AppCompatActivity {

    private ListView commentsListView;
    private Long puntoInteresId;
    ip ipo = new ip();
    String direccion = ipo.getIp();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        commentsListView = findViewById(R.id.comments_list_view);

        puntoInteresId = getIntent().getLongExtra("PUNTO_INTERES_ID", -1);

        if (puntoInteresId == -1) {
            Log.e("ComentariosActivity", "Punto Interes ID is invalid");
            Toast.makeText(this, "Punto Interes ID is invalid.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadComments(puntoInteresId);
    }

    private void loadComments(Long puntoInteresId) {
        String url = direccion +"/usuariopuntosinteres?puntoInteresId=" + puntoInteresId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Comentario> comentarios = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Long userId = jsonObject.getLong("idusuario");
                            String comentarioText = jsonObject.getString("comentarios");

                            String userUrl = direccion +"/usuarios/" + userId;

                            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                                    userResponse -> {
                                        try {
                                            String userName = userResponse.getString("nombre_usuario");
                                            String userPhoto = userResponse.getString("fotoUrl");
                                            comentarios.add(new Comentario(userName, comentarioText, userPhoto));
                                            if (comentarios.size() == response.length()) {
                                                ComentariosAdapter adapter = new ComentariosAdapter(ComentariosActivity.this, comentarios);
                                                commentsListView.setAdapter(adapter);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> Log.e("ComentariosActivity", "Error al obtener los detalles del usuario: " + error.toString()));

                            Volley.newRequestQueue(this).add(userRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e("ComentariosActivity", "Error al cargar los comentarios: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}

