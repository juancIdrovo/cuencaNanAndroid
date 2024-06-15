package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoPuntoInteres;

public class TipoPuntoInteresAdapter extends BaseAdapter {
    private Context context;
    private List<TipoPuntoInteres> puntosDeInteres;

    public TipoPuntoInteresAdapter(Context context, List<TipoPuntoInteres> puntosDeInteres) {
        this.context = context;
        this.puntosDeInteres = puntosDeInteres;
    }

    @Override
    public int getCount() {
        return puntosDeInteres.size();
    }

    @Override
    public Object getItem(int position) {
        return puntosDeInteres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return puntosDeInteres.get(position).getIdtipospuntosinteres();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_punto_interes, parent, false);
        }

        TextView categoryText = convertView.findViewById(R.id.category_text);
        TextView nameText = convertView.findViewById(R.id.name_text);
        TextView descriptionText = convertView.findViewById(R.id.description_text);

        TipoPuntoInteres punto = puntosDeInteres.get(position);

        categoryText.setText(punto.getCategoria());
        nameText.setText(punto.getNombre());
        descriptionText.setText(punto.getDescripcion());

        return convertView;
    }
}
