package com.sarihunter.localstores;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sarihunter.localstores.adapters.ProductAdapter;
import com.sarihunter.localstores.classes.Items;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Main3Activity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    ImageView userImgView;
    TextView displayName, userEmail;
    RecyclerView rvParent;
    ProductAdapter productAdapter;
    FirebaseUser cUser;
    FirebaseRecyclerOptions<Items> o;

//TODO implement basic search for items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkStatus();


        //Intent intent = getIntent();

        //String idInDB = intent.getExtras().getString("userID");

        //FirebaseDatabase myUsersDB = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = myUsersDB.getReference();

        cUser = FirebaseAuth.getInstance().getCurrentUser();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        // get the views and implement the data into header view

        View headerView = navigationView.getHeaderView(0);
        userEmail = headerView.findViewById(R.id.tvUserEmail);
        displayName = headerView.findViewById(R.id.tvDisplayName);
        userImgView = headerView.findViewById(R.id.ivUserImage);


        userEmail.setText(cUser.getEmail());

        if (TextUtils.isEmpty(cUser.getDisplayName())) {
            displayName.setText("please add display name at your profile page");
        } else {
            displayName.setText(cUser.getDisplayName());
        }


        if(cUser.getPhotoUrl()  == null){


                    userImgView.setImageResource(R.drawable.ic_person_black_24dp);
                }else{

                    try {
                        //Picasso.with(getApplicationContext()).load(cUser.getPhotoUrl()).into(userImgView);
                        Glide.with(this)
                                .load(cUser.getPhotoUrl())
                                .apply(RequestOptions.circleCropTransform())
                                .into(userImgView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main3, menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.search){
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {

                    if(!TextUtils.isEmpty(query.trim())){
                        //load query
                        loadQuery(query);
                        //productAdapter.stopListening();

                    }else{
                        loadAll();
                    }

                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }

            });
        }


        return super.onOptionsItemSelected(item);
        //return true;
    }

    private void loadAll() {

        Query q = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items")
                .limitToLast(100);

        o = new FirebaseRecyclerOptions.Builder<Items>().
                setQuery(q,Items.class).build();
        productAdapter = new ProductAdapter(o);
        productAdapter.updateOptions(o);

        rvParent= findViewById(R.id.rvParent);
        rvParent.setAdapter(productAdapter);
        rvParent.setLayoutManager(new LinearLayoutManager(this));
        productAdapter.startListening();

    }

    private void loadQuery(String s) {

        Query q = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items")
                .orderByChild("name").startAt(s);

        o = new FirebaseRecyclerOptions.Builder<Items>().
                setQuery(q,Items.class).build();


        productAdapter = new ProductAdapter(o);
        productAdapter.updateOptions(o);
        rvParent= findViewById(R.id.rvParent);
        rvParent.setAdapter(productAdapter);
        rvParent.setLayoutManager(new LinearLayoutManager(this));
        productAdapter.startListening();

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void checkStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            //user isn't logged in go to main activity
            startActivity(new Intent(this,MainActivity.class));
            finish();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        checkStatus();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
}
