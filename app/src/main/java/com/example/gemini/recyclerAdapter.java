package com.example.gemini;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.viewHolder>{

    Context context;
    List<Article> articleList;
    recyclerAdapter(Context context, List<Article> articleList){
        this.context=context;
        this.articleList=articleList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
        return new viewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        holder.heading.setText(articleList.get(position).getTitle());
        holder.source.setText(articleList.get(position).getSource().getName());
        Picasso.get().load(articleList.get(position).getUrlToImage()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView heading,source;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.newsImage);
            heading=itemView.findViewById(R.id.heading);
            source=itemView.findViewById(R.id.source);
        }
    }
}
