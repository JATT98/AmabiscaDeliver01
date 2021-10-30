package com.example.amabiscadeliver.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.amabiscadeliver.Connect.ClickButtonListener;
import com.example.amabiscadeliver.Connect.GlobalVarLog;
import com.example.amabiscadeliver.Connect.Item;
import com.example.amabiscadeliver.Connect.ItemAdapter;
import com.example.amabiscadeliver.Connect.Order;
import com.example.amabiscadeliver.Connect.OrderAdapter;
import com.example.amabiscadeliver.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends AppCompatActivity {
    private Integer _codigo;
    private String _estado;
    private Double total = 0.0;
    private EditText detailtxt;
    private TextView ordertxt, datetxt, clienttxt, pricetxt;

    private RecyclerView recyclerView;
    private List itemList;

    private TextView asignarbtn, completarbtn, actualizarbtn;

    GlobalVarLog var =   GlobalVarLog.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        ordertxt = findViewById(R.id.OrderTxt1);
        datetxt = findViewById(R.id.DateTxt1);
        clienttxt = findViewById(R.id.ClientTxt1);
        pricetxt = findViewById(R.id.PriceTxt1);

        asignarbtn = findViewById(R.id.AsignarBtn);
        completarbtn = findViewById(R.id.CompleteBtn);
        actualizarbtn = findViewById(R.id.UpdateBtn1);

        detailtxt = findViewById(R.id.DetailTxt1);

        Bundle bundle = getIntent().getExtras();
        _codigo = bundle.getInt("title");
        String fecha = bundle.getString("date");
        String cliente = bundle.getString("customer");
        _estado = bundle.getString("state");

        Log.e("ORDEN: ",_codigo.toString());

        ordertxt.setText("Orden #" + _codigo);
        datetxt.setText(fecha);
        clienttxt.setText(cliente);
        detailtxt.setText(var.get_ubicacion());

        if (_estado.equals("Publicada")){
            asignarbtn.setVisibility(View.VISIBLE);
            completarbtn.setVisibility(View.GONE);
            detailtxt.setVisibility(View.GONE);
            actualizarbtn.setVisibility(View.GONE);
        }else{
            asignarbtn.setVisibility(View.GONE);
            completarbtn.setVisibility(View.VISIBLE);
            detailtxt.setVisibility(View.VISIBLE);
            actualizarbtn.setVisibility(View.VISIBLE);
        }

        recyclerView = findViewById(R.id.RecyclerDetail);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList = new ArrayList<>();
        fetchProducts();


        Operations();
    }

    private void fetchProducts() {
        String URL = var.get_URLconnection() + "cart/" + _codigo;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String nombre = jsonObject.getString("nombre");
                                Double precio = jsonObject.getDouble("precio");
                                Integer cantidad = jsonObject.getInt("cantidad");
                                total = total + (precio * cantidad);
                                Item item = new Item(nombre , cantidad , precio);
                                itemList.add(item);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ItemAdapter adapter = new ItemAdapter(OrderDetailActivity.this, itemList);

                            recyclerView.setAdapter(adapter);
                        }

                        pricetxt.setText("Q. " + total);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderDetailActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    private void Operations() {
        asignarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsignarOrden();
            }
        });

        completarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompletarOrden();
            }
        });

        actualizarbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDetalle();
            }
        });


    }

    private void EditarDetalle() {
        String URL = var.get_URLconnection() + "modifyorder";

        Log.d("RESULT: ",_codigo + detailtxt.getText().toString());

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("detalle",detailtxt.getText());
            jsonObject.put("orden",_codigo);

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
                            Toast.makeText(getApplicationContext(),"ORDEN ACTUALIZADA",Toast.LENGTH_SHORT).show();
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

    private void CompletarOrden() {
        String URL = var.get_URLconnection() + "completeorder/" + _codigo;


        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET, URL, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject jsonObject) {
                            Log.d("RESULT: ", jsonObject.toString());
                            Toast.makeText(getApplicationContext(),"ORDEN COMPLETADA CON ÉXITO",Toast.LENGTH_SHORT).show();
                            _estado = "Entregada";
                            completarbtn.setVisibility(View.GONE);
                            detailtxt.setVisibility(View.GONE);
                            actualizarbtn.setVisibility(View.GONE);
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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(OrderDetailActivity.this, OrderActivity.class));
        finish();
        super.onBackPressed();
    }



    private void AsignarOrden() {
        String URL = var.get_URLconnection() + "assignorder";

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("emisor",var.get_codigo());
            jsonObject.put("orden",_codigo);

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
                            Toast.makeText(getApplicationContext(),"ORDEN ASIGNADA CON ÉXITO",Toast.LENGTH_SHORT).show();
                            _estado = "En proceso";
                            asignarbtn.setVisibility(View.GONE);

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