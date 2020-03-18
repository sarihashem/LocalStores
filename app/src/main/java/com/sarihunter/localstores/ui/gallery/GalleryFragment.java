package com.sarihunter.localstores.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.AddProductDialog;
import com.sarihunter.localstores.MainActivity;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.adapters.ProductAdapter;
import com.sarihunter.localstores.classes.Items;
import com.sarihunter.localstores.classes.Tags;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//my items fragment

public class GalleryFragment extends Fragment {

    RecyclerView rvParent;
    FloatingActionButton fabAdd;

    ProductAdapter productAdapter;

    private GalleryViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), MainActivity.class));
            Toast.makeText(getContext(), "please sign in", Toast.LENGTH_LONG).show();
        }
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        // show all my items here


        fabAdd = root.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v->{
            openAddDialog();
        });

        Tags tags = new Tags();

        //query items related to this User

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items").orderByChild("owner")
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());


        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
                setQuery(query,Items.class).build();

        productAdapter = new ProductAdapter(options);

        rvParent= root.findViewById(R.id.rvParent);
        rvParent.setAdapter(productAdapter);
        rvParent.setLayoutManager(new GridLayoutManager(getContext(),3));

        //Offline support
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        return root;
    }

    private void openAddDialog() {
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(getFragmentManager(),"Add Item Dialog");
    }

    @Override
    public void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        productAdapter.startListening();
    }
}