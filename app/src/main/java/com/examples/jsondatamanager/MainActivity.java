package com.examples.jsondatamanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.examples.jsondatamanager.model.Person;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "data.json";

    private final static String PERSON = "person";
    private final static String MODE = "mode";
    private final static String MODE_ADD = "add";
    private final static String MODE_UPDATE = "update";
    private final static String MODE_DELETE = "delete";
    private final static String MODE_CANCEL = "cansel";
    private final static String MODE_INDEFINITE = "indefinite";


    public static void addPerson(Context context, Person person) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MODE, MODE_ADD);
        intent.putExtra(PERSON, person);
        context.startActivity(intent);
    }

    public static void updatePerson(Context context, Person person) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MODE, MODE_UPDATE);
        intent.putExtra(PERSON, person);
        context.startActivity(intent);
    }

    public static void deletePerson(Context context, Person person) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MODE, MODE_DELETE);
        intent.putExtra(PERSON, person);
        context.startActivity(intent);
    }

    public static void cancelPerson(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MODE, MODE_CANCEL);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle arguments = getIntent().getExtras();
        final String mode = arguments != null ? arguments.getString(MODE, null) : MODE_INDEFINITE;
        final Person currentPerson = arguments != null ? (Person) arguments.getSerializable(PERSON) : null;
        final List<Person> persons = new ArrayList<Person>(readPerson(FILE_NAME));

        if (mode.equals(MODE_ADD)) {
            persons.add(currentPerson);
            writePerson(persons, FILE_NAME);
        } else if (mode.equals(MODE_UPDATE)) {
            Person updatePerson = getPersonFromId(persons, currentPerson.getId());
            if (updatePerson != null) {
                updatePerson.setFirstName(currentPerson.getFirstName());
                updatePerson.setLastName(currentPerson.getLastName());
                writePerson(persons, FILE_NAME);
            }
        } else if (mode.equals(MODE_DELETE)) {
            Person deletePerson = getPersonFromId(persons, currentPerson.getId());
            if (deletePerson != null) {
                persons.remove(deletePerson);
                writePerson(persons, FILE_NAME);
            }
        }

        PersonListAdapter adapter = new PersonListAdapter(new PersonListAdapter.WordDiff());
        adapter.setOnItemClickListener(person -> {
            EditPersonActivity.editPerson(MainActivity.this, person);
        });
        adapter.submitList(persons);

        RecyclerView personRecyclerView = findViewById(R.id.recycle_view_person);
        personRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        personRecyclerView.setAdapter(adapter);

        FloatingActionButton addButton = findViewById(R.id.floating_button_add);
        addButton.setOnClickListener(v -> {
            EditPersonActivity.addPerson(MainActivity.this);
        });
    }

    /**
     * Создает экземпляр объекта Person, из данных в формате json прочитанных в файле расположенному по пути filePath.
     */
    private List<Person> readPerson(String filePath) {
        Person[] person = new Person[0];

        try {
            String jsonText = readText(filePath);
            Gson gson = new Gson();
            person = gson.fromJson(jsonText, Person[].class);
        } catch (Exception error) {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
        }

        return Arrays.asList(person.clone());
    }

    /**
     * Запмсывает данные экземпляра объекта Person, в формате json в файл расположенный по адресу filePath
     */
    private void writePerson(Collection<Person> persons, String filePath) {
        try {
            Gson gson = new Gson();
            String jsonText = gson.toJson(persons);
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

    private Person getPersonFromId(List<Person> persons, UUID personId) {
        Person oldPerson = null;
        for (Person person : persons) {
            if (person.getId().equals(personId)) {
                oldPerson = person;
            }
        }
        return oldPerson;
    }
}