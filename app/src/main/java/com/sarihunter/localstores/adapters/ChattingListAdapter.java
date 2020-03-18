package com.sarihunter.localstores.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sarihunter.localstores.ChattingActivity;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.classes.User;

import java.util.List;

public class ChattingListAdapter extends RecyclerView.Adapter<ChattingListAdapter.ChatListHolder> {

    List<User> userList;

    public ChattingListAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ChatListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        return new ChatListHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListHolder holder, int position) {

        holder.tvSenderName.setText(userList.get(position).getEmail());


        try {


            //Picasso.with(holder.ivSenderImg.getContext()).load(userList.get(position).getPhotoURL()).into(holder.ivSenderImg);
            Glide.with(holder.ivSenderImg.getContext())
                    .load(userList.get(position).getPhotoURL())
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.ivSenderImg);

        } catch (Exception e) {
            e.printStackTrace();
        }



        holder.tvSenderName.setOnClickListener(v->{
            v.getContext().startActivity(new Intent(holder.ivSenderImg.getContext(), ChattingActivity.class)
                    .putExtra("owner", userList.get(position).getEmail())
                    .putExtra("OwnerID",userList.get(position).getUserID()));
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ChatListHolder extends RecyclerView.ViewHolder{

        ImageView ivSenderImg;
        TextView tvSenderName;


        public ChatListHolder(@NonNull View itemView) {
            super(itemView);

            tvSenderName = itemView.findViewById(R.id.tvSenderNameEmail);
            ivSenderImg = itemView.findViewById(R.id.ivSenderImg);
        }
    }
}
