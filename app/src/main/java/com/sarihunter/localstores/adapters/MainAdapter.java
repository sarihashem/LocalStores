package com.sarihunter.localstores.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.Category_items;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.classes.Tags;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainHolder> {

    private List<ProductAdapter> adapter;


    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Tags> tags;



    public MainAdapter(List<Tags> tags, List<ProductAdapter> adapter) {
        this.tags = tags;
        this.adapter = adapter;
    }


    @NonNull
    @Override
    public MainHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.nested_recycler,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainHolder holder, int position) {


        holder.title.setText(tags.get(position).getTag());


        holder.title.setOnClickListener(v-> {

            Intent i = new Intent(holder.title.getContext(), Category_items.class);
            i.putExtra("category", tags.get(position).getTag());
            holder.title.getContext().startActivity(i);

        });


        RecyclerView rvInternal = holder.rvInternal1;


        rvInternal.setAdapter(adapter.get(position));
        rvInternal.setLayoutManager(new LinearLayoutManager(rvInternal.getContext(),LinearLayoutManager.HORIZONTAL,false));
        rvInternal.setRecycledViewPool(viewPool);



    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    static class MainHolder extends RecyclerView.ViewHolder{

        RecyclerView rvInternal1;
        TextView title;

        public MainHolder(@NonNull View itemView) {
            super(itemView);

            rvInternal1 = itemView.findViewById(R.id.rvInternal);
            title = itemView.findViewById(R.id.tvTitle);

        }
    }
}
