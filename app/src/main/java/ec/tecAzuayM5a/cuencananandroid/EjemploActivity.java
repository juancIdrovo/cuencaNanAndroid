package ec.tecAzuayM5a.cuencananandroid;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ec.tecAzuayM5a.cuencananandroid.modelo.Foro;

public class EjemploActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ArrayList<Foro> arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ejemplo);
        arrayList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerview);
        arrayList.add(new Foro(R.drawable.ic_launcher_background,R.drawable.opcion64  ,"title","mensaje"))

    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(arrayList);
    recyclerView.setAdapter(recyclerAdapter);
    }
}
