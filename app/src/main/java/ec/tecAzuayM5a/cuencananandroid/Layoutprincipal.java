package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ec.tecAzuayM5a.cuencananandroid.databinding.ActivityLayoutprincipalBinding;

public class Layoutprincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLayoutprincipalBinding binding;
    private String userName;
    private String userEmail;
    private Uri imageUri;
    private String long_id;
    Button btnmodificar,  btnCrearUbicacion, btnVerUbicaciones, btnPuntosInteres;
    ImageView opt;
    String url = "http://192.168.1.25:8080/api/usuarios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLayoutprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userName = getIntent().getStringExtra("nombre");
        long_id = getIntent().getStringExtra("id_usuario");
        userEmail = getIntent().getStringExtra("mail");
        updateUI();
        setSupportActionBar(binding.appBarLayoutprincipal.toolbar);
        binding.appBarLayoutprincipal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonBody = new JSONObject();
                String url = "http://192.168.1.25:8080/api/usuarios/" + long_id;

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, jsonBody,
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

                                        Intent intent = new Intent(Layoutprincipal.this, modificarUsuario.class);
                                        intent.putExtra("user_name", nombre);
                                        intent.putExtra("user_email", correo);
                                        intent.putExtra("cedula", cedula);
                                        // intent.putExtra("image_uri", imageUri.toString()); // Asegúrate de que imageUri está definido
                                        intent.putExtra("apellidos", apellido);
                                        intent.putExtra("direccion", direccion);
                                        intent.putExtra("telefono", telefono);
                                        intent.putExtra("contrasena", contrasena);
                                        intent.putExtra("fecha_nac", fecha_nac);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(Layoutprincipal.this, "Autenticación fallida", Toast.LENGTH_LONG).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("LoginActivity", "Error en la solicitud: " + error.getMessage());
                        Toast.makeText(Layoutprincipal.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(Layoutprincipal.this);
                requestQueue.add(request);
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fragment_home, R.id.nav_gallery, R.id.puntosInteres, R.id.nav_puntosInteres, R.id.calificarPuntoInteres)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_layoutprincipal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layoutprincipal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_layoutprincipal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void updateUI() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvMail = headerView.findViewById(R.id.tvMail);
        ImageView ivUserImage = headerView.findViewById(R.id.fotoPerfil);

        if (tvName != null && tvMail != null) {
            tvName.setText(userName);
            tvMail.setText(userEmail);
        } else {
            Log.e("Layoutprincipal", "TextView es nulo");
        }

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                            .error(R.drawable.luffiperfil) // Usar imagen predeterminada si hay error
                            .placeholder(R.drawable.luffiperfil)) // Imagen predeterminada mientras se carga
                    .into(ivUserImage);
        } else {
            ivUserImage.setImageResource(R.drawable.luffiperfil);
        }
    }

}