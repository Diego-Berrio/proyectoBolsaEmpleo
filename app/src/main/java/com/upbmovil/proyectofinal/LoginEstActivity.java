package com.upbmovil.proyectofinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.upbmovil.proyectofinal.api.Api;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginEstActivity extends AppCompatActivity {
    Button btnAcceder;
    ProgressBar pbEsperar;
    EditText etID, etContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_est);
        inicializarCampos();
    }

    private void inicializarCampos() {
        btnAcceder = findViewById(R.id.btnVacantes);
        etContrasena = findViewById(R.id.tvTelefono);
        etID = findViewById(R.id.etID);
        pbEsperar = findViewById(R.id.pbEsperar);
        pbEsperar.setVisibility(View.GONE);
    }

    public void loginEstudiante(View view){
        btnAcceder.setVisibility(View.GONE);
        pbEsperar.setVisibility(View.VISIBLE);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Api.URL_BASE+"/estudiante/login";

        try {

            JSONObject datosJson = new JSONObject();
            datosJson.put("id_UPB", etID.getText().toString());
            datosJson.put("contrasena", etContrasena.getText().toString());

            JsonObjectRequest strRequest = new JsonObjectRequest(Request.Method.POST, url, datosJson,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //
                            try {
                                String meta = response.getString("meta");

                                JSONObject data = (JSONObject) response.get("data");

                                String token = data.getString("token");
                                JSONObject estudiante = (JSONObject) data.get("estudiante");

                                long id_UPB = estudiante.getLong("id_UPB");
                                String nombre = estudiante.getString("nombre");
                                String apellido = estudiante.getString("apellido");
                                String correo = estudiante.getString("correo");
                                String telefono = estudiante.getString("telefono");



                                Toast.makeText(getApplicationContext(), nombre, Toast.LENGTH_SHORT).show();
                                SharedPreferences pre = getSharedPreferences("estudiante", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pre.edit();
                                editor.putString("datos",response.toString());
                                editor.putLong("id_UPB",id_UPB);
                                editor.putString("nombre",nombre);
                                editor.putString("apellido",apellido);
                                editor.putString("correo",correo);
                                editor.putString("telefono",telefono);
                                editor.putString("token",token);
                                editor.putBoolean("acceso",true);

                                //editor.apply();
                                editor.commit();
                                Intent i = new Intent(getApplicationContext(),HomeEstActivity.class);
                                startActivity(i);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                if(e.getMessage().contains("No value for data")){
                                    try {
                                        Toast.makeText(getApplicationContext(), response.getString("errors"), Toast.LENGTH_SHORT).show();
                                    } catch (JSONException jsonException) {
                                        jsonException.printStackTrace();
                                    }

                                }

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