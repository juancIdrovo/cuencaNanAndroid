package ec.tecAzuayM5a.cuencananandroid.adaptador;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import java.util.List;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

import ec.tecAzuayM5a.cuencananandroid.ForoActivity;
import ec.tecAzuayM5a.cuencananandroid.ForoUsuarioActivity;
import ec.tecAzuayM5a.cuencananandroid.ModificarForo;
import ec.tecAzuayM5a.cuencananandroid.PuntosDeInteresActivity;
import ec.tecAzuayM5a.cuencananandroid.R;
import ec.tecAzuayM5a.cuencananandroid.RatePuntoDeInteresActivity;
import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;
import ec.tecAzuayM5a.cuencananandroid.modificarUsuario;

public class ForoUsuarioAdapter extends ArrayAdapter<Foro> {
    private Context context;
    private List<Foro> foros;

    public ForoUsuarioAdapter(Context context, List<Foro> foros) {
        super(context, 0, foros);
        this.context = context;
        this.foros = foros;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_view2, parent, false);
        }

        Foro foro = foros.get(position);

        TextView tituloText = convertView.findViewById(R.id.titulo);
        TextView mensajeText = convertView.findViewById(R.id.mensaje);
        ImageView fotoView = convertView.findViewById(R.id.ivPost);
        ImageView usuarioFotoView = convertView.findViewById(R.id.ivProfile);
        Button btnmodificar = convertView.findViewById(R.id.btnmodificarforo);
        Button btnEliminar = convertView.findViewById(R.id.btnEliminarforo);


        tituloText.setText(foro.getTitulo());
        mensajeText.setText(foro.getRespuesta());

        if (foro.getFoto() != null && foro.getFoto().getUrl() != null) {
            Glide.with(context)
                    .load(foro.getFoto().getUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(fotoView);
        } else {
            fotoView.setImageResource(R.drawable.placeholder);
        }

        if (foro.getUsuarioFotoUrl() != null) {
            Glide.with(context)
                    .load(foro.getUsuarioFotoUrl())
                    .placeholder(R.drawable.placeholder)
                    .into(usuarioFotoView);
        } else {
            new ForoUsuarioAdapter.GetUsuarioFotoTask(usuarioFotoView, foro).execute(foro.getIdUsuario());
        }
        btnmodificar.setOnClickListener(v -> {
            Intent intent = new Intent(context, ModificarForo.class);
            intent.putExtra("id_foro", foro.getIdForo());
            intent.putExtra("id_usuario", foro.getIdUsuario());
            context.startActivity(intent);
        });
        btnEliminar.setOnClickListener(v -> {
            Intent intent = new Intent(context, ForoActivity.class);
            String url = "http://192.168.1.25:8080/api/foros/" + foro.getIdForo();
            StringRequest deleteRequest = new StringRequest(Request.Method.DELETE, url,
                    response -> {
                        Toast.makeText(context, "Foro eliminado con éxito", Toast.LENGTH_SHORT).show();
                        String long_id2 = foro.getIdUsuario().toString();
                        intent.putExtra("id_usuario", long_id2);
                        context.startActivity(intent);


                    },
                    error -> {

                        Toast.makeText(context, "Error al eliminar el foro", Toast.LENGTH_SHORT).show();
                    }
            );
            Volley.newRequestQueue(context).add(deleteRequest);
        });
        return convertView;
    }
    private class GetUsuarioFotoTask extends AsyncTask<Long, Void, String> {
        private ImageView usuarioFotoView;
        private Foro foro;

        public GetUsuarioFotoTask(ImageView usuarioFotoView, Foro foro) {
            this.usuarioFotoView = usuarioFotoView;
            this.foro = foro;
        }


        @Override
        protected String doInBackground(Long... params) {
            Long idUsuario = params[0];
            try {
                URL url = new URL("http://192.168.137.83:8080/api/usuarios/" + idUsuario);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/json");

                if (connection.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "
                            + connection.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

                String output;
                StringBuilder response = new StringBuilder();
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }

                connection.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("fotoUrl");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String fotoUrl) {
            if (fotoUrl != null) {
                foro.setUsuarioFotoUrl(fotoUrl);
                Glide.with(context)
                        .load(fotoUrl)
                        .placeholder(R.drawable.placeholder)
                        .into(usuarioFotoView);
            } else {
                usuarioFotoView.setImageResource(R.drawable.placeholder);
            }
        }
    }

}