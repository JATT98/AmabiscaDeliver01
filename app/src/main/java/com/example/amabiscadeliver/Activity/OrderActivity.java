package com.example.amabiscadeliver.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.amabiscadeliver.Connect.ClickButtonListener;
import com.example.amabiscadeliver.Connect.GlobalVarLog;
import com.example.amabiscadeliver.Connect.Order;
import com.example.amabiscadeliver.Connect.OrderAdapter;
import com.example.amabiscadeliver.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderActivity extends AppCompatActivity {
    private static final int REQUEST_CALL = 1;
    GlobalVarLog var =   GlobalVarLog.getInstance();
    private RecyclerView recyclerView;
    private List orderList;
    private TextView phone2;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        phone2 = findViewById(R.id.numberCall);
        phone2.setText(var.get_phone());

        recyclerView = findViewById(R.id.orderView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Localizacion();

        fetchProducts();

        TextView phone = findViewById(R.id.btnCall2);

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallButton();
            }
        });
    }

    private void Localizacion() {
        if (ActivityCompat.checkSelfPermission(OrderActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                         Location location = task.getResult();
                         if (location != null){
                             Geocoder geocoder = new Geocoder(OrderActivity.this,
                                     Locale.getDefault());
                             try {
                                 List<Address> addresses = geocoder.getFromLocation(
                                   location.getLatitude(), location.getLongitude(), 1
                                 );

                                 var.set_ubicacion("Ubicación: " + addresses.get(0).getAddressLine(0));

                             } catch (IOException e) {
                                 e.printStackTrace();
                             }
                         }
                }
            });
        }else{
            ActivityCompat.requestPermissions(OrderActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    private void fetchProducts() {
        String URL = var.get_URLconnection() + "activeorders/" + var.get_codigo();
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0 ; i < response.length() ; i ++){
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                Integer codigo = jsonObject.getInt("orden");
                                String fecha = jsonObject.getString("fecha");
                                String estado = jsonObject.getString("estado");
                                String cliente = jsonObject.getString("cliente");
                                String telefono = jsonObject.getString("telefono");

                                Order order = new Order(codigo , fecha , cliente , telefono, estado);
                                orderList.add(order);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            OrderAdapter adapter = new OrderAdapter(OrderActivity.this, orderList, new ClickButtonListener() {
                                @Override
                                public void Click() {
                                    phone2.setText(var.get_phone());
                                }
                            });

                            recyclerView.setAdapter(adapter);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(OrderActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onBackPressed() {
        var.set_phone("");
        var.set_ubicacion("");
        super.onBackPressed();
    }

    private void CallButton(){
        if (ContextCompat.checkSelfPermission(OrderActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(OrderActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }else {
            String dial = "tel:" + var.get_phone();
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CALL){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                CallButton();
            }else{
                Toast.makeText(this, "PERMISO DENEGADO", Toast.LENGTH_SHORT).show();
            }
        }
    }
}