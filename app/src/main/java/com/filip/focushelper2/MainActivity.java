package com.filip.focushelper2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toastMe(View view) {
        // val myToast = Toast.makeText(this, message, duration);
        Toast myToast = Toast.makeText(this, "Hello Toast!", Toast.LENGTH_SHORT);
        myToast.show();
    }

    public void countMe (View view) {
        // Get the text view
        TextView showCountTextView = findViewById(R.id.textView);

        // Get the value of the text view.
        String countString = showCountTextView.getText().toString();

        // Convert value to a number and increment it
        int count = Integer.parseInt(countString);
        count++;

        // Display the new value in the text view.
        showCountTextView.setText(Integer.toString(count));
    }

    public void onClickButton(View view) {
        startActivity(new Intent(MainActivity.this,AppsListActivity.class));
    }

}
