package com.androidacademy.msk.exerciseproject.screen.news_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidacademy.msk.exerciseproject.R;
import com.androidacademy.msk.exerciseproject.data.Section;
import com.androidacademy.msk.exerciseproject.data.model.NewsItem;
import com.androidacademy.msk.exerciseproject.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    @NonNull
    private List<NewsItem> news = new ArrayList<>();
    @NonNull
    private final OnItemClickListener clickListener;
    @NonNull
    private final LayoutInflater inflater;

    public NewsAdapter(@NonNull OnItemClickListener clickListener,
                       @NonNull Context context) {
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                inflater.inflate(viewType, parent, false),
                clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(news.get(position));
    }

    @Override
    public int getItemCount() {
        return news.size();
    }

    @Override
    public int getItemViewType(int position) {
        try {
            Section section = Section.valueOf(news.get(position).getSection().toUpperCase());

            switch (section) {
                case TECHNOLOGY:
                    return R.layout.item_technology_news;
                default:
                    return R.layout.item_common_news;
            }
        } catch (IllegalArgumentException e) {
            return R.layout.item_common_news;

        }
    }

    public void addListData(List<NewsItem> newsItems) {
        news.clear();
        news.addAll(newsItems);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView categoryTextView;
        private final TextView titleTextView;
        private final TextView previewTextView;
        private final TextView publishDateTextView;
        private final ImageView imageView;

        ViewHolder(@NonNull View itemView, @NonNull OnItemClickListener listener) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.item_news__textview_category);
            titleTextView = itemView.findViewById(R.id.item_news__textview_title);
            previewTextView = itemView.findViewById(R.id.item_news__textview_preview);
            publishDateTextView = itemView.findViewById(R.id.item_news__textview_publish_date);
            imageView = itemView.findViewById(R.id.item_news__imageview_news);

            itemView.setOnClickListener(v -> {
                int position = ViewHolder.this.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(news.get(position).getUrl());
                }
            });
        }

        private void bind(@NonNull NewsItem newsItem) {
            String section = newsItem.getSection();
            categoryTextView.setText(section);
            titleTextView.setText(newsItem.getTitle());
            previewTextView.setText(newsItem.getAbstractX());

            String publishDate = DateUtils.convertTimestampToString(
                    newsItem.getPublishedDate(),
                    inflater.getContext());
            publishDateTextView.setText(publishDate);

            if (newsItem.getMultimedia().size() != 0) {
                imageView.setVisibility(View.VISIBLE);
                //get the position of the best quality preview image - it is always
                // the second position from the end of a list
                int previewImagePosition = newsItem.getMultimedia().size() - 2;
                String previewImageUrl = newsItem.getMultimedia().get(previewImagePosition).getUrl();
                Picasso.get().load(previewImageUrl).into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String url);
    }
}