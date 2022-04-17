package com.examples.jsondatamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextFirstName = findViewById(R.id.edit_text_first_name);
        EditText editTextLastName = findViewById(R.id.edit_text_last_name);

        Button readButton = findViewById(R.id.button_read);
        readButton.setOnClickListener(v->{

        });

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v->{

        });
    }
}