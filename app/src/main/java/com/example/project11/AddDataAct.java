package com.example.project11;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.project11.repository.UserRespository;

public class AddDataAct extends AppCompatActivity {

    private EditText editName, editAddress;
    private Button btnSubmit, btnCancel;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initalizeViews();
        initializeOnClickListeners();
    }

    void initializeOnClickListeners() {
        btnSubmit.setOnClickListener(v -> {
            String name = editName.getText().toString();
            String domisili = editAddress.getText().toString();

            if (name.isEmpty() || domisili.isEmpty()) {
                Toast.makeText(AddDataAct.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                insertDataToSQLite(name, domisili);
                Toast.makeText(AddDataAct.this, "User data submitted", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        });

        btnCancel.setOnClickListener(v -> {
            finish();
        });
    }

    void initalizeViews() {
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnCancel = findViewById(R.id.btnCancel);
        btnBack = findViewById(R.id.btnBack);
    }

    void insertDataToSQLite(String name, String domisili) {
        UserRespository userRepository = new UserRespository(this);
        userRepository.open();
        userRepository.createUser(name, domisili);
        userRepository.close();
    }
}