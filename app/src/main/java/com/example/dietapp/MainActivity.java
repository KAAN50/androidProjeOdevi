package com.example.dietapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.dietapp.dtos.LoginDto;
import com.example.dietapp.interfaces.ILogin;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private Button buttonLogin, buttonLoginRegister, buttonImageChange;
    private TextView textViewEmail,textViewPassword;
    private ImageView profileImageView;
    static final int SELECT_IMAGE=12; //görsel değişimi için yaptım
    Uri imageUri;

    ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        TextView textViewEmail = (TextView) findViewById(R.id.textViewEmail);
        TextView textViewPassword = (TextView) findViewById(R.id.textViewPassword);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        Button buttonImageChange = (Button) findViewById(R.id.buttonImageChange);



        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                ILogin iLogin = RetrofitClient.getRetrofitInstance().create(ILogin.class);
                LoginDto loginDto = new LoginDto(email,password);
                Call<Void> call = iLogin.loginUser(loginDto);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            // Başarılı bir şekilde cevap alındı, başarı mesajını göster
                            Toast.makeText(MainActivity.this, "Kayıt Başarılı", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, BmiCalculator.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Başarısız cevap alındı, hata durumunu göster
                            String errorMessage = "Error: " + response.code() + " " + response.message();
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();

                            // Server tarafından dönen detaylı hata mesajını almak için:
                            try {
                                String errorBody = response.errorBody().string();
                                // errorBody içinde server tarafından dönen JSON formatında hata mesajını kullanabilirsiniz.
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
        Button buttonLoginRegister = (Button) findViewById(R.id.buttonLoginRegister);
        buttonLoginRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kayıt ol ekranına geçiş yap
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
            }
        });

        //galleryLauncher adında bir ActivityResultLauncher nesnesi oluşturulur ve registerForActivityResult metodu kullanılır. Bu, galeriden içerik alma işlemi sona erdiğinde çağrılacak olan bir callback fonksiyonu içerir.
        //ActivityResultContracts.GetContent(): Bu, galeriden içerik alma işlemi için bir kontrakt (contract) tanımlar.
        galleryLauncher=registerForActivityResult(new ActivityResultContracts.GetContent()
                , new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        profileImageView.setImageURI(uri);
                    }
                });

        //. Bu buton tıklandığında galeriye gitmek için bir Intent oluşturulur ve galleryLauncher.launch("image/*") ile galeri başlatılır.
        buttonImageChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent.ACTION_GET_CONTENT: Bu, içerik almak için bir işlem talebi oluşturur. Genellikle galeri veya dosya tarayıcı gibi uygulamalara içerik alma talebi gönderir
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //galleryLauncher.launch("image/*"): Bu, galleryLauncher'ı başlatarak içerik alma işlemini başlatır. "image/*" parametresi, alınacak içeriğin türünü belirtir ve bu örnekte resim türünde içerik alınması sağlanır.
                galleryLauncher.launch("image/*");

            }
        });
    }

    }

