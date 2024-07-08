package ec.tecAzuayM5a.cuencananandroid;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ec.tecAzuayM5a.cuencananandroid.adaptador.EventosAdapter;
import ec.tecAzuayM5a.cuencananandroid.adaptador.LocalTimeAdapter;
import ec.tecAzuayM5a.cuencananandroid.modelo.Eventos;
import ec.tecAzuayM5a.cuencananandroid.modelo.EventosPuntoInteres;
import ec.tecAzuayM5a.cuencananandroid.modelo.PuntosDeInteres;
import ec.tecAzuayM5a.cuencananandroid.modelo.TipoEventos;

public class EventosActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ListView eventosListView;
    private EventosAdapter eventosAdapter;
    private List<Eventos> eventosList;
    private RequestQueue requestQueue;
    private Spinner searchTypeSpinner;
    private EditText searchInput;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventos);

        eventosListView = findViewById(R.id.points_list);
        searchTypeSpinner = findViewById(R.id.search_type);
        searchInput = findViewById(R.id.search_input);
        Button searchButton = findViewById(R.id.search_button);

        Button buttonMapa = findViewById(R.id.button_mapa);
        Button buttonPuntos = findViewById(R.id.button_puntos);
        Button buttonCasa = findViewById(R.id.button_casa);
        Button buttonEventos = findViewById(R.id.button_eventos);
        Button buttonForo = findViewById(R.id.button_foro);

        requestQueue = Volley.newRequestQueue(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment_pi);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fetchEventos();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarEventos();
            }
        });

        ///btns nav///

        // buttonForo.setOnClickListener(new View.OnClickListener() {
        //   @Override
        //   public void onClick(View view) {

        //     startActivity(new Intent(MapActivity.this, RatePuntoDeInteresActivity.class));

        //  }
        // });

        buttonMapa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EventosActivity.this, MapActivity.class));

            }
        });

        buttonCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EventosActivity.this, PerfilUsuarioActivity.class));

            }
        });

        buttonPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EventosActivity.this, PuntosDeInteresActivity.class));

            }
        });

        buttonEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(EventosActivity.this, EventosActivity.class));

            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng initialLocation = new LatLng(-2.8974172, -79.0044893);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 15));
    }

    private void fetchEventos() {
        String urlEventos = "http://192.168.0.111:8080/api/eventos";
        String urlTipoEventos = "http://192.168.0.111:8080/api/tipo_eventos";
        String urlEventosPuntoInteres = "http://192.168.0.111:8080/api/eventospuntointeres";
        String urlPuntosInteres = "http://192.168.0.111:8080/api/puntosinteres";

        JsonArrayRequest jsonArrayRequestEventos = new JsonArrayRequest(
                Request.Method.GET,
                urlEventos,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseEventos) {
                        Gson gson = new GsonBuilder()
                                .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                                .create();
                        Type listTypeEventos = new TypeToken<List<Eventos>>() {}.getType();
                        eventosList = gson.fromJson(responseEventos.toString(), listTypeEventos);

                        fetchTipoEventos(urlTipoEventos, urlEventosPuntoInteres, urlPuntosInteres);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventosActivity.this, "Error al obtener los eventos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequestEventos);
    }

    private void fetchTipoEventos(String urlTipoEventos, String urlEventosPuntoInteres, String urlPuntosInteres) {
        JsonArrayRequest jsonArrayRequestTipoEventos = new JsonArrayRequest(
                Request.Method.GET,
                urlTipoEventos,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseTipoEventos) {
                        Gson gson = new Gson();
                        Type listTypeTipoEventos = new TypeToken<List<TipoEventos>>() {}.getType();
                        List<TipoEventos> tipoEventosList = gson.fromJson(responseTipoEventos.toString(), listTypeTipoEventos);

                        Map<Long, TipoEventos> tipoEventosMap = new HashMap<>();
                        for (TipoEventos tipoEvento : tipoEventosList) {
                            tipoEventosMap.put(tipoEvento.getId_tipoEvento(), tipoEvento);
                        }

                        for (Eventos evento : eventosList) {
                            evento.setTipoEvento(tipoEventosMap.get(evento.getId_tipoEvento()));
                        }

                        fetchEventosPuntoInteres(urlEventosPuntoInteres, urlPuntosInteres);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventosActivity.this, "Error al obtener los tipos de eventos", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequestTipoEventos);
    }

    private void fetchEventosPuntoInteres(String urlEventosPuntoInteres, String urlPuntosInteres) {
        JsonArrayRequest jsonArrayRequestEventosPuntoInteres = new JsonArrayRequest(
                Request.Method.GET,
                urlEventosPuntoInteres,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responseEventosPuntoInteres) {
                        Gson gson = new Gson();
                        Type listTypeEventosPuntoInteres = new TypeToken<List<EventosPuntoInteres>>() {}.getType();
                        List<EventosPuntoInteres> eventosPuntoInteresList = gson.fromJson(responseEventosPuntoInteres.toString(), listTypeEventosPuntoInteres);

                        fetchPuntosInteres(urlPuntosInteres, eventosPuntoInteresList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventosActivity.this, "Error al obtener los eventos punto de interés", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequestEventosPuntoInteres);
    }

    private void fetchPuntosInteres(String urlPuntosInteres, List<EventosPuntoInteres> eventosPuntoInteresList) {
        JsonArrayRequest jsonArrayRequestPuntosInteres = new JsonArrayRequest(
                Request.Method.GET,
                urlPuntosInteres,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray responsePuntosInteres) {
                        Gson gson = new Gson();
                        Type listTypePuntosInteres = new TypeToken<List<PuntosDeInteres>>() {}.getType();
                        List<PuntosDeInteres> puntosInteresList = gson.fromJson(responsePuntosInteres.toString(), listTypePuntosInteres);

                        Map<Long, PuntosDeInteres> puntosInteresMap = new HashMap<>();
                        for (PuntosDeInteres puntoInteres : puntosInteresList) {
                            puntosInteresMap.put(puntoInteres.getId(), puntoInteres);
                        }

                        for (Eventos evento : eventosList) {
                            for (EventosPuntoInteres epi : eventosPuntoInteresList) {
                                if (evento.getId_evento().equals(epi.getIdEventoFk())) {
                                    PuntosDeInteres puntoInteres = puntosInteresMap.get(epi.getIdPuntoIFk());
                                    if (puntoInteres != null) {
                                        LatLng location = new LatLng(puntoInteres.getLatitud(), puntoInteres.getLongitud());
                                        evento.setPuntoInteresLocation(location);
                                        fetchFoto(puntoInteres.getIdFoto(), evento);
                                        mMap.addMarker(new MarkerOptions().position(location).title(puntoInteres.getNombre()));
                                    }
                                }
                            }
                        }

                        eventosAdapter = new EventosAdapter(EventosActivity.this, eventosList);
                        eventosListView.setAdapter(eventosAdapter);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EventosActivity.this, "Error al obtener los puntos de interés", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequestPuntosInteres);
    }

    private void fetchFoto(Long idFoto, Eventos evento) {
        String url = "http://192.168.0.111:8080/api/foto/" + idFoto;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String fotoUrl = response.getString("foto");
                        evento.setFotoUrl(fotoUrl);
                        if (eventosAdapter != null) {
                            eventosAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("EventosActivity", "Error al obtener la foto: " + error.toString())
        );
        requestQueue.add(jsonObjectRequest);
    }

    public void centerMapOnLocation(LatLng location) {
        if (mMap != null && location != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    }

    private void buscarEventos() {
        String searchType = searchTypeSpinner.getSelectedItem().toString();
        String searchText = searchInput.getText().toString().trim().toLowerCase();

        List<Eventos> filteredEventosList = new ArrayList<>();
        for (Eventos evento : eventosList) {
            if (searchType.equals("Nombre") && evento.getNombre().toLowerCase().contains(searchText)) {
                filteredEventosList.add(evento);
            } else if (searchType.equals("Categoría") && evento.getTipoEvento() != null &&
                    evento.getTipoEvento().getNombre_tipoEvento().toLowerCase().contains(searchText)) {
                filteredEventosList.add(evento);
            }
        }

        eventosAdapter = new EventosAdapter(this, filteredEventosList);
        eventosListView.setAdapter(eventosAdapter);
    }
}







