package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;

public class ForoAdapter extends ArrayAdapter<Foro> {
    private Context context;
    private List<Foro> foros;

    public ForoAdapter(Context context, List<Foro> foros) {
        super(context, 0, foros);
        this.context = context;
        this.foros = foros;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        }

        Foro foro = foros.get(position);

        TextView tituloText = convertView.findViewById(R.id.titulo);
        TextView mensajeText = convertView.findViewById(R.id.mensaje);

        tituloText.setText(foro.getTitulo());
        mensajeText.setText(foro.getRespuesta());

        return convertView;
    }
}
