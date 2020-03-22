package com.example.real.thinkers.thinkweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.real.thinkers.thinkweather.models.Weather;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    String [] cities= {"Dhaka", "Chittagong", "Barisal", "Klulna", "Rajshahi", "Rangpur", "Sylhet", "Mymensingh", "Comilla", "New Delhi",
            "Islamabad", "Kabul", "Yerevan", "Baku", "Manama", "Kuwait City", "Thimphu", "Bandar Seri Begawan", "Phnom Penh", "Beijing",
            "Nicosia", "Tbilisi", "Jakarta", "Tehran", "Baghdad", "Jerusalem", "Tokyo", "Amman", "Astana", "Bishkek", "Vientiane", "Beirut", "Kuala Lumpur",
            "Male", "Kathmandu", "Muscat", "Jerusalem (East)", "Doha", "Moscow", "Riyadh"};
    private String TAG = "MainActivity";
    Button refreshButton;
    String cityName="Dhaka";
    String url;
    String temperatureCount;
    RadioButton radioFahrenheit,radioCentigrde;
    AutoCompleteTextView autoCompleteTv;
    TextView city, date, temperature, conditions,unitsTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        refreshButton = findViewById(R.id.refreshButton);
        city =findViewById(R.id.city);
        date =  findViewById(R.id.date);
        temperature =  findViewById(R.id.temperature);
        conditions =  findViewById(R.id.conditions);
        autoCompleteTv = findViewById(R.id.autoCompleteTv);
        radioFahrenheit= findViewById(R.id.fahrenheit);
        radioCentigrde= findViewById(R.id.centigrade);
        unitsTv= findViewById(R.id.units);

        change();

        autoCompleteTv=findViewById(R.id.autoCompleteTv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,cities);
        autoCompleteTv.setAdapter(adapter);
        autoCompleteTv.setThreshold(1);


        radioFahrenheit.setChecked(true);
        radioCentigrde.setChecked(false);
        radioFahrenheit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                change();
            }
        });
        radioCentigrde.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                change();
            }
        });
        refreshButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            cityName = autoCompleteTv.getText().toString();
                            if (cityName.equals("")){
                                autoCompleteTv.setError("Please input a city");
                            }else {
                        url = "yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+cityName
                                +"%2C%20ak%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
                        change();
                        refreshButton.setText("Refresh");
                    }}
                }
        );

    }

    @Override
    protected void onResume() {
        super.onResume();
        change();
    }

    private void change() {
        WeatherApiInterface.Factory.getInstance().getWeather(url).enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Call<Weather> call, Response<Weather> response) {
                String cityName= response.body().getQuery().getResults().getChannel().getLocation().getCity();
                String buildDate= response.body().getQuery().getResults().getChannel().getLastBuildDate();
                String condition= response.body().getQuery().getResults().getChannel().getItem().getCondition().getText();


                if (radioFahrenheit.isChecked()){
                   temperatureCount = response.body().getQuery().getResults().getChannel().getItem().getCondition().getTemp();
                    temperature.setText(temperatureCount);
                    unitsTv.setText("°F");
                }else if (radioCentigrde.isChecked()){
                    double tempInDouble = Double.parseDouble(temperatureCount);
                    double tempInCentigrade = (tempInDouble-40.0)*(100.0/180.0);
                    DecimalFormat df = new DecimalFormat("#.#");
                    String tempInDecimal = df.format((tempInCentigrade));
                    temperature.setText(tempInDecimal);
                    unitsTv.setText("°C");
                }

                city.setText(cityName);
                date.setText(buildDate);
                conditions.setText(condition);


            }
            @Override
            public void onFailure(Call<Weather> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });
    }


    }

