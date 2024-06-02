package ec.tecAzuayM5a.cuencananandroid;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
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

import ec.tecAzuayM5a.cuencananandroid.modelo.Usuario;

public class modificarUsuario extends AppCompatActivity {
    EditText txtNombres, txtApellidos, txtCorreo, txtDireccion, txtTelefono, txtcedula, txtFecha, txtContrasena;
    Button btnGuarda, btnCancelar;
    Button btnSeleccionarFoto;

    private static final int REQUEST_IMAGE_PICK = 1;
    private String userName, apellidos, direccion, contrasena, telefono, fecha_nac;
    private String userEmail;
    private Uri imageUri;
    private String cedula;
    private ImageView imageView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_datos_estudiante);

        userName = getIntent().getStringExtra("user_name");
        cedula = getIntent().getStringExtra("cedula");
        apellidos = getIntent().getStringExtra("apellidos");
        direccion = getIntent().getStringExtra("direccion");
        telefono = getIntent().getStringExtra("telefono");
        userEmail = getIntent().getStringExtra("user_email");
        imageUri = Uri.parse(getIntent().getStringExtra("image_uri"));
        contrasena  = getIntent().getStringExtra("contrasena");
        fecha_nac = getIntent().getStringExtra("fecha_nac");


        txtNombres = findViewById(R.id.txtnombres);
        txtApellidos = findViewById(R.id.txtapellidos);
        txtCorreo = findViewById(R.id.txtcorreo);
        txtDireccion = findViewById(R.id.txtdireccion);
        txtTelefono = findViewById(R.id.txttelf);
        txtcedula = findViewById(R.id.txtcedula);
        txtFecha = findViewById(R.id.txtFechaNac);
        txtContrasena = findViewById(R.id.txtcontrasena);
        btnGuarda = findViewById(R.id.btnGuardar);
        imageView = findViewById(R.id.fotoPerfil);
        btnCancelar = findViewById(R.id.btnCancelar);
        btnSeleccionarFoto = findViewById(R.id.btnfotito);
        updateUI();
        btnSeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            }
        });

        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener datos del estudiante modificado
                String nuevosNombres = txtNombres.getText().toString();
                String nuevosApellidos = txtApellidos.getText().toString();
                String nuevoCorreo = txtCorreo.getText().toString();
                String nuevaDireccion = txtDireccion.getText().toString();
                String nuevoTelefono = txtTelefono.getText().toString();
                String nuevaContrasena = txtContrasena.getText().toString();
                String nuevaFecha = ((TextInputEditText) findViewById(R.id.txtFechaNac)).getText().toString().trim();
                Date fecha = null;
                if (!nuevaFecha.isEmpty()) {
                    try {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        fecha = sdf.parse(nuevaFecha);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        // Manejar la excepción si la cadena no se puede convertir a Date
                    }
                }


                Usuario estudianteModificado = new Usuario(cedula, nuevosNombres, nuevosApellidos, nuevoCorreo, nuevaDireccion, nuevoTelefono, null, fecha, nuevaContrasena, cedula);

                updateStudent(estudianteModificado,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(modificarUsuario.this, "Estudiante modificado correctamente", Toast.LENGTH_SHORT).show();
                                Toast.makeText(modificarUsuario.this, "Vuelva a iniciar sesion para que se Actualizen sus datos", Toast.LENGTH_SHORT).show();
onBackPressed();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VolleyError", "Error al modificar estudiante: " + error.getMessage(), error);
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    String responseBody = new String(error.networkResponse.data);
                                    Log.e("VolleyError", "Respuesta del servidor: " + responseBody);
                                }
                                // Mostrar un mensaje de error al usuario (puedes personalizarlo según tus necesidades)
                                Toast.makeText(modificarUsuario.this, "Error al modificar estudiante. Consulta el LogCat para más detalles.", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
            }
        });
        Button btnCancelar = findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Agregar el código para ir a la actividad "perfil_usuario"
                onBackPressed();

            }
        });
    }
    private void updateStudent(Usuario usuario, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = "http://192.168.43.81:8080/api/estudiantes/" + usuario.getCedula();

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("nombres", usuario.getNombres());
            jsonBody.put("apellidos", usuario.getApellidos());
            jsonBody.put("correo", usuario.getCorreo());
            jsonBody.put("direccion", usuario.getDireccion());
            jsonBody.put("telf", usuario.getTelf());
            jsonBody.put("contrasena", usuario.getContrasena());
            if (usuario.getFecha_nac() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                String fechaFormateada = sdf.format(usuario.getFecha_nac());
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



        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonBody, successListener, errorListener);

        requestQueue.add(request);
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
    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
    private void updateUI() {
        TextView tvName = findViewById(R.id.txtnombres);
        TextView tvMail = findViewById(R.id.txtcorreo);
        TextView tvapellidos = findViewById(R.id.txtapellidos);
        TextView tvcontrasenia = findViewById(R.id.txtcontrasena);
        TextView tvdireccion = findViewById(R.id.txtdireccion);
        TextView tvcedula = findViewById(R.id.txtcedula);
        TextView tvTelefono = findViewById(R.id.txttelf);
        TextView tvFecha = findViewById(R.id.txtFechaNac);

        ImageView ivUserImage = findViewById(R.id.fotoPerfil);

        tvName.setText(userName);
        tvMail.setText(userEmail);
        tvapellidos.setText(apellidos);
        tvdireccion.setText(direccion);
        tvcedula.setText(cedula);
        tvTelefono.setText(telefono);
        tvcontrasenia.setText(contrasena);
        tvFecha.setText(fecha_nac);


        // Verifica que la URL de la imagen no sea nula
        if (imageUri != null) {
            // Intenta cargar la imagen con Glide
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                             // Imagen de marcador de posición mientras carga
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


