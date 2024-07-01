package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class RatePuntoDeInteresActivity extends AppCompatActivity {
    private TextView nombreText;
    private TextView descripcionText;
    private ImageView fotoView;
    private EditText comentarioEditText;
    private RatingBar ratingBar;
    private Button submitButton;
    private Long puntoInteresId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_punto_de_interes);

        nombreText = findViewById(R.id.nombre_text);
        descripcionText = findViewById(R.id.descripcion_text);
        fotoView = findViewById(R.id.foto_view);
        comentarioEditText = findViewById(R.id.comentario_edit_text);
        ratingBar = findViewById(R.id.rating_bar);
        submitButton = findViewById(R.id.submit_button);

        puntoInteresId = getIntent().getLongExtra("PUNTO_INTERES_ID", -1);

        // Aquí deberías cargar los detalles del punto de interés usando puntoInteresId
        // y mostrarlos en los TextViews y el ImageView.

        // Ejemplo de cómo cargar los datos (esto debe ser reemplazado con una solicitud a tu API)
        cargarPuntoDeInteres(puntoInteresId);

        submitButton.setOnClickListener(v -> {
            // Aquí deberías enviar la calificación y el comentario al servidor.
            enviarCalificacionYComentario();
        });
    }

    private void cargarPuntoDeInteres(Long id) {
        // Aquí deberías hacer una solicitud a tu API para obtener los detalles del punto de interés
        // y actualizar los TextViews y el ImageView con los datos recibidos.

        // Ejemplo de datos estáticos
        String nombre = "Parque Abdón Calderón";
        String descripcion = "Oficialmente Parque Mayor Abdón Calderón​, antiguamente conocida también como Plaza República o Plaza de Armas.";
        String fotoUrl = "https://practicasjohnsiguenza.s3.amazonaws.com/8fa81ac4-f1a6-4d8a-b5a1-4c5241b7a80e.jpg";

        nombreText.setText(nombre);
        descripcionText.setText(descripcion);

        Glide.with(this)
                .load(fotoUrl)
                .placeholder(R.drawable.placeholder)
                .into(fotoView);
    }

    private void enviarCalificacionYComentario() {
        // Aquí deberías implementar la lógica para enviar la calificación y el comentario al servidor.
    }
}



