package mobile.ucsal.br.rangus.activitys;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.airbnb.lottie.model.Font;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import mobile.ucsal.br.rangus.R;
import mobile.ucsal.br.rangus.fragments.PerfilFragment;

public class HomeActivity extends AppCompatActivity {

    private FirebaseUser user;

    private Drawer drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        findViewsById();
        getUser();

        startDrawer();

        setSupportActionBar(toolbar);

    }

    private void getUser() {

        FirebaseAuth auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

    }

    private void findViewsById() {

        toolbar = findViewById(R.id.home_toolbar);

    }

    private void startDrawer() {

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.drawer_header)
                .addProfiles(
                        new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail())
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();

        //Items Drawer

        final PrimaryDrawerItem home = new PrimaryDrawerItem().withIdentifier(1).withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home);
        final PrimaryDrawerItem perfil = new PrimaryDrawerItem().withIdentifier(2).withName(R.string.drawer_item_profile).withIcon(FontAwesome.Icon.faw_user);
        final PrimaryDrawerItem logout = new PrimaryDrawerItem().withIdentifier(5).withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_sign_out_alt);



        drawer = new DrawerBuilder().withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(home, perfil, new DividerDrawerItem(), logout)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    android.support.v4.app.FragmentManager mg =  getSupportFragmentManager();
                    FragmentTransaction transaction = mg.beginTransaction();

                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        if(drawerItem == logout){
                            signOut();
                        }else if(drawerItem == home){

                        }else if(drawerItem == perfil){

                            PerfilFragment perfil = new PerfilFragment();

                            transaction.replace(R.id.home_content, perfil);
                            transaction.commit();


                        }


                        return false;

                    }
                })
                .withAccountHeader(headerResult)
                .build();

    }

    private void signOut() {

        new AlertDialog.Builder(this)
                .setTitle("Saindo..")
                .setMessage("Tem certeza que deseja sair?")
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.signOut();
                        finish();

                    }
                })
                .setNegativeButton("Não", null)
                .show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home:

                drawer.openDrawer();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
