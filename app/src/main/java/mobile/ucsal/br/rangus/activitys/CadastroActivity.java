package mobile.ucsal.br.rangus.activitys;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mobile.ucsal.br.rangus.R;
import mobile.ucsal.br.rangus.model.User;

public class CadastroActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText editTextEmail;
    private EditText editTextSenha;
    private EditText editTextNome;
    private EditText editTextTelefone;

    private TextView mostrarSenha;

    private Button btnCadastra;

    private int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        findViewsById();

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();

        progressDialog = new ProgressDialog(this);

        btnCadastra.setOnClickListener(this);
        mostrarSenha.setOnClickListener(this);
    }

    private void findViewsById() {

        editTextEmail = findViewById(R.id.cadastro_email);
        editTextSenha = findViewById(R.id.cadastro_password);
        editTextNome = findViewById(R.id.cadastro_nome);
        editTextTelefone = findViewById(R.id.cadastro_telefone);

        btnCadastra = findViewById(R.id.cadastro_btnCadastra);

        mostrarSenha = findViewById(R.id.cadastro_text_senha);

    }

    @Override
    public void onClick(View v) {



        if (v == btnCadastra) {

            cadastrar();

        }

        if (v == mostrarSenha) {

            if (cont == 0) {

                mostrarSenha.setText("Esconder senha");
                editTextSenha.setTransformationMethod(null);
                cont++;
            } else {
                mostrarSenha.setText("Mostrar senha");
                editTextSenha.setTransformationMethod(new PasswordTransformationMethod());
                cont--;

            }

        }
    }

    private void cadastrar() {

        progressDialog.setMessage("Cadastrando...");
        progressDialog.show();

        String email = editTextEmail.getText().toString().trim();
        String senha = editTextSenha.getText().toString().trim();
        String nome = editTextNome.getText().toString();
        String telefone = editTextTelefone.getText().toString().trim();

        if(validar(email,senha,nome,telefone)){

            //Cadastra
            signUp(email,senha,telefone,nome);


        }

    }

    private void signUp(final String email, String senha, final String telefone, final String nome) {

        progressDialog.setMessage("Finalizando...");

        firebaseAuth.createUserWithEmailAndPassword(email,senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){

                            FirebaseUser firebaseUserAtual = firebaseAuth.getCurrentUser();
                            writeData(nome,telefone,email,firebaseUserAtual);
                            startActivity(new Intent(CadastroActivity.this,HomeActivity.class));
                            finish();

                        }else{
                            progressDialog.dismiss();

                            Toast.makeText(CadastroActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void writeData(String nome, String telefone, String email, FirebaseUser firebaseUserAtual) {

        User user = new User(telefone,email,nome);

        String userFirebaseId = firebaseUserAtual.getUid();

        databaseReference.child("users").child(userFirebaseId).setValue(user);


    }


    private boolean validar(String email, String senha, String confirmSenha, String telefone) {

        if(email.isEmpty()){

            editTextEmail.setError("Email inválido");
            progressDialog.dismiss();
            return false;
        }else if(senha.isEmpty()){

            editTextSenha.setError("Senha inválida");
            progressDialog.dismiss();
            return false;
        }else if(telefone.isEmpty()){

            editTextTelefone.setError("Telefone inválido");
            progressDialog.dismiss();
            return false;
        }else if(confirmSenha.isEmpty()){

            editTextSenha.setError("Nome Obrigatório");
            progressDialog.dismiss();
            return false;
        }



        return true;

    }

}
