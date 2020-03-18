package com.sarihunter.localstores;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.adapters.ChatAdapter;
import com.sarihunter.localstores.classes.Chat;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChattingActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rvChat;
    ImageButton sendMsg;
    ImageView image;
    EditText tvMsg;
    DatabaseReference chats;
    ChatAdapter adapter;
    TextView talkingWith;
    String ownerID;


    String myUID;

    @Override
    protected void onStart() {
        super.onStart();
        checkStatus();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent i = getIntent();
        ownerID  = i.getExtras().getString("OwnerID");
        String owner = i.getStringExtra("owner");

        toolbar = findViewById(R.id.toolbar_chatting);
        setSupportActionBar(toolbar);
        // to hide app name from toolbar
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        image = findViewById(R.id.ivChat);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        rvChat = findViewById(R.id.rvChat);
        tvMsg = findViewById(R.id.etMsg);
        sendMsg = findViewById(R.id.ibSendMsg);
        talkingWith = findViewById(R.id.tvSenderNameInside);
        talkingWith.setText(owner);


// get the owner ID
        //create the query
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference myRef = database.getReference("Chats").child(user).child(ownerID);

        Query query = myRef.limitToFirst(50);

        FirebaseRecyclerOptions<Chat> options = new FirebaseRecyclerOptions.Builder<Chat>()
                .setQuery(query,Chat.class).build();

        adapter = new ChatAdapter(options);
        rvChat.setAdapter(adapter);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        //manager.setReverseLayout(true);
        manager.setStackFromEnd(true);
        rvChat.setLayoutManager(manager);
        rvChat.scrollToPosition(adapter.getItemCount()-1);


        sendMsg.setOnClickListener(v->{

            String msg = tvMsg.getText().toString().trim();

            if(!TextUtils.isEmpty(msg)){
                sendMsg(msg, ownerID);
            }else{
                Toast.makeText(this,"please write down something, check your connection",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendMsg(String msg,String ownerID) {

        String timeStamp = String.valueOf(System.currentTimeMillis());
        chats = FirebaseDatabase.getInstance().getReference().child("Chats");
        Chat c = new Chat(myUID,ownerID,msg,timeStamp);
        chats.child(myUID).child(ownerID).push().setValue(c).addOnSuccessListener(aVoid -> {
            Toast.makeText(this,"your msg sent successfuly",Toast.LENGTH_LONG).show();
        });
        chats.child(ownerID).child(myUID).push().setValue(c);
        tvMsg.setText("");

    }

    public void checkStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            //user isn't logged in go to main activity
            startActivity(new Intent(this,MainActivity.class));
            finish();

        }else {
            myUID = user.getUid();
        }
    }

}
