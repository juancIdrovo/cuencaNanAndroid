package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ec.tecAzuayM5a.cuencananandroid.EventosActivity;
import ec.tecAzuayM5a.cuencananandroid.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import ec.tecAzuayM5a.cuencananandroid.modelo.Eventos;

public class EventosAdapter extends BaseAdapter {

    private Context context;
    private List<Eventos> eventosList;
    private LayoutInflater inflater;

    public EventosAdapter(Context context, List<Eventos> eventosList) {
        this.context = context;
        this.eventosList = eventosList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return eventosList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventosList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_eventos, parent, false);
        }

        TextView nameText = convertView.findViewById(R.id.name_value);
        TextView categoriaText = convertView.findViewById(R.id.categoria_value);
        TextView descripcionText = convertView.findViewById(R.id.descripcion_value);
        TextView fechaIniText = convertView.findViewById(R.id.fechaIni_value);
        TextView horaIniText = convertView.findViewById(R.id.horaIni_value);
        TextView fechaFinText = convertView.findViewById(R.id.fechaFin_value);
        TextView horaFinText = convertView.findViewById(R.id.horaFin_value);
        ImageView fotoView = convertView.findViewById(R.id.foto_view);
        Button centerMapButton = convertView.findViewById(R.id.center_map_button);

        Eventos evento = eventosList.get(position);

        nameText.setText(evento.getNombre());
        if (evento.getTipoEvento() != null) {
            categoriaText.setText(evento.getTipoEvento().getNombre_tipoEvento());
            descripcionText.setText(evento.getTipoEvento().getDescripcion());
        } else {
            categoriaText.setText("Sin categoría");
            descripcionText.setText("Sin descripción");
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        fechaIniText.setText(dateFormat.format(evento.getFecha_Inicio()));
        horaIniText.setText(evento.getHora_Inicio().toString());
        fechaFinText.setText(dateFormat.format(evento.getFecha_Fin()));
        horaFinText.setText(evento.getHora_Fin().toString());

        if (evento.getFotoUrl() != null) {
            Glide.with(context).load(evento.getFotoUrl()).into(fotoView);
        }

        centerMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context instanceof EventosActivity) {
                    ((EventosActivity) context).centerMapOnLocation(evento.getPuntoInteresLocation());
                }
            }
        });

        return convertView;
    }
}






