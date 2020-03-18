package com.sarihunter.localstores;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.adapters.ProductAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Main2Activity extends AppCompatActivity {

    RecyclerView rvParent;
    FloatingActionButton fabAdd;

    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabAdd = findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v->{
            openAddDialog();
        });

//        Tags tags = new Tags();
//
//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Items")
//                .limitToLast(50);
//
//        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
//                setQuery(query,Items.class).build();
//
//        productAdapter = new ProductAdapter(options);
//
//
//
//        MainAdapter adapter = new MainAdapter(tags.fillTagsList(),productAdapter);
//
//        rvParent= findViewById(R.id.rvParent);
//        rvParent.setAdapter(adapter);
//        rvParent.setLayoutManager(new LinearLayoutManager(this));





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
        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }

    private void openAddDialog() {
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(getSupportFragmentManager(),"Add Item Dialog");
    }

}
