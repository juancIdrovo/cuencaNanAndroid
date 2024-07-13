package ec.tecAzuayM5a.cuencananandroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
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
import ec.tecAzuayM5a.cuencananandroid.ip.ip;

import ec.tecAzuayM5a.cuencananandroid.modelo.VolleyMultipartRequest;
import ec.tecAzuayM5a.cuencananandroid.validaciones.Validator;

public class RegistroUsuario extends AppCompatActivity {

    private RequestQueue requestQueue;
    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView imageView;
    private Uri imageUri;
    private String fotoPath;
    private TextInputEditText txtFechaNac;
    ip ipo = new ip();
    String direccion = ipo.getIp();
    private String urlRegistro = direccion + "/usuarios";
    private String urlUpload = direccion + "/assets/upload";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        requestQueue = Volley.newRequestQueue(this);

        Button btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);
        imageView = findViewById(R.id.imageViewfoto);
        txtFechaNac = findViewById(R.id.txtFechaNac);

        setNextFocus(findViewById(R.id.txtcedula), findViewById(R.id.txtnombres));
        setNextFocus(findViewById(R.id.txtnombres), findViewById(R.id.txtapellidos));
        setNextFocus(findViewById(R.id.txtapellidos), findViewById(R.id.txtcorreo));
        setNextFocus(findViewById(R.id.txtcorreo), findViewById(R.id.txtdireccion));
        setNextFocus(findViewById(R.id.txtdireccion), findViewById(R.id.txtContrasena));
        setNextFocus(findViewById(R.id.txtContrasena), findViewById(R.id.txttelf));

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }

    private void setNextFocus(TextInputEditText currentEditText, final TextInputEditText nextEditText) {
        currentEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                nextEditText.requestFocus();
                return true;
            }
            return false;
        });
    }

    public void showDatePickerDialog(View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view1, year1, monthOfYear, dayOfMonth) -> {
            String selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year1, monthOfYear + 1, dayOfMonth);
            txtFechaNac.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
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
                    response -> {
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
                    },
                    error -> {
                        String errorMessage;
                        if (error.networkResponse != null && error.networkResponse.data != null) {
                            errorMessage = new String(error.networkResponse.data);
                        } else {
                            errorMessage = "Error desconocido en la solicitud.";
                        }
                        Log.e("Registro", "Error en la solicitud: " + errorMessage, error);
                        Toast.makeText(RegistroUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
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

        // Validaciones
        if (!Validator.isValidCedula(cedula)) {
            Toast.makeText(this, "Cédula no válida", Toast.LENGTH_SHORT).show();
        } else if (!Validator.isValidName(nombres)) {
            Toast.makeText(this, "Nombres no válidos", Toast.LENGTH_SHORT).show();
        } else if (!Validator.isValidName(apellidos)) {
            Toast.makeText(this, "Apellidos no válidos", Toast.LENGTH_SHORT).show();
        } else if (!Validator.isValidEmail(mail)) {
            Toast.makeText(this, "Correo no válido", Toast.LENGTH_SHORT).show();
        } else if (!Validator.isValidPhoneNumber(telefono)) {
            Toast.makeText(this, "Teléfono no válido", Toast.LENGTH_SHORT).show();
        } else if (!Validator.isValidPassword(contrasena)) {
            Toast.makeText(this, "Contraseña no válida", Toast.LENGTH_SHORT).show();
        } else if (fotoPath == null) {
            Toast.makeText(this, "Debe seleccionar una imagen primero y esperar a que se suba.", Toast.LENGTH_SHORT).show();
        } else {
            enviarSolicitudRegistro(cedula, nombres, apellidos, mail, direccion, nombre_usuario, contrasena, telefono, fecha, fotoPath);
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
            jsonBody.put("fotoPath", fotoPath); // Usar fotoPath para la clave de la imagen
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRegistro, jsonBody,
                response -> {
                    Log.d("Registro", "Respuesta del servidor: " + response.toString());
                    startActivity(new Intent(RegistroUsuario.this, LoginActivity.class));
                    finish();
                }, error -> {
            String errorMessage;
            if (error.networkResponse != null && error.networkResponse.data != null) {
                errorMessage = new String(error.networkResponse.data);
            } else {
                errorMessage = "Error desconocido en la solicitud.";
            }

            Log.e("Registro", "Error en la solicitud: " + errorMessage, error);
            Toast.makeText(RegistroUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
        });

        requestQueue.add(request);
    }
}

