package com.example.david.diariodemisviajes;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public final class ViajeSingleton {

    private static ViajeSingleton viajeSingleton;
    private RequestQueue miColaDePeticiones;
    private static Context contexto;

    //Constructor --------------------------------
    private ViajeSingleton(Context context) {
        this.contexto = context;
        miColaDePeticiones = getRequestQueue();
    }

    public static synchronized ViajeSingleton getInstance(Context context) {
        if (viajeSingleton == null) {
            viajeSingleton = new ViajeSingleton(context);
        }
        return viajeSingleton;
    }

    public RequestQueue getRequestQueue() {
        if (miColaDePeticiones == null) {
            miColaDePeticiones = Volley.newRequestQueue(contexto.getApplicationContext());
        }
        return miColaDePeticiones;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

}

