package com.sarihunter.localstores;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.adapters.ProductAdapter;
import com.sarihunter.localstores.classes.Items;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Category_items extends AppCompatActivity {

    String category;
    RecyclerView rv;
    DatabaseReference mydb;
    ProductAdapter productAdapter;
    TextView tvCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_items);

        Intent result = getIntent();
        category = result.getExtras().getString("category");

        tvCategory = findViewById(R.id.tvCategoryList);
        tvCategory.setText(category);

        mydb = FirebaseDatabase.getInstance().getReference().child("Items");

        Query query = mydb.orderByChild("category").equalTo(category).limitToLast(100);

        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
                setQuery(query,Items.class).build();

        productAdapter = new ProductAdapter(options);

        rv=findViewById(R.id.rvCategory);
        rv.setAdapter(productAdapter);
        rv.setLayoutManager(new GridLayoutManager(this,3));

    }

    @Override
    protected void onStart() {
        productAdapter.startListening();
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }
}
