package ec.tecAzuayM5a.cuencananandroid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RegistroUsuario extends AppCompatActivity {

    private String urlRegistro = "http://192.168.0.209:8080/api/usuarios";
    private RequestQueue requestQueue;
    private static final int REQUEST_IMAGE_PICK = 1;
    private ImageView imageView;
    private Uri imageUri;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);

        requestQueue = Volley.newRequestQueue(this);
        imageView = findViewById(R.id.imageViewfoto);

        Button btnSeleccionarFoto = findViewById(R.id.btnSeleccionarFoto);

        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the chosen date
            String selectedDate = String.format(Locale.US, "%04d-%02d-%02d", year, month + 1, day);
            ((TextInputEditText) getActivity().findViewById(R.id.txtFechaNac)).setText(selectedDate);
        }
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
        public void clickbtnGuardar(View view) {
        String cedula = ((TextInputEditText) findViewById(R.id.txtcedula)).getText().toString().trim();
        String nombres = ((TextInputEditText) findViewById(R.id.txtnombres)).getText().toString().trim();
        String apellidos = ((TextInputEditText) findViewById(R.id.txtapellidos)).getText().toString().trim();
        String correo = ((TextInputEditText) findViewById(R.id.txtcorreo)).getText().toString().trim();
        String direccion = ((TextInputEditText) findViewById(R.id.txtdireccion)).getText().toString().trim();
        String telf = ((TextInputEditText) findViewById(R.id.txttelf)).getText().toString().trim();
        String contrasena = ((TextInputEditText) findViewById(R.id.txtContrasena)).getText().toString().trim();
        String fechaString = ((TextInputEditText) findViewById(R.id.txtFechaNac)).getText().toString().trim();
        Date fecha = null;
        String tipoUsuario = ((TextInputEditText) findViewById(R.id.txtEstudiante)).getText().toString().trim();

        if (!fechaString.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                fecha = sdf.parse(fechaString);
            } catch (ParseException e) {
                e.printStackTrace();
                // Manejar la excepción si la cadena no se puede convertir a Date
            }
        }

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("cedula", cedula);
            jsonBody.put("nombres", nombres);
            jsonBody.put("apellidos", apellidos);
            jsonBody.put("correo", correo);
            jsonBody.put("direccion", direccion);
            jsonBody.put("telf", telf);
            jsonBody.put("contrasena", contrasena);
            jsonBody.put("cedula_estudiante_fk", cedula);
            jsonBody.put("contrasena", contrasena);
            jsonBody.put("tipoUsuario", tipoUsuario);
            if (fecha != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fechaFormateada = sdf.format(fecha);
                jsonBody.put("fecha_nac", fechaFormateada);
            } else {
                Log.e("RegistroEstudiante", "La fecha es null, no se añadió al JSONObject");
            }

            if (imageUri != null) {
                String base64Image = convertirImagenBase64(imageUri);
                jsonBody.put("foto", base64Image);
            } else {
                Log.e("RegistroEstudiante", "imageUri es null en clickbtnGuardar");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlRegistro, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("RegistroEstudiante", "Respuesta del servidor: " + response.toString());
                        startActivity(new Intent(RegistroUsuario.this, LoginActivity.class));
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String errorMessage;
                if (error.networkResponse != null && error.networkResponse.data != null) {
                    // Si hay datos en la respuesta, intenta convertirlos a una cadena para obtener más información.
                    errorMessage = new String(error.networkResponse.data);
                } else {
                    errorMessage = "Error desconocido en la solicitud.";
                }

                Log.e("RegistroEstudiante", "Error en la solicitud: " + errorMessage, error);
                Toast.makeText(RegistroUsuario.this, "Error en la solicitud: " + errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);
    }

    private String convertirImagenBase64(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            inputStream.close();
            return "data:image/jpeg;base64," + Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RegistroEstudiante", "Error al convertir imagen a base64");
            return null;
        }
    }
}
