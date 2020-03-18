package com.sarihunter.localstores.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sarihunter.localstores.MainActivity;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.adapters.ChattingListAdapter;
import com.sarihunter.localstores.classes.User;

import java.util.ArrayList;
import java.util.List;

public class SendFragment extends Fragment {

    //chatting fragment

    public SendFragment(){
        //Required empty constructor
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    RecyclerView recyclerView;
    List<User> chatlistusers;
    List<String> keys;
    DatabaseReference dbRef;
    FirebaseUser user;
    FirebaseAuth mAuth;
    ChattingListAdapter adapter;


    private SendViewModel sendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), MainActivity.class));
            Toast.makeText(getContext(), "please sign in", Toast.LENGTH_LONG).show();
        }


        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        recyclerView = root.findViewById(R.id.rvChattingList);

        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        keys = new ArrayList<>();

        String keyId = user.getUid();

        dbRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("Chats").child(keyId);
        //Log.i("enter query" , dbRefChatlist.getParent().toString());

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                keys.clear();
                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    String key = ds.getKey();
                    keys.add((key));
                    Log.i("childss" , key);
                }

                loadChats();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        return root;
    }

    private void loadChats() {
        chatlistusers = new ArrayList<>();
        dbRef = FirebaseDatabase.getInstance().getReference().child("Users");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistusers.clear();


                for (DataSnapshot ds :
                        dataSnapshot.getChildren()) {
                    User user = ds.getValue(User.class);
                    for(String key : keys){
                        if(key.equals(user.getUserID())) {
                            chatlistusers.add(user);
                            Log.i("chatlistusers inside" , chatlistusers.toString());

                            break;
                        }

                    }
                    Log.i("chatlistusers outside" , chatlistusers.toString());


                }
                Log.i("chatlistusers outsideFor" , chatlistusers.toString());
                LinearLayoutManager manager = new LinearLayoutManager(getContext());
                recyclerView.setLayoutManager(manager);
                recyclerView.setHasFixedSize(true);
                adapter = new ChattingListAdapter(chatlistusers);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.search);
        if(item!=null)
            item.setVisible(false);
    }
}