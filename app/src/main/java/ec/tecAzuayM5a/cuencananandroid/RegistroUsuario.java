package ec.tecAzuayM5a.cuencananandroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ec.tecAzuayM5a.cuencananandroid.modelo.VolleyMultipartRequest;
public class RegistroUsuario extends AppCompatActivity {

    private String urlRegistro = "http://192.168.1.25:8080/api/usuarios";
    private String urlUpload = "http://192.168.1.25:8080/api/assets/upload";
    private RequestQueue requestQueue;
    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView imageView;
    private Uri imageUri;
    private String fotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        requestQueue = Volley.newRequestQueue(this);
        Button btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        imageView = findViewById(R.id.imageViewfoto);

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
                uploadImage(imageUri);
            } else {
                Toast.makeText(this, "No se ha seleccionado ninguna imagen.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImage(Uri imageUri) {
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
                                fotoPath = jsonResponse.getString("key"); // Guardar la clave en fotoPath
                                Log.d("Registro", "Clave de la imagen: " + fotoPath);
                                Toast.makeText(RegistroUsuario.this, "Imagen subida exitosamente.", Toast.LENGTH_SHORT).show();
                            } catch (JSONException | UnsupportedEncodingException e) {
                                e.printStackTrace();
                                Toast.makeText(RegistroUsuario.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
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
                            Log.e("Registro", "Error en la solicitud: " + errorMessage, error);
                            Toast.makeText(RegistroUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, DataPart> getByteData() {
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

    public void clickbtnGuardar(View view) {
        String cedula = ((TextInputEditText) findViewById(R.id.txtcedula)).getText().toString().trim();
        String nombres = ((TextInputEditText) findViewById(R.id.txtnombres)).getText().toString().trim();
        String apellidos = ((TextInputEditText) findViewById(R.id.txtapellidos)).getText().toString().trim();
        String mail = ((TextInputEditText) findViewById(R.id.txtcorreo)).getText().toString().trim();
        String direccion = ((TextInputEditText) findViewById(R.id.txtdireccion)).getText().toString().trim();
        String nombre_usuario = ((TextInputEditText) findViewById(R.id.txtnombres)).getText().toString().trim();
        String contrasena = ((TextInputEditText) findViewById(R.id.txtContrasena)).getText().toString().trim();
        String telefono = ((TextInputEditText) findViewById(R.id.txttelf)).getText().toString().trim();
        String fechaString = ((TextInputEditText) findViewById(R.id.txtFechaNac)).getText().toString().trim();
        Date fecha = null;
        if (!fechaString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                fecha = sdf.parse(fechaString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (fotoPath != null) {
            enviarSolicitudRegistro(cedula, nombres, apellidos, mail, direccion, nombre_usuario, contrasena, telefono, fecha, fotoPath);
        } else {
            Toast.makeText(this, "Debe seleccionar una imagen primero y esperar a que se suba.", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarSolicitudRegistro(String cedula, String nombres, String apellidos, String mail, String direccion, String nombre_usuario, String contrasena, String telefono, Date fecha, String fotoPath) {
        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("cedula", cedula);
            jsonBody.put("nombres", nombres);
            jsonBody.put("apellidos", apellidos);
            jsonBody.put("mail", mail);
            jsonBody.put("direccion", direccion);
            jsonBody.put("nombre_usuario", nombre_usuario);
            jsonBody.put("contrasena", contrasena);
            jsonBody.put("celular", telefono);
            if (fecha != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fechaFormateada = sdf.format(fecha);
                jsonBody.put("fecha_nacimiento", fechaFormateada);
            }
            jsonBody.put("fotoPath", fotoPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRegistro, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Registro", "Respuesta del servidor: " + response.toString());
                        startActivity(new Intent(RegistroUsuario.this, LoginActivity.class));
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

                Log.e("Registro", "Error en la solicitud: " + errorMessage, error);
                Toast.makeText(RegistroUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }
}
