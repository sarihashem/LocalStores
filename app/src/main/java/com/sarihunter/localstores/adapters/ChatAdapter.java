package com.sarihunter.localstores.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.classes.Chat;

import java.util.Calendar;
import java.util.Locale;

public class ChatAdapter extends FirebaseRecyclerAdapter<Chat, ChatAdapter.ChatHolder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatAdapter(@NonNull FirebaseRecyclerOptions<Chat> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatHolder holder, int position, @NonNull Chat model) {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.i("chat child", model.toString());

        if (user.getUid() == model.getSender()){
            //holder.linearLayout.setBaselineAligned(true);
            holder.linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            holder.tvChat.setBackgroundColor(0xaaaaff55);

         }else{
            holder.linearLayout.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            holder.tvChat.setBackgroundColor(0xf00fff55);

        }

        holder.tvChat.setText(model.getMsg());


        String timeStamp = model.getTimeStamp();

        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime = DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        holder.tvTime.setText(dateTime);


        //change color and gravity of text according to the user
//
//        if (FirebaseAuth.getInstance().getCurrentUser().getUid() == model.getReciever()){
//            holder.tvChat.setBackgroundColor(0xaaaaff55);
//            holder.tvChat.setGravity(Gravity.RIGHT);
//        }else{
//            holder.tvChat.setGravity(Gravity.LEFT);
//            holder.tvChat.setBackgroundColor(0xf00fff55);
//        }

    }



    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item,parent,false));
    }

    static class ChatHolder extends RecyclerView.ViewHolder{

        TextView tvChat;
        TextView tvTime;
        LinearLayout linearLayout;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            tvChat = itemView.findViewById(R.id.tvMsg);
            tvTime = itemView.findViewById(R.id.tvStamp);
            linearLayout = itemView.findViewById(R.id.linearLayoutChat);
        }
    }


    //TODO Notification for users got the msg

    public void notificationContent(Context c, String textTitle, String textContent){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(c, "1")
                .setSmallIcon(R.drawable.store)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }



}
