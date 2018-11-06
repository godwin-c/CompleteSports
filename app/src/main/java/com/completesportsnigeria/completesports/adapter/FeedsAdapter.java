package com.completesportsnigeria.completesports.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.completesportsnigeria.completesports.R;
import com.completesportsnigeria.completesports.activity.SelectedNewsActivity;
import com.completesportsnigeria.completesports.classes.FeedItem;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedsAdapter extends RecyclerView.Adapter<FeedsAdapter.MyViewHolder> {
    ArrayList<FeedItem> feedItems;
    private static int page_position;

    private static final int CONTENT = 0;
    private static final int AD = 1;

    Context context;

    public FeedsAdapter(Context context, ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.custum_row_news_item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");

        YoYo.with(Techniques.FadeIn).playOn(holder.cardView);
        FeedItem current = feedItems.get(position);
        holder.Title.setTypeface(typeface);
        holder.Description.setTypeface(typeface);
        holder.Date.setTypeface(typeface);

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm a");
        String date = formatter.format(Date.parse(current.getPubDate()));

        holder.Title.setText(current.getTitle());
        holder.Description.setText(Html.fromHtml(current.getDescription()));
        holder.Date.setText(date);
        Picasso.get().load(current.getThumbnailUrl()).into(holder.Thumbnail);

    }


    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView Title, Description, Date;
        ImageView Thumbnail;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            Title = (TextView) itemView.findViewById(R.id.title_text);
            Description = (TextView) itemView.findViewById(R.id.description_text);
            Date = (TextView) itemView.findViewById(R.id.date_text);
            Thumbnail = (ImageView) itemView.findViewById(R.id.thumb_img);
            cardView = (CardView) itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    FeedItem feedItem = feedItems.get(position);
                    page_position = position;

                    Bundle bundle = new Bundle();
                    bundle.putString("url", feedItem.getLink());
                    bundle.putString("title", feedItem.getTitle());

                    Intent intent = new Intent(context, SelectedNewsActivity.class);
                    intent.putExtra("position", page_position);
                    intent.putExtra("feeditems", feedItems);

                    context.startActivity(intent);

                }
            });
        }


    }

    public void setItems(ArrayList<FeedItem> feedItems) {
        this.feedItems = feedItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 5 == 0)
            return AD;
        return CONTENT;
    }
}
