package ec.tecAzuayM5a.cuencananandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

public class RatePuntoDeInteresActivity extends AppCompatActivity {
    private TextView nombreText;
    private TextView descripcionText;
    private ImageView fotoView;
    private EditText comentarioEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private Long puntoInteresId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_punto_de_interes);

        nombreText = findViewById(R.id.nombre_text);
        descripcionText = findViewById(R.id.descripcion_text);
        fotoView = findViewById(R.id.foto_view);
        comentarioEditText = findViewById(R.id.comentario_edit_text);
        ratingBar = findViewById(R.id.rating_bar);
        submitButton = findViewById(R.id.submit_button);

        puntoInteresId = getIntent().getLongExtra("PUNTO_INTERES_ID", -1);

        cargarPuntoDeInteres(puntoInteresId);

        submitButton.setOnClickListener(v -> {
            enviarCalificacionYComentario();
        });
    }

    private void cargarPuntoDeInteres(Long id) {
        String url = "http://192.168.100.196:8080/api/puntosinteres/" + id;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String nombre = response.getString("nombre");
                            String descripcion = response.getString("descripcion");
                            String fotoUrl = response.getString("foto");

                            nombreText.setText(nombre);
                            descripcionText.setText(descripcion);

                            Glide.with(RatePuntoDeInteresActivity.this)
                                    .load(fotoUrl)
                                    .placeholder(R.drawable.placeholder)
                                    .into(fotoView);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Manejar error de JSON
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar error de la solicitud
                        error.printStackTrace();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private void enviarCalificacionYComentario() {
        String url = "http://192.168.100.196:8080/api/usuariopuntosinteres";
        RequestQueue queue = Volley.newRequestQueue(this);

        Long userId = getUserId();
        if (userId == -1) {
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject postData = new JSONObject();
        try {
            postData.put("idusuario", userId);
            postData.put("idpuntosinteres", puntoInteresId);
            postData.put("calificacion", (int) ratingBar.getRating());
            postData.put("comentarios", comentarioEditText.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, url, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RatePuntoDeInteresActivity.this, "Calificación enviada exitosamente", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(RatePuntoDeInteresActivity.this, "Error al enviar la calificación", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }


    private Long getUserId() {
        SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getLong("userId", -1); // -1 es el valor predeterminado si no se encuentra el ID
    }
}



