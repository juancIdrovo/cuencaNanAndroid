package ec.tecAzuayM5a.cuencananandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.zip.Inflater;

import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Foro> arrayList;

    public RecyclerAdapter(ArrayList<Foro> arrayList){
        this.arrayList = arrayList;

    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
Foro foro = arrayList.get(position);
holder.titulo.setText(foro.getTitulo());
holder.mensaje.setText(foro.getMensaje());
holder.profileImagen.setImageResource(foro.getIconoperfil());
holder.post.setImageResource(foro.getImagen());
    }

    @Override
    public int getItemCount() {
       return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView profileImagen;
        ImageView post;
        TextView titulo;
        TextView mensaje;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            profileImagen = itemView.findViewById(R.id.ivProfile);
post = itemView.findViewById(R.id.ivPost);
titulo = itemView.findViewById(R.id.titulo);
mensaje = itemView.findViewById(R.id.mensaje);
        }

    }
}
