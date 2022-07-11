package com.upbmovil.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.upbmovil.proyectofinal.datos.AdapterVacantes;
import com.upbmovil.proyectofinal.modelo.Vacantes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VacantesActivity extends AppCompatActivity {

    ProgressBar pbApi;
    LinearLayout panel;

    RecyclerView mRVLista;
    AdapterVacantes mAdapter;
    ArrayList<Vacantes> listaVacantesApi;
    Vacantes vac = new Vacantes();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacantes);

        panel = findViewById(R.id.panel);
        pbApi = findViewById(R.id.progressBarApi);
        pbApi.setVisibility(View.GONE);

        mRVLista = findViewById(R.id.rvLista);
        mRVLista.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRVLista.setLayoutManager(layoutManager);

        listaVacantesApi = new ArrayList<>();
        datosApiii();

    }

    public void datosApiii (){
        pbApi.setVisibility(View.VISIBLE);
        panel.setVisibility(View.VISIBLE);
        SharedPreferences sharedPref = getSharedPreferences("estudiante",Context.MODE_PRIVATE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://talentoupb.000webhostapp.com/api/vacantes?token="+sharedPref.getString("token", "");

        try {


            JSONObject datosJson = new JSONObject();

            datosJson.put("token", sharedPref.getString("token", ""));

            JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.GET, url, datosJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //

                            try {
                                JSONArray datos = response.getJSONArray("data");
                                if (datos.length()>0){
                                    listaVacantesApi = new ArrayList<>();
                                    for(int posicion=0;posicion<datos.length();posicion++){
                                        JSONObject registro = datos.getJSONObject(posicion);
                                        Vacantes vTemp = new Vacantes();
                                        vTemp.setNombre(registro.getString("nombre"));
                                        vTemp.setDetalle(registro.getString("detalle"));
                                        listaVacantesApi.add(vTemp);
                                    }
                                    mAdapter = new AdapterVacantes(listaVacantesApi);

                                    mRVLista.setAdapter(mAdapter);
                                    pbApi.setVisibility(View.GONE);
                                    panel.setVisibility(View.GONE);
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                pbApi.setVisibility(View.GONE);
                                panel.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                            pbApi.setVisibility(View.GONE);
                            panel.setVisibility(View.GONE);
                        }
                    });
            queue.add(strRequest);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    public void recargar(View view) {
        datosApiii();
    }
}