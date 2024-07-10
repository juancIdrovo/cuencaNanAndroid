package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

import java.util.List;

import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.modelo.Comentario;



    public class ComentariosAdapter extends ArrayAdapter<Comentario> {

        private final Context context;
        private final List<Comentario> comentarios;

        public ComentariosAdapter(Context context, List<Comentario> comentarios) {
            super(context, R.layout.list_comentarios, comentarios);
            this.context = context;
            this.comentarios = comentarios;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.list_comentarios, parent, false);
            }

            Comentario comentario = comentarios.get(position);

            TextView userNameTextView = convertView.findViewById(R.id.user_name);
            TextView userCommentTextView = convertView.findViewById(R.id.user_comment);
            ImageView userPhotoImageView = convertView.findViewById(R.id.user_photo);

            userNameTextView.setText(comentario.getUserName());
            userCommentTextView.setText(comentario.getComment());
            Glide.with(context).load(comentario.getUserPhotoUrl()).into(userPhotoImageView);

            return convertView;
        }
    }


