package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
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
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;


public class PerfilUsuarioActivity extends AppCompatActivity {
    private String userName;
    private String userEmail;
    private Uri imageUri;
    private String cedula;
    Button  btnmodificar,  btnCrearUbicacion, btnVerUbicaciones, btnPuntosInteres;
    ImageView opt;
    String url = "http://192.168.0.209:8080/api/usuarios/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_usuairo);

        userName = getIntent().getStringExtra("user_name");
        cedula = getIntent().getStringExtra("cedula");
        userEmail = getIntent().getStringExtra("user_email");
        imageUri = Uri.parse(getIntent().getStringExtra("image_uri"));



        btnPuntosInteres = findViewById(R.id.btnPuntosInteres);
        btnCrearUbicacion = findViewById(R.id.btnCrearUbicacion);
        btnVerUbicaciones = findViewById(R.id.btnVerUbicaciones);
        opt = findViewById(R.id.btnOptions);
        updateUI();
        opt = findViewById(R.id.btnOptions);
        opt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });

    }

    private void showPopupMenu(View view) {
        // Inflate the popup menu layout
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_menu_desplegaable, null);

        // Create the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setOutsideTouchable(true);

        // Find the buttons in the popup menu
        Button modifyProfileButton = popupView.findViewById(R.id.modify_profile_button);
        Button signOutButton = popupView.findViewById(R.id.sign_out_button);

        // Set click listeners for the buttons


        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
finish();
                startActivity(new Intent(PerfilUsuarioActivity.this, LoginActivity.class).putExtra("cedula",cedula));

            }
        });

        modifyProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    JSONObject jsonBody = new JSONObject();
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url+cedula, jsonBody,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        String status = response.optString("status", "");

                                        if (!status.equals("error")) {

                                            String nombre = response.optString("nombres", "");
                                            String correo = response.optString("correo", "");
                                            String imageUri = response.optString("foto", "");
                                            String cedula = response.optString("cedula", "");
                                            String apellido = response.optString("apellidos", "");
                                            String direccion = response.optString("direccion", "");
                                            String telefono = response.optString("telf", "");
                                            String contrasena = response.optString("contrasena", "");
                                            String fecha_nac = response.optString("fecha_nac", "");

                                            Intent intent = new Intent(PerfilUsuarioActivity.this, modificarUsuario.class);
                                            intent.putExtra("user_name", nombre);
                                            intent.putExtra("user_email", correo);
                                            intent.putExtra("cedula",cedula);
                                            intent.putExtra("image_uri", imageUri.toString());
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
                            Log.e("LoginActivity", "Error en la solicitud: " + error.getMessage());
                            Toast.makeText(PerfilUsuarioActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });

                    RequestQueue requestQueue = Volley.newRequestQueue(PerfilUsuarioActivity.this);
                    requestQueue.add(request);

            }

        });

        // Show the popup menu
        popupWindow.showAsDropDown(view);
    }

    private void updateUI() {
        TextView tvName = findViewById(R.id.tvName);
        TextView tvMail = findViewById(R.id.tvMail);
        ImageView ivUserImage = findViewById(R.id.fotoPerfil);

        tvName.setText(userName);
        tvMail.setText(userEmail);

        // Verifica que la URL de la imagen no sea nula
        if (imageUri != null) {
            // Intenta cargar la imagen con Glide
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                             // Imagen de marcador de posición en caso de error
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Manejar fallo de carga de imagen aquí
                            Log.e("PerfilUsuarioActivity", "Error al cargar la imagen con Glide: " + e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // La imagen se cargó correctamente
                            return false;
                        }
                    })
                    .into(ivUserImage);
        } else {
            // URL de la imagen nula o vacía, usa una imagen de marcador de posición
            ivUserImage.setImageResource(R.drawable.luffiperfil);
        }
    }

}

