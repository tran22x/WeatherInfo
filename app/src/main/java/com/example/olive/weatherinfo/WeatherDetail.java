package com.example.olive.weatherinfo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.olive.weatherinfo.data.City;
import com.example.olive.weatherinfo.data.weatherdata.WeatherResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDetail extends AppCompatActivity {

    private TextView cityName;
    private ImageView weatherIcon;
    private Button closeBtn;
    private TextView details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);


        String cityQuery = "";
        cityName = findViewById(R.id.cityName);
        closeBtn = findViewById(R.id.closeBtn);
        details = findViewById(R.id.description);
        weatherIcon = findViewById(R.id.weatherIcon);
        final TextView tempHi = findViewById(R.id.tempHi);
        final TextView tempLo = findViewById(R.id.tempLo);
        final TextView temp = findViewById(R.id.temp);
        final TextView humidity = findViewById(R.id.humidity);



        if (getIntent().getSerializableExtra("city") != null) {
            final City city = (City) getIntent().getSerializableExtra("city");
            cityName.setText(city.getCityName());
            cityQuery = city.getCityName();

        }

        MainActivity.weatherAPI.getCurrentWeather(MainActivity.apiKey, "metric", cityQuery).enqueue(new Callback<WeatherResult>() {
            @Override
            public void onResponse(Call<WeatherResult> call, Response<WeatherResult> response) {
                String data =
                        ""+response.body().getWeather().get(0).getDescription();
                details.setText(data);
                temp.setText(String.format("%s (Celcius) ", response.body().getMain().getTemp()));
                tempHi.setText(String.format("High: %s", response.body().getMain().getTempMax()));
                tempLo.setText(String.format("Low: %s", response.body().getMain().getTempMin()));
                humidity.setText("Humidity: " + response.body().getMain().getHumidity());
                String imgURL = response.body().getWeather().get(0).getIcon();
                System.out.println("imgURL: " + imgURL);

                Glide.with(WeatherDetail.this).load("http://openweathermap.org/img/w/" + imgURL +".png").into(weatherIcon);
            }

            @Override
            public void onFailure(Call<WeatherResult> call, Throwable t) {
                Toast.makeText(WeatherDetail.this, "Error: "+
                        t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
