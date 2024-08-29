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

import com.example.project11.model.User;
import com.example.project11.repository.UserRespository;

public class EditDataAct extends AppCompatActivity {
    private EditText editName, editAddress;
    private ImageButton btnBack;
    private Button btnSubmit;
    private UserRespository userRepository;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        userRepository = new UserRespository(this);
        userId = getIntent().getLongExtra("USER_ID", -1);
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initalizeViews();
        initializeOnClickListeners();
        loadUserData();
    }

    private void initalizeViews() {
        editName = findViewById(R.id.editName);
        editAddress = findViewById(R.id.editAddress);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnBack = findViewById(R.id.btnBack);
    }

    private void initializeOnClickListeners() {
        btnSubmit.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String domisili = editAddress.getText().toString().trim();

            if (name.isEmpty() || domisili.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                updateDataToSQLite(userId, name, domisili);
                Toast.makeText(this, "User data updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnBack.setOnClickListener(v -> {
            finish();
        } );
    }


    private void loadUserData() {
        userRepository.open();
        User user = userRepository.getUserById(userId);
        userRepository.close();

        if (user != null) {
            editName.setText(user.getName());
            editAddress.setText(user.getDomisili());
        } else {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void updateDataToSQLite(long id, String name, String domisili) {
        userRepository.open();
        userRepository.updateUser(id, name, domisili);
        userRepository.close();
    }
}