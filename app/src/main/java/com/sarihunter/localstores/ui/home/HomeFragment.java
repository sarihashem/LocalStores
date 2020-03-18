package com.sarihunter.localstores.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sarihunter.localstores.AddProductDialog;
import com.sarihunter.localstores.MainActivity;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.adapters.MainAdapter;
import com.sarihunter.localstores.adapters.ProductAdapter;
import com.sarihunter.localstores.classes.Items;
import com.sarihunter.localstores.classes.Tags;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView rvParent;
    FloatingActionButton fabAdd;

    //ProductAdapter productAdapter;
    List<ProductAdapter> productAdapters = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), MainActivity.class));
            Toast.makeText(getContext(), "please sign in", Toast.LENGTH_LONG).show();
        }


        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fabAdd = root.findViewById(R.id.fabAdd);

        fabAdd.setOnClickListener(v->{
            openAddDialog();
        });

        Tags tags = new Tags();
        List<Tags> myTags = tags.fillTagsList();


//        Query query = FirebaseDatabase.getInstance()
//                .getReference()
//                .child("Items")
//                .limitToFirst(1000);
//
//        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
//                setQuery(query,Items.class).build();
//
//        productAdapter = new ProductAdapter(options);

        for (int i = 0; i< myTags.size(); i++){

            Query q = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("Items")
                    .orderByChild("category").equalTo(myTags.get(i).getTag());

            FirebaseRecyclerOptions<Items> o = new FirebaseRecyclerOptions.Builder<Items>().
                    setQuery(q,Items.class).build();

            productAdapters.add(new ProductAdapter(o));
        }


        MainAdapter adapter = new MainAdapter(myTags,productAdapters);

        rvParent= root.findViewById(R.id.rvParent);
        rvParent.setAdapter(adapter);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(root.getContext());
        rvParent.setLayoutManager(manager);



        return root;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        inflater.inflate(R.menu.main3, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onStart() {
        super.onStart();


        for (int i = 0; i < 7; i++) {
            productAdapters.get(i).startListening();
        }


    }

    @Override
    public void onStop() {
        for (int i = 0; i < 7; i++) {
            productAdapters.get(i).stopListening();
        }


        super.onStop();
    }

    private void openAddDialog() {
        AddProductDialog dialog = new AddProductDialog();
        dialog.show(getFragmentManager(),"Add Item Dialog");
    }


}