package com.yrlapps.randomasiannamegenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.jikyo.romaji.Transliterator;
import com.mariten.kanatools.KanaConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String japaneseJsonUrl = "https://raw.githubusercontent.com/YutongLi291/RandomAsianNameGenerator/master/db/japanese_names.json";
    TextView surnameView;
    TextView firstnameView;
    TextView surnameHiraganaView;
    TextView firstnameHiraganaView;
    TextView surnameRomajiView;
    TextView firstnameRomajiView;
    Spinner genderSpinner;
    Button getNameButton;
    int gender;  //0 is any, 1 is male, 2 is female.
    RequestQueue requestQueue;
    TextView genderView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surnameView = findViewById(R.id.generated_surname_view);
        firstnameView = findViewById(R.id.generated_firstname_view);
        firstnameHiraganaView = findViewById(R.id.generated_firstname_view_hiragana);
        surnameHiraganaView = findViewById(R.id.generated_surname_view_hiragana);
        firstnameRomajiView = findViewById(R.id.firstname_romaji_view);
        surnameRomajiView = findViewById(R.id.surname_romaji_view);
        gender = 0;
        genderView = findViewById(R.id.gender_view);
        genderSpinner = findViewById(R.id.gender_select);
        getNameButton = findViewById(R.id.get_name_button);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        getNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String genderSpinnerSelectedItem =(String)genderSpinner.getSelectedItem();
                if (genderSpinnerSelectedItem.equals("Female")){
                    gender = 2;
                }
                else if (genderSpinnerSelectedItem.equals("Male")){
                    gender = 1;
                }
                else{
                    gender = 0;
                }
                getRandomName();
//                getrandomFirstname();
            }
        });

    }

//    private void getrandomFirstname() {
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, japaneseJsonUrl, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//
//                    JSONArray firstname;
//                    JSONObject firstnames = response.getJSONObject("first_name");
//                    switch (gender) {
//                        case 1:    //Male case
//                            JSONArray malenames = firstnames.getJSONArray("male");
//                            firstname = malenames.getJSONArray((int) (Math.random() * malenames.length()));
//                            firstnameView.setText(firstname.getString(0));
//                            break;
//                        case 2:
//                            JSONArray femalenames = firstnames.getJSONArray("female");
//                            firstname = femalenames.getJSONArray((int) (Math.random() * femalenames.length()));
//                            firstnameView.setText(firstname.getString(0));
//                            break;
//                        default:
////                            int totalLength =firstnames.getJSONArray("male").length() +firstnames.getJSONArray("female").length();
//                            double genderRandom = Math.random();
//                            int randomIndex;
//                            if (genderRandom < 0.5){                //Male
//                                randomIndex=(int)(Math.random()*firstnames.getJSONArray("male").length());
//                                 malenames = firstnames.getJSONArray("male");
//                                 firstname= malenames.getJSONArray(randomIndex);
//                                 firstnameView.setText(firstname.getString(0));
//                            }
//                            else{
//                                randomIndex=(int)(Math.random()*firstnames.getJSONArray("female").length());
//                                femalenames = firstnames.getJSONArray("female");
//                                firstname = femalenames.getJSONArray(randomIndex);
//                                firstnameView.setText(firstname.getString(0));
//                            }
//
//
//                    }
//                } catch (JSONException e) {
//                    Log.e("yrl", "japanese surname parsing problem");
//                }
//            }
//
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        requestQueue.add(request);
//    }

    public void getRandomName() {
//        JSONObject surname;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, japaneseJsonUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    getFirstName(response);
                    getSurname(response);
                } catch (JSONException e) {
                    Log.e("yrl", "japanese surname parsing problem");
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);

    }

    private void getFirstName(JSONObject response) throws JSONException {
        JSONArray firstname;
        JSONObject firstnames = response.getJSONObject("first_name");
        switch (gender) {
            case 1:    //Male case
                JSONArray malenames = firstnames.getJSONArray("male");
                firstname = malenames.getJSONArray((int) (Math.random() * malenames.length()));
                setNameTexts(firstname);
                genderView.setText("Male");
                genderView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                break;
            case 2:
                JSONArray femalenames = firstnames.getJSONArray("female");
                firstname = femalenames.getJSONArray((int) (Math.random() * femalenames.length()));
                setNameTexts(firstname);
                genderView.setText("Female");
                genderView.setTextColor(Color.parseColor(String.valueOf("#ff69b4")));
                break;
            default:
//                            int totalLength =firstnames.getJSONArray("male").length() +firstnames.getJSONArray("female").length();
                double genderRandom = Math.random();
                int randomIndex;
                if (genderRandom < 0.5){                //Male
                    randomIndex=(int)(Math.random()*firstnames.getJSONArray("male").length());
                    malenames = firstnames.getJSONArray("male");
                    firstname= malenames.getJSONArray(randomIndex);
                    setNameTexts(firstname);
                    genderView.setText("Male");
                    genderView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                else{
                    randomIndex=(int)(Math.random()*firstnames.getJSONArray("female").length());
                    femalenames = firstnames.getJSONArray("female");
                    firstname = femalenames.getJSONArray(randomIndex);
                    setNameTexts(firstname);
                    genderView.setText("Female");
                    genderView.setTextColor(Color.parseColor("#ff69b4"));
                }


        }
    }

    private void getSurname(JSONObject response) throws JSONException {
        JSONArray surname;
        JSONArray surnames = response.getJSONArray("last_name");
        surname = surnames.getJSONArray((int) (Math.random() * surnames.length()));
        surnameView.setText(surname.getString(0));
        surnameHiraganaView.setText(surname.getString(1));
        surnameRomajiView.setText(Transliterator.transliterate("" + surnameHiraganaView.getText()).get(0));
    }

    private void setNameTexts(JSONArray firstname) throws JSONException {
        firstnameView.setText(firstname.getString(0));
        firstnameHiraganaView.setText(firstname.getString(1));
        String temp =""+firstnameHiraganaView.getText();
        firstnameRomajiView.setText(Transliterator.transliterate( temp).get(0));

//        List<String> stirngs  = Transliterator.transliterate("しば");
//        System.out.println(stirngs);
    }


}
