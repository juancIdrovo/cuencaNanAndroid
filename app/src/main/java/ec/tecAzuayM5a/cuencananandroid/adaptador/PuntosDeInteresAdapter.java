package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;

import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.PuntosDeInteresActivity;
import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.RatePuntoDeInteresActivity;
import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;

public class PuntosDeInteresAdapter extends ArrayAdapter<PuntosDeInteres> {
    private Context context;
    private List<PuntosDeInteres> puntosDeInteres;

    public PuntosDeInteresAdapter(Context context, List<PuntosDeInteres> puntosDeInteres) {
        super(context, 0, puntosDeInteres);
        this.context = context;
        this.puntosDeInteres = puntosDeInteres;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_punto_interes, parent, false);
        }

        PuntosDeInteres punto = puntosDeInteres.get(position);

        TextView nameText = convertView.findViewById(R.id.name_text);
        TextView categoriaText = convertView.findViewById(R.id.categoria_txt);
        TextView descripcionText = convertView.findViewById(R.id.descripcion_txt);
        ImageView fotoView = convertView.findViewById(R.id.foto_view);
        TextView ratingText = convertView.findViewById(R.id.rating_text); // TextView para mostrar el rating
        Button rateButton = convertView.findViewById(R.id.rate_button);
        Button centerMapButton = convertView.findViewById(R.id.center_map_button); // Botón para centrar el mapa

        nameText.setText(punto.getNombre());
        categoriaText.setText(punto.getCategoria());
        descripcionText.setText(punto.getDescripcion());

        if (punto.getFoto() != null && punto.getFoto().getUrl() != null) {
            Glide.with(context)
                    .load(punto.getFoto().getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(fotoView);
        } else {
            fotoView.setImageResource(R.drawable.placeholder);
        }

        // Obtener la media de las calificaciones
        fetchAverageRating(punto.getId(), ratingText);

        rateButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, RatePuntoDeInteresActivity.class);
            intent.putExtra("PUNTO_INTERES_ID", punto.getId());
            context.startActivity(intent);
        });

        centerMapButton.setOnClickListener(v -> {
            if (context instanceof PuntosDeInteresActivity) {
                ((PuntosDeInteresActivity) context).updateMap(punto);
            }
        });

        return convertView;
    }

    private void fetchAverageRating(Long puntoInteresId, TextView ratingText) {
        String url = "http://192.168.0.75:8080/api/puntosinteres/" + puntoInteresId + "/media-calificaciones";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Double averageRating = Double.parseDouble(response);
                        ratingText.setText(String.format("Rating: %.1f", averageRating));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ratingText.setText("Rating: N/A");
                    }
                },
                error -> {
                    Log.e("PuntosDeInteres", "Error al obtener el rating: " + error.toString());
                    ratingText.setText("Rating: N/A");
                }
        );

        Volley.newRequestQueue(context).add(stringRequest);
    }

}





