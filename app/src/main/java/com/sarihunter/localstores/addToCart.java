package com.sarihunter.localstores;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.adapters.ProductAdapter;

public class addToCart extends AppCompatActivity {

    ProductAdapter productAdapter;
    RecyclerView rvCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_cart);

//        FirebaseUser mUser= FirebaseAuth.getInstance().getCurrentUser();
//
//        Query cart = FirebaseDatabase.getInstance().getReference().child("Cart").child(mUser.getUid());
//        List<String> itemsCart = new ArrayList<>();
//
//        cart.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                 for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
//                    itemsCart.add(postSnapshot.getValue().toString());
//
//                    // here you can access to name property like university.name
//                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Items").orderByKey().equalTo(itemsCart.get(0));


//        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
//                setQuery(query,Items.class).build();
//
//        productAdapter = new ProductAdapter(options);
//
//        rvCart= findViewById(R.id.rvCart);
//        rvCart.setAdapter(productAdapter);
//        rvCart.setLayoutManager(new GridLayoutManager(this,3));
    }

    @Override
    protected void onStart() {
        super.onStart();
//        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
 //       productAdapter.stopListening();
    }
}
