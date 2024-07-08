package ec.tecAzuayM5a.cuencananandroid;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalTime;

import ec.tecAzuayM5a.cuencananandroid.adaptador.LocalTimeAdapter;

    public class GsonHelper {
        private static Gson gson;

        public static Gson getGson() {
            if (gson == null) {
                gson = new GsonBuilder()
                        .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
                        .create();
            }
            return gson;
        }
    }

