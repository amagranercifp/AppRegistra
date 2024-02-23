package com.example.appregistra.datos;

import android.os.Parcelable;

import java.io.Serializable;

public class Tarea implements Serializable {

    String idTarea;
    String nombreTecnico;

    Ubicacion uInicial,uFinal;

    String momentoInicial;

    String momentoFinal;

    String descripcion;


    public Tarea(String idTarea, String nombreTecnico, Ubicacion uInicial, String momentoInicial) {
        this.idTarea = idTarea;
        this.nombreTecnico = nombreTecnico;
        this.uInicial = uInicial;
        this.momentoInicial = momentoInicial;
    }

    public String getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(String idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public Ubicacion getuInicial() {
        return uInicial;
    }

    public void setuInicial(Ubicacion uInicial) {
        this.uInicial = uInicial;
    }

    public Ubicacion getuFinal() {
        return uFinal;
    }

    public void setuFinal(Ubicacion uFinal) {
        this.uFinal = uFinal;
    }

    public String getMomentoInicial() {
        return momentoInicial;
    }

    public void setMomentoInicial(String momentoInicial) {
        this.momentoInicial = momentoInicial;
    }

    public String getMomentoFinal() {
        return momentoFinal;
    }

    public void setMomentoFinal(String momentoFinal) {
        this.momentoFinal = momentoFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
