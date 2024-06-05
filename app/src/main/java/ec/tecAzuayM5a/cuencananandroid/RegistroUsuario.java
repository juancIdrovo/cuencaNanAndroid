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

    private String urlRegistro = "http://192.168.1.39:8080/api/usuarios";
    private RequestQueue requestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuario);
        requestQueue = Volley.newRequestQueue(this);

    }
        public void clickbtnGuardar(View view) {
        String idUsuario = ((TextInputEditText) findViewById(R.id.txtcedula)).getText().toString().trim();
        String cedula = ((TextInputEditText) findViewById(R.id.txtnombres)).getText().toString().trim();
        String nombres = ((TextInputEditText) findViewById(R.id.txtapellidos)).getText().toString().trim();

        JSONObject jsonBody = new JSONObject();

        try {
            jsonBody.put("idUsuario", idUsuario);
            jsonBody.put("cedula", cedula);
            jsonBody.put("nombres", nombres);

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
}
