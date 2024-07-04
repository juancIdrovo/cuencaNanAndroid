package ec.tecAzuayM5a.cuencananandroid;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    Button btnAceptar, btnRegistarse;
    EditText txtEmail, txtPass;

    String mail, pass;
    String url = "http://192.168.18.17:8080/api/usuarios/loginusuario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        btnAceptar = findViewById(R.id.btnLogIn);
        btnRegistarse = findViewById(R.id.btnRegistro);
        txtEmail = findViewById(R.id.inputEmail);
        txtPass = findViewById(R.id.inputPassword);

        btnRegistarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistroUsuario.class));
            }
        });

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login(v);
            }
        });
    }

    public void Login(View view) {
        if (txtEmail.getText().toString().equals("")) {
            Toast.makeText(this, "Ingrese el correo", Toast.LENGTH_LONG).show();
        } else if (txtPass.getText().toString().equals("")) {
            Toast.makeText(this, "Ingrese la contraseña", Toast.LENGTH_LONG).show();
        } else {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Espere un momento...");
            progressDialog.show();
            mail = txtEmail.getText().toString().trim();
            pass = txtPass.getText().toString().trim();
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("mail", mail);
                jsonBody.put("contrasena", pass);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            try {
                                String status = response.optString("status", "");
                                if (!status.equals("error")) {

                                    String nombre = response.optString("nombres", "");
                                    String correo = response.optString("mail", "");
                                    String id = response.optString("id_usuario", "");
                                    String fotoPath = response.optString("fotoPath", "");
                                    String fotoUrl = response.optString("fotoUrl", "");

                                    SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putLong("userId", Long.parseLong(id));
                                    editor.apply();

                                    Intent intent = new Intent(LoginActivity.this, PerfilUsuarioActivity.class);
                                    intent.putExtra("nombre", nombre);
                                    intent.putExtra("mail", correo);
                                    intent.putExtra("id_usuario", id);
                                    intent.putExtra("fotoPath", fotoPath);
                                    intent.putExtra("fotoUrl", fotoUrl);
                                    Log.d("LoginActivity", "Respuesta del servidor: " + response.toString());

                                    finish();
                                    startActivity(intent);
                                } else {
                                    Log.d("LoginActivity", "Autenticación fallida para el correo: " + mail);
                                    Toast.makeText(LoginActivity.this, "Autenticación fallida", Toast.LENGTH_LONG).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    progressDialog.dismiss();
                    Log.e("LoginActivity", "Error en la solicitud: " + error.getMessage());
                    Toast.makeText(LoginActivity.this, "Error en la solicitud: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(request);
        }
    }
}



