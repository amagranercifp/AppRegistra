package com.example.appregistra;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.osmdroid.config.Configuration;
import org.osmdroid.views.CustomZoomButtonsController;
import org.osmdroid.views.MapView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link OpenStreetMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OpenStreetMapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public OpenStreetMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OpenStreetMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OpenStreetMapFragment newInstance(String param1, String param2) {
        OpenStreetMapFragment fragment = new OpenStreetMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Context context = requireActivity();

        View rootView = inflater.inflate(R.layout.fragment_open_street_map, container, false);

        // Configurar osmdroid antes de inicializar el mapa
        Configuration.getInstance().load(context, context.getSharedPreferences("osmdroid", MODE_PRIVATE));

        // Obtener una referencia al MapView desde tu diseño XML
        MapView map = rootView.findViewById(R.id.mapview);

        // Configurar el mapa
        map.setTileSource(org.osmdroid.tileprovider.tilesource.TileSourceFactory.MAPNIK);
        map.getZoomController().setVisibility(CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT);
        map.setMultiTouchControls(true);

        // Inflate the layout for this fragment
        return rootView;
    }
}