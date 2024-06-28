package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.R;
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

        nameText.setText(punto.getNombre());
        categoriaText.setText(punto.getCategoria()); // Mostrar categoría

        return convertView;
    }
}