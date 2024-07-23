package ec.tecAzuayM5a.cuencananandroid;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ec.tecAzuayM5a.cuencananandroid.ip.ip;
import ec.tecAzuayM5a.cuencananandroid.modelo.Usuariopuntosinteres;

public class RatePuntoDeInteresActivity extends AppCompatActivity {

    private ImageView fotoView;
    private TextView nombreText;
    private TextView descripcionText;
    private RatingBar ratingBar;
    private EditText comentarioEditText;
    private Button submitButton;
    private Button comentariosButton;

    private Long puntoInteresId;
    private Long userId;
    private Long existingRatingId = null; // Para almacenar el ID de la valoración existente
    ip ipo = new ip();
    String direccion = ipo.getIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_punto_de_interes);

        fotoView = findViewById(R.id.foto_view);
        nombreText = findViewById(R.id.nombre_text);
        descripcionText = findViewById(R.id.descripcion_text);
        ratingBar = findViewById(R.id.rating_bar);
        comentarioEditText = findViewById(R.id.comentario_edit_text);
        submitButton = findViewById(R.id.submit_button);
        comentariosButton = findViewById(R.id.comentarios_button);
        Button buttonMapa = findViewById(R.id.button_mapa);
        Button buttonPuntos = findViewById(R.id.button_puntos);
        ImageButton deleteButton = findViewById(R.id.delete_button);
        Button buttonEventos = findViewById(R.id.button_eventos);

        puntoInteresId = getIntent().getLongExtra("PUNTO_INTERES_ID", -1);

        // Obtén el userId del usuario que inició sesión
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);

        if (userId == -1) {
            Log.e("RatePuntoDeInteres", "User ID is not found in SharedPreferences");
            Toast.makeText(this, "User ID is not found. Please log in again.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        loadPuntoInteresDetails(puntoInteresId);

        submitButton.setOnClickListener(v -> {
            try {
                enviarCalificacionYComentario();

            } catch (JSONException e) {
                Log.e("RatePuntoDeInteres", "Error al enviar la valoración y comentario: " + e.toString());
                Toast.makeText(this, "Error al enviar la valoración y comentario", Toast.LENGTH_SHORT).show();
            }
        });

        comentariosButton.setOnClickListener(v -> {
            Intent intent = new Intent(RatePuntoDeInteresActivity.this, ComentariosActivity.class);
            intent.putExtra("PUNTO_INTERES_ID", puntoInteresId);
            startActivity(intent);
        });

        ///btns nav///
        buttonMapa.setOnClickListener(view -> startActivity(new Intent(RatePuntoDeInteresActivity.this, MapActivity.class)));
        buttonPuntos.setOnClickListener(view -> startActivity(new Intent(RatePuntoDeInteresActivity.this, PuntosDeInteresActivity.class)));
        buttonEventos.setOnClickListener(view -> startActivity(new Intent(RatePuntoDeInteresActivity.this, EventosActivity.class)));

        deleteButton.setOnClickListener(v -> {
            eliminarCalificacionYComentario();
        });
    }

    private void loadPuntoInteresDetails(Long puntoInteresId) {
        String url = direccion + "/puntosinteres/" + puntoInteresId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String nombre = response.getString("nombre");
                        String descripcion = response.getString("descripcion");
                        Long idFoto = response.getLong("idFoto");

                        nombreText.setText(nombre);
                        descripcionText.setText(descripcion);
                        fetchFoto(idFoto);

                        // Cargar valoración y comentario existente
                        loadExistingRatingAndComment(userId, puntoInteresId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("RatePuntoDeInteres", "Error al cargar los detalles del punto de interés: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void fetchFoto(Long idFoto) {
        String url = direccion + "/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("foto");
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.cargando)
                                .into(fotoView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("RatePuntoDeInteres", "Error al cargar la foto: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void loadExistingRatingAndComment(Long userId, Long puntoInteresId) {
        String url = direccion + "/usuariopuntosinteres/" + userId + "/" + puntoInteresId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        if (response.length() > 0) { // Verifica si existe una calificación
                            existingRatingId = response.getLong("idusuariopuntosinteres");
                            int calificacion = response.getInt("calificacion");
                            String comentarios = response.getString("comentarios");

                            ratingBar.setRating(calificacion);
                            comentarioEditText.setText(comentarios);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 409) {
                        Log.e("RatePuntoDeInteres", "Múltiples valoraciones encontradas para el mismo usuario y punto de interés");
                        Toast.makeText(this, "Múltiples valoraciones encontradas. Por favor, contacte al administrador.", Toast.LENGTH_LONG).show();
                    } else {
                        Log.e("RatePuntoDeInteres", "Error al cargar la valoración y comentario existente: " + error.toString());
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void enviarCalificacionYComentario() throws JSONException {
        int calificacion = (int) ratingBar.getRating();
        String comentarios = comentarioEditText.getText().toString().trim();

        // Verificar si el comentario o la calificación están vacíos
        if (comentarios.isEmpty() || calificacion == 0) {
            Toast.makeText(this, "No se puede publicar una calificación o comentario vacío", Toast.LENGTH_SHORT).show();
            return; // Detener el envío si el comentario o la calificación están vacíos
        }

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("idusuario", userId);
        jsonBody.put("idpuntosinteres", puntoInteresId);
        jsonBody.put("calificacion", calificacion);
        jsonBody.put("comentarios", comentarios);

        String url;
        int method;

        if (existingRatingId != null) {
            // Si ya existe una valoración, la actualizamos
            url = direccion + "/usuariopuntosinteres/" + existingRatingId;
            method = Request.Method.PUT;
        } else {
            // Si no existe, creamos una nueva
            url = direccion + "/usuariopuntosinteres";
            method = Request.Method.POST;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, url, jsonBody,
                response -> {
                    Toast.makeText(this, "Valoración enviada", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RatePuntoDeInteresActivity.this, PuntosDeInteresActivity.class);
                    startActivity(intent);
                    finish();
                },
                error -> Log.e("RatePuntoDeInteres", "Error al enviar la valoración y comentario: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void eliminarCalificacionYComentario() {
        if (existingRatingId == null) {
            Toast.makeText(this, "No hay valoración para eliminar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear el diálogo de confirmación
        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que deseas eliminar esta valoración y comentario?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    // Proceder con la eliminación si el usuario confirma
                    String url = direccion + "/usuariopuntosinteres/" + existingRatingId;
                    Log.d("RatePuntoDeInteres", "URL de eliminación: " + url);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, url, null,
                            response -> {
                                Log.d("RatePuntoDeInteres", "Respuesta de eliminación: " + response.toString());
                                Toast.makeText(this, "Valoración y comentario eliminados", Toast.LENGTH_SHORT).show();
                                ratingBar.setRating(0);
                                comentarioEditText.setText("");
                                existingRatingId = null;

                                // Redirigir a la actividad PuntosDeInteresActivity
                                Intent intent = new Intent(RatePuntoDeInteresActivity.this, PuntosDeInteresActivity.class);
                                startActivity(intent);
                                finish();
                            },
                            error -> {
                                Toast.makeText(this, "Valoración y comentario eliminados", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RatePuntoDeInteresActivity.this, PuntosDeInteresActivity.class);
                                startActivity(intent);
                                finish();
                                NetworkResponse networkResponse = error.networkResponse;
                                if (networkResponse != null) {
                                    String statusCode = String.valueOf(networkResponse.statusCode);
                                    String data = new String(networkResponse.data);
                                    Log.e("RatePuntoDeInteres", "Código de estado: " + statusCode);
                                    Log.e("RatePuntoDeInteres", "Respuesta de error: " + data);
                                }

                            }
                    );

                    // Agregar la solicitud a la cola de Volley
                    RequestQueue requestQueue = Volley.newRequestQueue(this);
                    requestQueue.add(jsonObjectRequest);
                })
                .setNegativeButton("No", null) // No hacer nada si el usuario cancela
                .show();
    }

}











