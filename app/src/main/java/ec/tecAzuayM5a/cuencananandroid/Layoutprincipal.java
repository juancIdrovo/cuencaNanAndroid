package ec.tecAzuayM5a.cuencananandroid;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import ec.tecAzuayM5a.cuencananandroid.databinding.ActivityLayoutprincipalBinding;

public class Layoutprincipal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityLayoutprincipalBinding binding;
    private String userName;
    private String userEmail;
    private Uri imageUri;
    private String cedula;
    Button btnmodificar,  btnCrearUbicacion, btnVerUbicaciones, btnPuntosInteres;
    ImageView opt;
    String url = "http://192.168.18.17:8080/api/usuarios/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLayoutprincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userName = getIntent().getStringExtra("nombre");
        cedula = getIntent().getStringExtra("long_id");
        userEmail = getIntent().getStringExtra("mail");
     //   imageUri = Uri.parse(getIntent().getStringExtra("image_uri"));
        updateUI();
        setSupportActionBar(binding.appBarLayoutprincipal.toolbar);
        binding.appBarLayoutprincipal.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null)
                        .setAnchorView(R.id.fab).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_fragment_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_puntosInteres)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_layoutprincipal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.layoutprincipal, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_layoutprincipal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    private void updateUI() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvMail = headerView.findViewById(R.id.tvMail);
        //ImageView ivUserImage = findViewById(R.id.fotoPerfil);
        if (tvName != null && tvMail != null) {
            tvName.setText(userName);
            tvMail.setText(userEmail);
        } else {
            Log.e("Layoutprincipal", "TextView es nulo");
        }
        // Verifica que la URL de la imagen no sea nula
     /*   if (imageUri != null) {
            // Intenta cargar la imagen con Glide
            Glide.with(this)
                    .load(imageUri)
                    .apply(new RequestOptions()
                            // Imagen de marcador de posición en caso de error
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // Manejar fallo de carga de imagen aquí
                            Log.e("PerfilUsuarioActivity", "Error al cargar la imagen con Glide: " + e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            // La imagen se cargó correctamente
                            return false;
                        }
                    })
                    .into(ivUserImage);
        } else {
            // URL de la imagen nula o vacía, usa una imagen de marcador de posición
            ivUserImage.setImageResource(R.drawable.luffiperfil);
            */


    }
}