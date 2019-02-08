package com.example.cuacaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.cuacaapp.API.NetworkClient;
import com.example.cuacaapp.API.WeatherAPI;
import com.example.cuacaapp.GSON.WResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button button;
    private TextView responseText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hajar();
    }

    private void hajar() {
        editText = findViewById(R.id.city_name);
        button = findViewById(R.id.city_click);
        responseText = findViewById(R.id.response_text);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchWeatherDetails();
            }
        });
    }

    private void fetchWeatherDetails() {
        //Obtain an instance of Retrofit by calling the static method.
        Retrofit retrofit = NetworkClient.getRetrofitClient();
        /*
        The main purpose of Retrofit is to create HTTP calls from the Java interface based on the annotation associated with each method. This is achieved by just passing the interface class as parameter to the create method
        */
        WeatherAPI weatherAPIs = retrofit.create(WeatherAPI.class);

        Call call = weatherAPIs.getWeatherByCity(editText.getText().toString(), "YOUR_API_KEY");

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                /*This is the success callback. Though the response type is JSON, with Retrofit we get the response in the form of WResponse POJO class
                 */
                if (response.body() != null) {
                    WResponse wResponse = (WResponse) response.body();
                    responseText.setText("SUHU = " + wResponse.getMain().getTemp() + "Kelvin" +"\n" +
                            "Tekanan = " + wResponse.getMain().getPressure() + "\n" +
                            "Negara = " + wResponse.getSys().getCountry());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error !", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
