package com.example.weatherapp;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class CurrentWeather extends AppCompatActivity {
    private final String url = "https://api.openweathermap.org/data/2.5/weather";
    private final String appid = "172c2c5bc329d96e5b17ac25736c923a";
    TextView textview;
    TextView Title;
    TextView DescriptionTextView ;
    TextView TempTextView;
    ImageView WeatherPic;
    DecimalFormat df = new DecimalFormat("#.##");

    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myTag", "This is my message 2");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.currentweather);

        textview = findViewById(R.id.textView);
        Title = findViewById(R.id.Title);
        WeatherPic = findViewById(R.id.WeatherPic);
        DescriptionTextView = findViewById(R.id.Description);
        TempTextView = findViewById(R.id.temptextview);

        Bundle extras = getIntent().getExtras();
        String city = extras.getString("City");
        String country = extras.getString("Country");
        String tempUrl = "";

        if (!country.equals("")) {
            tempUrl = url + "?q=" + city + "," + country + "&appid=" + appid;
        } else {
            tempUrl = url + "?q=" + city + "&appid=" + appid;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, tempUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String output = "";
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray jsonArray = jsonResponse.getJSONArray("weather");
                    JSONObject jsonObjectWeather = jsonArray.getJSONObject(0);
                    String description = jsonObjectWeather.getString("description");
                    JSONObject jsonObjectMain = jsonResponse.getJSONObject("main");
                    double temp = jsonObjectMain.getDouble("temp") - 273.15;
                    double feelsLike = jsonObjectMain.getDouble("feels_like") - 273.15;
                    float pressure = jsonObjectMain.getInt("pressure");
                    int humidity = jsonObjectMain.getInt("humidity");
                    JSONObject jsonObjectWind = jsonResponse.getJSONObject("wind");
                    String wind = jsonObjectWind.getString("speed");
                    JSONObject jsonObjectClouds = jsonResponse.getJSONObject("clouds");
                    String clouds = jsonObjectClouds.getString("all");
                    JSONObject jsonObjectSys = jsonResponse.getJSONObject("sys");
                    String countryName = jsonObjectSys.getString("country");
                    String cityName = jsonResponse.getString("name");
                    textview.setTextColor(Color.rgb(0, 0, 0));
                    String TitleOut = "Current weather of " + cityName + " (" + countryName + ")";
                    Title.setText(TitleOut);
                    if (description.equals("overcast clouds")){
                        WeatherPic.setImageResource(R.drawable.overcastclouds);
                    }
                    else if (description.equals("clear sky")){
                        WeatherPic.setImageResource(R.drawable.clearsky);
                    }
                    DescriptionTextView.setText(description);
                    TempTextView.setText(df.format(temp) + " °C");
                    output += "Feels Like: " + df.format(feelsLike) + " °C"
                            + "\n Humidity: " + humidity + "%"
                            + "\n Wind Speed: " + wind + "m/s (meters per second)"
                            + "\n Cloudiness: " + clouds + "%"
                            + "\n Pressure: " + pressure + " hPa";
                    textview.setText(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString().trim(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);


    }

}
