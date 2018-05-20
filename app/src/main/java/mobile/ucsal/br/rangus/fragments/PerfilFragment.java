package mobile.ucsal.br.rangus.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import de.hdodenhof.circleimageview.CircleImageView;
import mobile.ucsal.br.rangus.R;
import mobile.ucsal.br.rangus.model.User;

public class PerfilFragment extends Fragment{

    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;

    private User userModel;

    private TextView textViewUsername;
    private TextView textViewEmaill;
    private TextView textViewTelefone;

    private Button entrarContato;

    private CircleImageView userImage;

    private ListView menusProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_perfil, container, false);

        //Faz tudo aqui dentro
        findViewsById(rootView);
        startFirebase();
        updateProfile();

        listners();

        return rootView;
    }

    private void listners() {

        entrarContato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse("smsto:" + userModel.getTelefone());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);

                i.setPackage("com.whatsapp");
                startActivity(i);

            }
        });

    }

    private void startFirebase() {

        user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference().child("users").child(user.getUid());

        if(userModel == null){

            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    userModel = new User();
                    userModel.setTelefone(dataSnapshot.child("telefone").getValue().toString());
                    userModel.setNome(dataSnapshot.child("nome").getValue().toString());
                    updateProfile();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    private void updateProfile() {

        textViewEmaill.setText(user.getEmail());

        if(userModel != null){
            textViewTelefone.setText(userModel.getTelefone());
            textViewUsername.setText(userModel.getNome());
        }



    }

    private void findViewsById(ViewGroup rootView) {

        textViewEmaill = rootView.findViewById(R.id.profile_user_email);
        textViewUsername = rootView.findViewById(R.id.profile_user_nome);
        textViewTelefone = rootView.findViewById(R.id.profile_user_telefone);

        userImage = rootView.findViewById(R.id.profile_user_image);

        entrarContato = rootView.findViewById(R.id.perfil_btnContato);

        menusProfile = rootView.findViewById(R.id.perfil_menus_list);

    }
}
