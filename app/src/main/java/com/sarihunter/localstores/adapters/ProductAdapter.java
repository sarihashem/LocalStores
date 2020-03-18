package com.sarihunter.localstores.adapters;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.ProductDetailss;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.classes.Items;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.squareup.picasso.Picasso;

public class ProductAdapter extends FirebaseRecyclerAdapter<Items,ProductAdapter.ProductHolder> {




    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Items> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductHolder holder, int position, @NonNull Items model) {

//        Picasso.with().load(model.getPhotoUrl())
//                .into((holder.itemImg);
        Intent i = new Intent(holder.name.getContext(), ProductDetailss.class);

        Bundle bundle = new Bundle();
        bundle.putString("OwnerID",model.getOwnerID());
        i.putExtra("itemID",model.getId());
        i.putExtra("owner",model.getOwner());
        i.putExtras(bundle);




        holder.name.setText(model.getName());
        holder.price.setText(String.valueOf(model.getPrice()));

        downloadItemImg(holder, model);

        holder.itemImg.setOnClickListener(v -> {

            v.getContext().startActivity(i);

        });



    }



    public void downloadItemImg(@NonNull ProductHolder holder, @NonNull Items model) {
        try {

            Picasso.with(holder.itemImg.getContext()).load(model.getPhotoUrl()).into(holder.itemImg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false));
    }

    static class ProductHolder extends RecyclerView.ViewHolder{

        private TextView name,price;
        private ImageView itemImg;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvItemName);
            price=itemView.findViewById(R.id.tvItemPrice);
            itemImg=itemView.findViewById(R.id.ivItem);

        }
    }
}
