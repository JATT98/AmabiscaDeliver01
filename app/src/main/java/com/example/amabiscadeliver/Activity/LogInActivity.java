package com.example.amabiscadeliver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.amabiscadeliver.Connect.GlobalVarLog;
import com.example.amabiscadeliver.R;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInActivity extends AppCompatActivity {
    GlobalVarLog var =   GlobalVarLog.getInstance();

    private ConstraintLayout logBtn;
    EditText txtUser, txtPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        txtUser = findViewById(R.id.txtUser);
        txtPass = findViewById(R.id.txtPass);

        logBtn = findViewById(R.id.btnLog);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogIn(txtUser.getText().toString(),txtPass.getText().toString());
            }
        });
    }

    public void LogIn(String User, String Pass) {

        String URL = var.get_URLconnection() + "getUser";

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("name",User);
            jsonObject.put("pass",Pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST, URL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("RESULT: ", jsonObject.toString());
                            try {
                                Integer     _codigo     = jsonObject.getInt("_codigo");
                                String      _usuario    = jsonObject.getString("_user");
                                String      _clave      = jsonObject.getString("_pass");
                                String      _nombre     = jsonObject.getString("_nombre");
                                String      _apellido   = jsonObject.getString("_apellido");
                                String      _dni        = jsonObject.getString("_dni");
                                String      _direccion  = jsonObject.getString("_direccion");
                                String      _telefono   = jsonObject.getString("_telefono");
                                String      _correo     = jsonObject.getString("_correo");
                                String      _nacimiento = jsonObject.getString("_fnacimiento");
                                String      _sexo       = jsonObject.getString("_sexo");
                                String      _rol        = jsonObject.getString("_rol");
                                String      _msj        = jsonObject.getString("_msj");


                                if(_msj.equals("OK") ){
                                    if (_rol.equals("Repartidor")){
                                        var.set_codigo(_codigo);
                                        var.set_nombre(_nombre);
                                        var.set_apellido(_apellido);
                                        var.set_dni(_dni);
                                        var.set_direccion(_direccion);
                                        var.set_correo(_correo);
                                        var.set_telefono(_telefono);
                                        var.set_fnacimiento(_nacimiento);
                                        var.set_sexo(_sexo);
                                        var.set_usuario(_usuario);
                                        var.set_clave(_clave);
                                        Log.e("RESULT: ",_msj);

                                        Toast.makeText(getApplicationContext(),"BIENVENIDO "+ _nombre + " " + _apellido,Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(LogInActivity.this, MainActivity.class));
                                        finish();
                                    }else{
                                        Log.e("ERROR: ","TIPO DE USUARIO DENEGADO");
                                        Toast.makeText(getApplicationContext(),"PERMISO DENEGADO",Toast.LENGTH_SHORT).show();
                                    }


                                }else{
                                    Log.e("ERROR: ",_msj);
                                    Toast.makeText(getApplicationContext(),_msj,Toast.LENGTH_SHORT).show();
                                }

                            }catch (JSONException e){Log.e("ERROR: ",e.toString());}


                        }
                    }, new Response.ErrorListener (){

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Log.d("ERROR: ",volleyError.toString());
                }
            }
            );
            requestQueue.add(jsonObjectRequest);



        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
}