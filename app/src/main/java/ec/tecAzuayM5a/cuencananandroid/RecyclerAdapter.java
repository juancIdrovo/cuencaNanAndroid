package ec.tecAzuayM5a.cuencananandroid;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Foro> arrayList;

    public RecyclerAdapter(ArrayList<Foro> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Foro foro = arrayList.get(position);
        holder.titulo.setText("Foro " + foro.getIdForo());
        holder.mensaje.setText(foro.getRespuesta());
        // Puedes configurar las imágenes de perfil y post según sea necesario
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProfile, ivPost, ivLike, ivComment;
        TextView titulo, mensaje;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProfile = itemView.findViewById(R.id.ivProfile);
            ivPost = itemView.findViewById(R.id.ivPost);
            ivLike = itemView.findViewById(R.id.ivLike);
            ivComment = itemView.findViewById(R.id.ivComment);
            titulo = itemView.findViewById(R.id.titulo);
            mensaje = itemView.findViewById(R.id.mensaje);
        }
    }
}
