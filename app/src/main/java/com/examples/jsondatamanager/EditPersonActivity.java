package com.examples.jsondatamanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.examples.jsondatamanager.model.Person;

import java.util.UUID;

public class EditPersonActivity extends AppCompatActivity {

    private final static String PERSON = "person";
    private final static String MODE = "mode";
    private final static String MODE_ADD = "add";
    private final static String MODE_EDIT = "edit";
    private final static String MODE_INDEFINITE = "indefinite";

    public static void addPerson(Context context) {
        Intent intent = new Intent(context, EditPersonActivity.class);
        intent.putExtra(MODE, MODE_ADD);
        context.startActivity(intent);
    }

    public static void editPerson(Context context, Person person) {
        Intent intent = new Intent(context, EditPersonActivity.class);
        intent.putExtra(MODE, MODE_EDIT);
        intent.putExtra(PERSON, person);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_person);

        Bundle arguments = getIntent().getExtras();
        final String mode = arguments != null ? arguments.getString(MODE, null) : MODE_INDEFINITE;
        final Person currentPerson = getCurrentPerson(arguments);

        Button cancelButton = findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(v -> {
            MainActivity.cancelPerson(EditPersonActivity.this);
        });

        EditText editTextFirstName = findViewById(R.id.edit_text_first_name);
        EditText editTextLastName = findViewById(R.id.edit_text_last_name);

        Button updateButton = findViewById(R.id.button_update);
        updateButton.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            currentPerson.setFirstName(firstName);
            currentPerson.setLastName(lastName);

            MainActivity.updatePerson(EditPersonActivity.this, currentPerson);
        });

        Button deleteButton = findViewById(R.id.button_delete);
        deleteButton.setOnClickListener(v -> {
            MainActivity.deletePerson(EditPersonActivity.this, currentPerson);
        });

        Button addButton = findViewById(R.id.button_add);
        addButton.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();
            currentPerson.setFirstName(firstName);
            currentPerson.setLastName(lastName);

            MainActivity.addPerson(EditPersonActivity.this, currentPerson);
        });

        // Устанавливаем начальные значения
        editTextFirstName.setText(currentPerson.getFirstName());
        editTextLastName.setText(currentPerson.getLastName());

        // Скрываем неиспользуемые кнопки
        switch (mode) {
            case MODE_ADD:
                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                break;
            case MODE_EDIT:
                addButton.setVisibility(View.GONE);
                break;
            case MODE_INDEFINITE:
                addButton.setVisibility(View.GONE);
                deleteButton.setVisibility(View.GONE);
                updateButton.setVisibility(View.GONE);
                break;
        }
    }

    @NonNull
    private Person getCurrentPerson(Bundle arguments) {
        Person person = null;

        if(arguments != null) {
            person = (Person) arguments.getSerializable(PERSON);
        }

        return person != null ? person : new Person(UUID.randomUUID(), "", "");
    }
}