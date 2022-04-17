package com.examples.jsondatamanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.examples.jsondatamanager.model.Person;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "data.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextFirstName = findViewById(R.id.edit_text_first_name);
        EditText editTextLastName = findViewById(R.id.edit_text_last_name);

        Button readButton = findViewById(R.id.button_read);
        readButton.setOnClickListener(v -> {

            Person person = readPerson(FILE_NAME);
            if (person != null) {
                editTextFirstName.setText(person.getFirstName());
                editTextLastName.setText(person.getLastName());
            }

        });

        Button saveButton = findViewById(R.id.button_save);
        saveButton.setOnClickListener(v -> {
            String firstName = editTextFirstName.getText().toString();
            String lastName = editTextLastName.getText().toString();

            Person person = new Person(firstName, lastName);
            writePerson(person, FILE_NAME);
        });

        Person person = readPerson(FILE_NAME);
        if (person != null) {
            editTextFirstName.setText(person.getFirstName());
            editTextLastName.setText(person.getLastName());
        }
    }

    /**
     * Создает экземпляр объекта Person, из данных в формате json прочитанных в файле расположенному по пути filePath.
     */
    private Person readPerson(String filePath) {
        Person person = null;

        try {
            String jsonText = readText(filePath);
            Gson gson = new Gson();
            person = gson.fromJson(jsonText, Person.class);
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }

        return person;
    }

    /**
     * Запмсывает данные экземпляра объекта Person, в формате json в файл расположенный по адресу filePath
     */
    private void writePerson(Person person, String filePath) {
        try {
            Gson gson = new Gson();
            String jsonText = gson.toJson(person);
            writeText(filePath, jsonText);
            Toast.makeText(this, R.string.toast_save_file_ok, Toast.LENGTH_LONG).show();
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Чтение файла расположенного по адресу filePath,
     * возвращает прочитанный текст.
     */
    private String readText(String filePath) throws IOException {
        String text = null;

        FileInputStream stream = null;

        try {
            stream = openFileInput(filePath);
            byte[] data = new byte[stream.available()];
            stream.read(data);
            text = new String(data);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }

        return text;
    }

    /**
     * Записать текста в файл расположенного по адресу filePath
     */
    private void writeText(String filePath, String text) throws IOException {
        FileOutputStream stream = null;

        try {
            stream = openFileOutput(filePath, MODE_PRIVATE);
            byte[] data = text.getBytes(StandardCharsets.UTF_8);
            stream.write(data);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
}