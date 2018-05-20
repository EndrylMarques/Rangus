package mobile.ucsal.br.rangus.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import mobile.ucsal.br.rangus.R;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignIn;

    private ProgressDialog progressDialog;

    private TextView textMostrar;

    private EditText editTextUser;
    private EditText editTextPassword;

    private Button btnLogin;
    private SignInButton btnLogarGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewsById();

        googleAuth();
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        iniciarEscutadores();




    }

    private void googleAuth() {

        GoogleSignInOptions gpo = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignIn = GoogleSignIn.getClient(this, gpo);


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
                                 progressDialog.dismiss();
                                 //iniciar activity
                                 startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                 finish();

                             }else{
                                 progressDialog.dismiss();
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
            progressDialog.dismiss();
            return false;
        }


        if(senha.isEmpty()){
            editTextPassword.setError("Senha invalido");
            progressDialog.dismiss();
            return false;
        }

        return true;

    }

    private void iniciarEscutadores() {

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.setMessage("Logando...");
                progressDialog.show();
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

        btnLogarGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                logarGoolge();
                
            }
        });

    }

    private void logarGoolge() {

        Intent signInIntent = googleSignIn.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    private void findViewsById() {

        editTextUser = findViewById(R.id.login_user);
        editTextPassword = findViewById(R.id.login_password);

        textMostrar = findViewById(R.id.login_text_senha);

        btnLogin = findViewById(R.id.login_btnLogin);
        btnLogarGoogle = findViewById(R.id.login_btnGoogle);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            resultSignInGoogle(task);

        }

    }

    private void resultSignInGoogle(Task<GoogleSignInAccount> task) {

        try {

            GoogleSignInAccount accout = task.getResult(ApiException.class);

            startActivity(new Intent(this, HomeActivity.class));
            finish();

        }catch (ApiException e){

            Log.d("Google Error", e.getMessage());
            Toast.makeText(this,"Ocorreu um erro", Toast.LENGTH_SHORT).show();

        }

    }
}
