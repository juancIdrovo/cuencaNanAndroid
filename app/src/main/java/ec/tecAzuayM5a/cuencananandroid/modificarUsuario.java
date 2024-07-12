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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

import ec.tecAzuayM5a.cuencananandroid.ip.ip;
import ec.tecAzuayM5a.cuencananandroid.modelo.Usuario;
import ec.tecAzuayM5a.cuencananandroid.modelo.VolleyMultipartRequest;

public class modificarUsuario extends AppCompatActivity {
    EditText txtNombres, txtApellidos, txtCorreo, txtDireccion, txtTelefono, txtcedula, txtFecha, txtContrasena;
    Button btnGuarda, btnCancelar, btnSeleccionarFoto;
    private String fotoPath, fotoUrl, userName, apellidos, direccion, contrasena, telefono, fecha_nac, userEmail, long_id, cedula;
    private Uri imageUri;
    private ImageView imageView;
    private static final int REQUEST_IMAGE_PICK = 1;
    private RequestQueue requestQueue;
    ip ipo = new ip();
    String direccion1 = ipo.getIp();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_datos_estudiante);
        requestQueue = Volley.newRequestQueue(this);

        long_id = getIntent().getStringExtra("id_usuario");
        userName = getIntent().getStringExtra("user_name");
        cedula = getIntent().getStringExtra("cedula");
        apellidos = getIntent().getStringExtra("apellidos");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("celular");
        userEmail = getIntent().getStringExtra("user_email");
        contrasena = getIntent().getStringExtra("contrasena");
        fecha_nac = getIntent().getStringExtra("fecha_nac");
        fotoPath = getIntent().getStringExtra("fotoPath");
        fotoUrl = getIntent().getStringExtra("fotoUrl");

        txtNombres = findViewById(R.id.txtnombres);
        txtApellidos = findViewById(R.id.txtapellidos);
        txtCorreo = findViewById(R.id.txtcorreo);
        txtDireccion = findViewById(R.id.txtdireccion);
        txtTelefono = findViewById(R.id.txttelf);
        txtcedula = findViewById(R.id.txtcedula);
        txtFecha = findViewById(R.id.txtFechaNac);
        txtContrasena = findViewById(R.id.txtcontrasena);
        imageView = findViewById(R.id.fotoPerfil);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSeleccionarFoto = findViewById(R.id.btnfotito);
        btnGuarda = findViewById(R.id.btnGuardar);

        updateUI();

        btnSeleccionarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        btnGuarda.setOnClickListener(view -> {
            String nuevosNombres = txtNombres.getText().toString();
            String nuevosApellidos = txtApellidos.getText().toString();
            String nuevoCorreo = txtCorreo.getText().toString();
            String nuevaDireccion = txtDireccion.getText().toString();
            String nuevoTelefono = txtTelefono.getText().toString();
            String nuevaContrasena = txtContrasena.getText().toString();
            String nuevaFecha = txtFecha.getText().toString().trim();
            Date fecha = null;
            if (!nuevaFecha.isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                    fecha = sdf.parse(nuevaFecha);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Usuario usuModi = new Usuario(cedula, nuevosNombres, nuevosApellidos, nuevoCorreo, nuevaDireccion, fecha, nuevaContrasena, nuevoTelefono, fotoPath);

            updateStudent(usuModi, response -> {
                Toast.makeText(modificarUsuario.this, "Estudiante modificado correctamente", Toast.LENGTH_SHORT).show();
                Toast.makeText(modificarUsuario.this, "Vuelva a iniciar sesión para que se actualicen sus datos", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }, error -> {
                Log.e("VolleyError", "Error al modificar estudiante: " + long_id);
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    String responseBody = new String(error.networkResponse.data);
                    Log.e("VolleyError", "Respuesta del servidor: " + responseBody);
                }
                Toast.makeText(modificarUsuario.this, "Error al modificar sus datos, inténtelo de nuevo más tarde", Toast.LENGTH_SHORT).show();
            });
        });

        btnCancelar.setOnClickListener(view -> onBackPressed());
    }

    private void updateStudent(Usuario usuario, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        String url = direccion1 + "/usuarios/" + long_id;
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombres", usuario.getNombres());
            jsonBody.put("apellidos", usuario.getApellidos());
            jsonBody.put("mail", usuario.getMail());
            jsonBody.put("direccion", usuario.getDireccion());
            jsonBody.put("celular", usuario.getCelular());
            jsonBody.put("contrasena", usuario.getContrasena());
            if (usuario.getFecha_nacimiento() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fechaFormateada = sdf.format(usuario.getFecha_nacimiento());
                jsonBody.put("fecha_nacimiento", fechaFormateada);
            }
            jsonBody.put("fotoPath", fotoPath);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, successListener, errorListener);
        requestQueue.add(request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

            String urlUpload = direccion1 + "/assets/upload";

            VolleyMultipartRequest request = new VolleyMultipartRequest(Request.Method.POST, urlUpload,
                    response -> {
                        try {
                            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                            JSONObject jsonResponse = new JSONObject(jsonString);
                            fotoPath = jsonResponse.getString("key");
                            Log.d("ModificarUsuario", "Clave de la imagen: " + fotoPath);
                            Toast.makeText(modificarUsuario.this, "Imagen subida exitosamente.", Toast.LENGTH_SHORT).show();
                        } catch (JSONException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                            Toast.makeText(modificarUsuario.this, "Error al procesar la respuesta del servidor.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    error -> {
                        String errorMessage;
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        } else {
                            errorMessage = "Error desconocido en la solicitud.";
                        }
                        Log.e("ModificarUsuario", "Error en la solicitud: " + errorMessage, error);
                        Toast.makeText(modificarUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
                    }) {
                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("file", new DataPart("image.jpg", imageBytes, "image/jpeg"));
                    return params;
                }
            };

            requestQueue.add(request);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al leer la imagen seleccionada.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI() {
        txtNombres.setText(userName);
        txtApellidos.setText(apellidos);
        txtCorreo.setText(userEmail);
        txtDireccion.setText(direccion);
        txtTelefono.setText(telefono);
        txtcedula.setText(cedula);
        txtFecha.setText(fecha_nac);
        txtContrasena.setText(contrasena);

        if (fotoUrl != null && !fotoUrl.isEmpty()) {
            Glide.with(this)
                    .load(fotoUrl)
                    .apply(new RequestOptions().circleCrop())
                    .into(imageView);
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            TextView txtFecha = getActivity().findViewById(R.id.txtFechaNac);
            txtFecha.setText(String.format(Locale.US, "%d-%02d-%02d", year, month + 1, day));
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }
}
