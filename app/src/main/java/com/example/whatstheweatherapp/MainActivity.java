package com.example.whatstheweatherapp;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText cityName;
    TextView weather;

    public void getTheWeather(View view){
        new Weather().execute("http://openweathermap.org/data/2.5/weather?q=" + cityName.getText().toString() + "&appid=b6907d289e10d714a6e88b30761fae22");
        InputMethodManager mgr= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
    }

    public class Weather extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url=new URL(urls[0]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                InputStream in=connection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                String result="";
                int data=reader.read();
                while(data!=-1){
                    result+=(char)data;
                    data=reader.read();
                }
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object=new JSONObject(s);
                String data=object.getString("main");
                JSONObject jpart=new JSONObject(data);
                String main="",description="",weatherMsg="",temp="";
                temp=jpart.getString("temp");
                weatherMsg+=("Temperature: "+temp+"`C\n");
                data=object.getString("weather");
                JSONArray arr=new JSONArray(data);
                for(int i=0;i<arr.length();i++){
                    JSONObject part=arr.getJSONObject(i);
                    main=part.getString("main");
                    description=part.getString("description");
                    if(main!="" && description!="") weatherMsg+=(main+": "+description)+"\n";
                }
                if(weatherMsg!="")
                weather.setText(weatherMsg);
                else Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
            }
            catch (Exception e) {
                e.printStackTrace();
                weather.setText("");
                Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=findViewById(R.id.city);
        weather=findViewById(R.id.weatherinfo);
    }
}
