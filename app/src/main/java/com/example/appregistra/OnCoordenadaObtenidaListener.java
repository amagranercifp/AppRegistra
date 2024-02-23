package com.example.appregistra;

import com.example.appregistra.datos.Ubicacion;

public interface OnCoordenadaObtenidaListener {
    void onCoordenadaObtenida(Ubicacion ubicacion);

    void onCoordenadaObtenidaTransicion();
}