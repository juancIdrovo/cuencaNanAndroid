package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
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

public class ComentariosForo extends AppCompatActivity {

    private ListView commentsListView;
    private Long foroId;
    private Long idusuario;
    private Button addCommentButton;
    private EditText Comentario;
    private String comentario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentariosforo);

        commentsListView = findViewById(R.id.comments_list_view);
        addCommentButton = findViewById(R.id.btncomentario);
        Comentario = findViewById(R.id.new_comment_text);


        foroId = getIntent().getLongExtra("id_foro", -1);
        idusuario = getIntent().getLongExtra("id_usuario", -1);

        if (foroId == -1) {
            Log.e("ComentariosForo", "Foro ID is invalid");
            Toast.makeText(this, "Foro ID is invalid.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadComments(foroId);
        addCommentButton.setOnClickListener(v -> addComment());
    }

    private void loadComments(Long foroId) {
        String url = "http://192.168.1.25:8080/api/comentarios/foro/" + foroId;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    List<Comentario> comentarios = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            Long userId = jsonObject.getLong("idUsuario");
                            String comentarioText = jsonObject.getString("comentario");

                            String userUrl = "http://192.168.1.25:8080/api/usuarios/" + userId;

                            JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, userUrl, null,
                                    userResponse -> {
                                        try {
                                            String userName = userResponse.getString("nombre_usuario");
                                            String userPhoto = userResponse.getString("fotoUrl");
                                            comentarios.add(new Comentario(userName, comentarioText, userPhoto));
                                            if (comentarios.size() == response.length()) {
                                                ComentariosAdapter adapter = new ComentariosAdapter(ComentariosForo.this, comentarios);
                                                commentsListView.setAdapter(adapter);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    },
                                    error -> Log.e("ComentariosForo", "Error al obtener los detalles del usuario: " + error.toString()));

                            Volley.newRequestQueue(this).add(userRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Log.e("ComentariosForo", "Error al cargar los comentarios: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
    private void addComment() {
        comentario = Comentario.getText().toString();
        if (comentario.isEmpty()) {
            Toast.makeText(this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = "http://192.168.1.25:8080/api/comentarios";
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("comentario", comentario);
            jsonRequest.put("idForo", foroId);
            jsonRequest.put("idUsuario",idusuario);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonRequest,
                    response -> {
                        try {
                            String userName = response.getString("nombre_usuario");
                            String userPhoto = response.getString("fotoUrl");

                            fetchUserDataAndAddComment(idusuario, comentario, userName, userPhoto);
                            Intent intent = new Intent(ComentariosForo.this, ComentariosForo.class);
                            intent.putExtra("id_foro", foroId);
                            intent.putExtra("id_usuario", idusuario);
                            finish();
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    },
                    error -> Log.e("ComentariosForo", "Error al añadir comentario: " + error.toString())
            );

            Volley.newRequestQueue(this).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void fetchUserDataAndAddComment(Long idUsuario, String comentario, String userName, String userPhoto) {
        String url = "http://192.168.1.25:8080/api/usuarios/" + idUsuario;

        JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                userResponse -> {
                    try {
                        String nombreUsuario = userResponse.getString("nombre_usuario");
                        String fotoUrl = userResponse.getString("fotoUrl");

                        Comentario newComment = new Comentario(nombreUsuario, comentario, fotoUrl);
                        ((ComentariosAdapter) commentsListView.getAdapter()).add(newComment);
                        Comentario.setText("");
                        onBackPressed();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("ComentariosForo", "Error al obtener datos del usuario: " + error.toString())
        );

        // Añade la solicitud a la cola de solicitudes
        Volley.newRequestQueue(this).add(userRequest);
    }
}
