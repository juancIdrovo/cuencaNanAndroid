package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;

public class ForoComentarioActivity extends AppCompatActivity {

    private ImageView fotoView;
    private TextView nombreText;
    private TextView descripcionText;
    private EditText comentarioEditText;
    private Button submitButton;

    private Long puntoInteresId;
    private Long userId;
    private Long existingRatingId = null; // Para almacenar el ID de la valoración existente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_punto_de_interes);

        fotoView = findViewById(R.id.foto_view);
        nombreText = findViewById(R.id.nombre_text);
        descripcionText = findViewById(R.id.descripcion_text);
        comentarioEditText = findViewById(R.id.comentario_edit_text);
        submitButton = findViewById(R.id.submit_button);

        Button buttonMapa = findViewById(R.id.button_mapa);
        Button buttonPuntos = findViewById(R.id.button_puntos);
        Button buttonCasa = findViewById(R.id.button_casa);
        Button buttonEventos = findViewById(R.id.button_eventos);
        Button buttonForo = findViewById(R.id.button_foro);

        puntoInteresId = getIntent().getLongExtra("PUNTO_INTERES_ID", -1);
        userId = getIntent().getLongExtra("USER_ID", -1);


        submitButton.setOnClickListener(v -> {

        });

        ///btns nav///

       /*  buttonForo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

            startActivity(new Intent(ForoComentarioActivity.this, foroActivity.class));

         }
         });
*/
        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ForoComentarioActivity.this, MapActivity.class));

            }
        });

        buttonCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ForoComentarioActivity.this, PerfilUsuarioActivity.class));

            }
        });

        buttonPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ForoComentarioActivity.this, PuntosDeInteresActivity.class));

            }
        });

        buttonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ForoComentarioActivity.this, EventosActivity.class));

            }
        });

    }

    private void loadForoDetails(Long foroid) {
        String url = "http://192.168.1.25:8080/api/puntosinteres/" + foroid;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        String nombre = response.getString("titulo");
                        String descripcion = response.getString("respuesta");
                        Long idFoto = response.getLong("idFoto");

                        nombreText.setText(nombre);
                        descripcionText.setText(descripcion);
                        fetchFoto(idFoto);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Aquí puedes mostrar un mensaje de error o tomar alguna acción
                        showError("Error parsing JSON response");
                    }
                },
                error -> {
                    // Manejo de errores de la solicitud
                    error.printStackTrace();
                    // Aquí puedes mostrar un mensaje de error o tomar alguna acción
                    showError("Error fetching details: " + error.getMessage());
                }
        );

        // Añadir la solicitud a la cola
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    // Método para mostrar un mensaje de error (puedes personalizarlo según tu necesidad)
    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void fetchFoto(Long idFoto) {
        String url = "http://192.168.1.25:8080/api/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("foto");
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.logo_appc_con_fondo)
                                .into(fotoView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("RatePuntoDeInteres", "Error al cargar la foto: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

}








