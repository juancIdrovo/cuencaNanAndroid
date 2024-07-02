package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONObject;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private String userName;
    private String userEmail;
    private Uri imageUri;
    private String long_id;
    ImageView opt;
    Button btnNotas, btnHorario, btnDocente, btnmodificar, btnCurso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuairo);

        userName = getIntent().getStringExtra("nombre");
        long_id = getIntent().getStringExtra("id_usuario");
        userEmail = getIntent().getStringExtra("mail");
      //  btnHorario = findViewById(R.id.btnSchedule1);
        btnDocente = findViewById(R.id.btnCrearUbicacion);
        btnCurso = findViewById(R.id.btnPuntosInteres);

        updateUI();

        opt = findViewById(R.id.btnOptions);

        btnCurso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PerfilUsuarioActivity.this, PuntosDeInteresActivity.class));

            }
        });
        btnDocente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(PerfilUsuarioActivity.this, RatePuntoDeInteresActivity.class));

            }
        });


    }
    private void updateUI() {
        TextView tvName = findViewById(R.id.tvName);
        TextView tvMail = findViewById(R.id.tvMail);
        ImageView ivUserImage = findViewById(R.id.fotoPerfil);

        if (tvName != null && tvMail != null) {
            tvName.setText(userName);
            tvMail.setText(userEmail);
        } else {
            Log.e("PerfilUsuarioActivity", "TextView es nulo");
        }

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                            .error(R.drawable.luffiperfil)
                            .placeholder(R.drawable.luffiperfil))
                    .into(ivUserImage);
        } else {
            ivUserImage.setImageResource(R.drawable.luffiperfil);
        }
    }

    private void loadUserData() {
        String url = "http://192.168.0.123:8080/api/usuarios/" + long_id;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.optString("status", "");

                            if (!status.equals("error")) {
                                String nombre = response.optString("nombres", "");
                                String correo = response.optString("mail", "");
                                String cedula = response.optString("cedula", "");
                                String apellido = response.optString("apellidos", "");
                                String direccion = response.optString("direccion", "");
                                String telefono = response.optString("celular", "");
                                String contrasena = response.optString("contrasena", "");
                                String fecha_nac = response.optString("fecha_nacimiento", "");

                                Intent intent = new Intent(PerfilUsuarioActivity.this, modificarUsuario.class);
                                intent.putExtra("user_name", nombre);
                                intent.putExtra("user_email", correo);
                                intent.putExtra("cedula", cedula);
                                intent.putExtra("apellidos", apellido);
                                intent.putExtra("direccion", direccion);
                                intent.putExtra("telefono", telefono);
                                intent.putExtra("contrasena", contrasena);
                                intent.putExtra("fecha_nac", fecha_nac);
                                startActivity(intent);
                            } else {
                                Toast.makeText(PerfilUsuarioActivity.this, "Autenticación fallida", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("PerfilUsuarioActivity", "Error en la solicitud: " + error.getMessage());
                Toast.makeText(PerfilUsuarioActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(PerfilUsuarioActivity.this);
        requestQueue.add(request);
    }
}
