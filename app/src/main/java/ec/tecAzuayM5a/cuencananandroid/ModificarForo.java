package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;
import ec.tecAzuayM5a.cuencananandroid.modelo.Usuario;
import ec.tecAzuayM5a.cuencananandroid.modelo.VolleyMultipartRequest;

public class ModificarForo extends AppCompatActivity {
    private Long long_id;
    private String long2;
    private TextView tvtitulo, tvdescripcion;
    private Button btpost, btnSeleccionarFoto;
    private ImageView imageView;
    private Uri imageUri;
    private Long idForo;
    private Long fotoId;

    private String urlUpload = "http://192.168.1.25:8080/api/foto";
    private String urlRegistro = "http://192.168.137.83:8080/api/foros";


    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_foro);
        long_id = getIntent().getLongExtra("id_usuario", -1);
        btpost = findViewById(R.id.btnpost1);
        tvtitulo = findViewById(R.id.tvtitulo1);

        tvdescripcion = findViewById(R.id.tvdescripcion1);
        btnSeleccionarFoto = findViewById(R.id.btnimagen1);
        imageView = findViewById(R.id.imagenpost1);
        long2 = long_id.toString();
        idForo = getIntent().getLongExtra("id_foro", -1);
        Log.d("PerfilUsuarioActivity", "Nombre: " + idForo + ", Email: " + long_id);



        cargarforo(idForo);
            btpost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (imageUri != null) {
                        uploadImageAndPostForo(imageUri);
                    } else {
                        postForo(null);
                    }
                }
            });


            // Funcionalidad para un nuevo foro
            btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, REQUEST_IMAGE_PICK);
                }
            });


        }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();

            if (imageUri != null) {
                imageView.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "No se ha seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void cargarforo(Long idForo) {
        String url = "http://192.168.1.25:8080/api/foros/" + idForo;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String nombre = response.getString("titulo");
                        String descripcion = response.getString("respuesta");
                        Long idFoto = response.getLong("idFoto");

                        tvtitulo.setText(nombre);
                        tvdescripcion.setText(descripcion);
                        fetchFoto(idFoto);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("RatePuntoDeInteres", "Error al cargar los detalles del punto de interés: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void uploadImageAndPostForo(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = new byte[inputStream.available()];
            inputStream.read(imageBytes);

            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, urlUpload,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                JSONObject jsonResponse = new JSONObject(jsonString);
                                fotoId = jsonResponse.optLong("fotoid", -1);
                                Log.d("PostForo", "ID de la foto: " + fotoId);
                                postForo(fotoId == -1 ? null : fotoId); // Publicar el foro con el ID de la foto
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Toast.makeText(ModificarForo.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage;
                            if (error.networkResponse != null && error.networkResponse.data != null) {
                                errorMessage = new String(error.networkResponse.data);
                            } else {
                                errorMessage = "Error desconocido en la solicitud.";
                            }
                            Log.e("PostForo", "Error en la solicitud: " + errorMessage, error);
                            Toast.makeText(ModificarForo.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("file", new DataPart("image.jpg", imageBytes, "image/jpeg"));
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(request);

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la imagen.", Toast.LENGTH_SHORT).show();
        }
    }
    private void fetchFoto(Long idFoto) {
        String url = "http://192.168.1.25:8080/api/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("fotoUrl");
                        Glide.with(this)
                                .load(fotoUrl)
                                .placeholder(R.drawable.logo_appc_con_fondo)
                                .into(imageView);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("RatePuntoDeInteres", "Error al cargar la foto: " + error.toString())
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    private void postForo(Long fotoId) {
        String titulo = tvtitulo.getText().toString();
        String descripcion = tvdescripcion.getText().toString();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("idUsuario", long_id);
            jsonBody.put("respuesta", descripcion);
            jsonBody.put("titulo", titulo);
            if (fotoId != null) {
                jsonBody.put("idFoto", fotoId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "http://192.168.1.25:8080/api/foros/" + idForo;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PostForo", "Respuesta del servidor: " + response.toString());
                        Intent intent = new Intent(ModificarForo.this, ForoActivity.class);
                        intent.putExtra("id_usuario", long2);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    errorMessage = new String(error.networkResponse.data);
                } else {
                    errorMessage = "Error desconocido en la solicitud.";
                }

                Log.e("PostForo", "Error en la solicitud: " + errorMessage, error);
                Toast.makeText(ModificarForo.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(ModificarForo.this).add(request);
    }
}
