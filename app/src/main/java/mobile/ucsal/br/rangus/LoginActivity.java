package mobile.ucsal.br.rangus;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private TextView textMostrar;

    private EditText editTextUser;
    private EditText editTextPassword;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewsById();
        firebaseAuth = FirebaseAuth.getInstance();
        iniciarEscutadores();


    }

    private void logar() {

         if(validarCredenciais()){

            String email = editTextUser.getText().toString().trim();
            String senha = editTextPassword.getText().toString().trim();

            firebaseAuth.signInWithEmailAndPassword(email,senha)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                             if(task.isSuccessful()){
                                 //Loguei activity
                                 Toast.makeText(LoginActivity.this, "LOGOU",Toast.LENGTH_SHORT).show();

                             }else{
                                 //Jogar um erro
                                 Toast.makeText(LoginActivity.this, "BURRO",Toast.LENGTH_SHORT).show();
                             }

                        }
                    });



         }else{

             Toast.makeText(this, " Usuario ou senha Invalidos", Toast.LENGTH_SHORT).show();

         }

    }

    private boolean validarCredenciais() {

        String email = editTextUser.getText().toString().trim();
        String senha = editTextPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextUser.setError("Email invalido");
            return false;
        }


        if(senha.isEmpty()){
            editTextPassword.setError("Senha invalido");
            return false;
        }

        return true;

    }

    private void iniciarEscutadores() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logar();

            }
        });

        textMostrar.setOnClickListener(new View.OnClickListener() {

            int cont = 0;

            @Override
            public void onClick(View v) {

                if(cont == 0){

                    textMostrar.setText("Esconder senha");
                    editTextPassword.setTransformationMethod(null);
                    cont++;
                }else{
                    textMostrar.setText("Mostrar senha");
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                    cont--;
                }
            }
        });

    }

    private void findViewsById() {

        editTextUser = findViewById(R.id.login_user);
        editTextPassword = findViewById(R.id.login_password);

        textMostrar = findViewById(R.id.login_text_senha);

        btnLogin = findViewById(R.id.login_btnLogin);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
