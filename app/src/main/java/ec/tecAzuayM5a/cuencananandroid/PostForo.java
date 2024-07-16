package ec.tecAzuayM5a.cuencananandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import ec.tecAzuayM5a.cuencananandroid.ip.ip;

import ec.tecAzuayM5a.cuencananandroid.modelo.VolleyMultipartRequest;

public class PostForo extends AppCompatActivity {
    private String long_id;
    private TextView tvtitulo, tvdescripcion;
    private Button btpost;
    private ImageButton btnSeleccionarFoto;
    private ImageView imageView;
    private Uri imageUri;
    private Long fotoId; // Para almacenar el ID de la foto
    ip ipo = new ip();
    String direccion = ipo.getIp();
    private String urlRegistro = direccion +"/foros";
    private String urlUpload = direccion + "/foto";

    private static final int REQUEST_IMAGE_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_foro);
        long_id = getIntent().getStringExtra("id_usuario");
        btpost = findViewById(R.id.btnpost);
        tvtitulo = findViewById(R.id.tvtitulo);
        tvdescripcion = findViewById(R.id.tvdescripcion);
        btnSeleccionarFoto = findViewById(R.id.btnimagen);
        imageView = findViewById(R.id.imagenpost);
        Log.d("PostForo", "Nombre: " + long_id);


        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

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
                                Toast.makeText(PostForo.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(PostForo.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
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

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRegistro, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("PostForo", "Respuesta del servidor: " + response.toString());
                        Intent intent = new Intent(PostForo.this, ForoActivity.class);
                        intent.putExtra("id_usuario", long_id);
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
                Toast.makeText(PostForo.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        Volley.newRequestQueue(PostForo.this).add(request);
    }
}