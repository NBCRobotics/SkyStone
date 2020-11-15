package com.example.decarecyclingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText source;
    private Button button2;
    private TextView textView2;
    private TextView textView3;
    //private Button saveButton;

    //    public static final String SHARED_PREFS = "sharedPrefs";
//    public static final String TEXT = "text";
    //public static final String
    public static final String SHARED_PREF_NAME = "username";
    public static final String KEY_NAME = "key_username";

    private String text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        source = (EditText) findViewById(R.id.Source);
        button2 = (Button) findViewById(R.id.button2);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        //saveButton = (Button) findViewById(R.id.saveButton);
        textView2.setText("Welcome to the Recycling App! \n This app will help keep track of your recycled items and the more you recycle the more points you get! ");
        textView3.setText("First login, if you are a new user, enter a Username, Click Submit, and click New User. \nIf you are an Old User, make sure your name is correct, then just click Old User.");
        //Toast.makeText(this,"Name Submitted", Toast.LENGTH_LONG).show();
        //displayName();

        button2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveName();
                //checker

            }
        });







//        public void saveDate(){
//            SharedPreferences sharedPreferences =  getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.putString(TEXT, textView.getText().toString());
//
//            editor.apply();
//            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
//
//
//
//        }
//        public void loadData(){
//            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
//            text = sharedPreferences.getString(TEXT," "); // It will set to TEXT if no TEXT then empty
//
//
//        }
//        public void updateViews(){
//            textView.setText(text);
//
//
//        }



    }
    private void displayName(){
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME,MODE_PRIVATE);
        String name = sp.getString(KEY_NAME,null);

        if(name != null){
            textView.setText("Name : \n" +name);
            Toast.makeText(this,"Name Submitted", Toast.LENGTH_LONG).show();
        }


    }
    private void saveName(){
        String name = source.getText().toString().trim();
        if (name.isEmpty()){
            source.setError("OOPS! No Name");
            source.requestFocus();

        }
        SharedPreferences sp = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor e = sp.edit();

        e.putString(KEY_NAME, name);
        e.apply();
        source.setText("");
        //Toast.makeText(this,"Name Submitted", Toast.LENGTH_LONG).show();
        displayName();


    }
    public void disable(View v){
//        v.setEnabled(false);
//        //Log.d("success", "Button Disabled"); //Logcat
//        Button button = (Button) v;
//        button.setText("Disabled");

        View myView = findViewById(R.id.button3);
        myView.setEnabled(false);
        Button myBut = (Button) myView;
        myBut.setText("DISABLED 2.0");

    }
    public void handleText(View text){ //Getting a text from a text input
        TextView t = findViewById(R.id.Source);
        String input = t.getText().toString();
        String actualName = input.substring(6);
        //Toast.makeText(this,"Name Submitted", Toast.LENGTH_LONG).show();

        Log.d("info", input);

        //View myNewButton = findViewById(R.id.saveButton);
//        myNewButton.setEnabled(true);
//        Button myNewBut = (Button) myNewButton;
//        myNewBut.setText(actualName);
    }
    public void lauchToDirections(View v){
        Intent i = new Intent(this, WelcomeActivity.class);
        startActivity(i);


    }

}
