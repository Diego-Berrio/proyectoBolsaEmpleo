package com.upbmovil.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.upbmovil.proyectofinal.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;

public class HomeEstActivity extends AppCompatActivity {

    SharedPreferences sharedPref;
    TextView tvId, tvNombres, tvApellidos, tvCorreo, tvTelefono;
    ProgressBar pbEsperar;
    Button btnVacantes, btnSalir;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_est);
        btnVacantes = findViewById(R.id.btnVacantes);
        btnSalir = findViewById(R.id.btnSalir);
        pbEsperar = findViewById(R.id.pbEsperar);
        pbEsperar.setVisibility(View.GONE);


        sharedPref = getSharedPreferences("estudiante", Context.MODE_PRIVATE);
        tvId = findViewById(R.id.tvId);
        tvNombres = findViewById(R.id.tvNombres);
        tvApellidos = findViewById(R.id.tvApellidos);
        tvCorreo = findViewById(R.id.tvCorreo);
        tvTelefono = findViewById(R.id.tvTelefono);

        //int id = sharedPref.getInt("id_UPB",0);
        //tvId.setText(String.valueOf(id));

        //SharedPreferences.Editor editor = sharedPref.edit();
        //editor.putString("id_UPB",String.valueOf();
        //editor.apply();
        long id = sharedPref.getLong("id_UPB",0);
        tvId.setText(String.valueOf(id));
        tvNombres.setText(sharedPref.getString("nombre",""));
        tvApellidos.setText(sharedPref.getString("apellido",""));
        tvCorreo.setText(sharedPref.getString("correo",""));
        tvTelefono.setText(sharedPref.getString("telefono",""));

    }



    public void pantallaVacantes(View view) {
        btnVacantes.setVisibility(View.GONE);
        btnSalir.setVisibility(View.GONE);
        pbEsperar.setVisibility(View.VISIBLE);
        CountDownTimer con = new CountDownTimer(1000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                Intent in = new Intent(getApplicationContext(),VacantesActivity.class);
                startActivity(in);
                finish();
            }
        };

        con.start();
    }

    public void Salir(View view) {
        btnVacantes.setVisibility(View.GONE);
        btnSalir.setVisibility(View.GONE);
        pbEsperar.setVisibility(View.VISIBLE);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Api.URL_BASE+"/estudiante/logout";

        try {

            JSONObject datosJson = new JSONObject();
            datosJson.put("id_UPB", sharedPref.getLong("id_UPB", 0));
            datosJson.put("token", sharedPref.getString("token", ""));

            JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, url, datosJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //
                            Intent i;
                            try {
                                boolean respuesta = response.getBoolean("respuesta");
                                if (respuesta){
                                    SharedPreferences pre = getSharedPreferences("estudiante", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pre.edit();
                                    editor.clear();
                                    //editor.apply();
                                    editor.commit();

                                    i = new Intent(getApplicationContext(),LoginEstActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(getApplicationContext(),"sapo",Toast.LENGTH_LONG).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

            queue.add(strRequest);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}