package com.example.ssanusi.advert.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ssanusi.advert.model.AdvertDetails;
import com.example.ssanusi.advert.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HomeadvertAdapter extends RecyclerView.Adapter<HomeadvertAdapter.ViewHolder> implements Filterable {
    private  List<AdvertDetails> advertDetailsList;
    private List<AdvertDetails> advertDetailsListFiltered;
    private onClistener listener;
    private SearchFilter searchFilter;
    Context context;

    public HomeadvertAdapter(onClistener listerner, Context context) {
        this.listener = listerner;
        this.context = context;
    }


    public interface onClistener {
        void toview(AdvertDetails advertDetails);
        void onSizeChanged(int size);
    }

    public void swapItem(List<AdvertDetails> newItem) {
        if (newItem == null) return;

        if (advertDetailsList != null) advertDetailsList.clear();
        if (advertDetailsListFiltered != null) advertDetailsListFiltered.clear();
        advertDetailsList = newItem;
        advertDetailsListFiltered = newItem;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HomeadvertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adverts_recyler, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeadvertAdapter.ViewHolder holder, int position) {
        AdvertDetails populator = advertDetailsListFiltered.get(position);
        holder.bindViews(populator);
    }

    @Override
    public int getItemCount() {
        return advertDetailsListFiltered == null ? 0 : advertDetailsListFiltered.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categories, description, companyName, date;
        ImageView logo, toView;

        public ViewHolder(View itemView) {
            super(itemView);
            categories = itemView.findViewById(R.id.categories);
            description = itemView.findViewById(R.id.description);
            companyName = itemView.findViewById(R.id.companyName);
            date = itemView.findViewById(R.id.date);
            logo = itemView.findViewById(R.id.logo);
            toView = itemView.findViewById(R.id.toview);
        }

        public void bindViews(AdvertDetails advertDetails) {
            categories.setText(advertDetails.getCategories());
            description.setText(advertDetails.getDescription());
            companyName.setText(advertDetails.getCompanyName());
            date.setText(advertDetails.getDate());
            logo.setImageResource(advertDetails.getLogo());
            toView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            AdvertDetails advertDetails = advertDetailsListFiltered.get(getAdapterPosition());
            if (listener != null) {
                listener.toview(advertDetails);
            }
        }
    }

    @Override
    public Filter getFilter() {
        if (searchFilter == null) {
            searchFilter = new SearchFilter();
        }
        return searchFilter;
    }

    /**
     * Filter for item list
     * Filter content in item list according to the search text
     */
    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                List<AdvertDetails> tempItems = new ArrayList<>();
                String query = constraint.toString().toLowerCase(Locale.getDefault());
                // search content in items list
                for (AdvertDetails item : advertDetailsList) {
                    if (item != null) {
                        String source = item.getCompanyName().toLowerCase(Locale.getDefault());
                         if (source.contains(query)) {
                            tempItems.add(item);
                         }
                    }
                }
                filterResults.count = tempItems.size();
                filterResults.values = tempItems;
            } else {
                filterResults.count = advertDetailsList.size();
                filterResults.values = advertDetailsList;
            }
            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            advertDetailsListFiltered = (List<AdvertDetails>) results.values;
            if (listener != null){
                listener.onSizeChanged(advertDetailsListFiltered.size());
            }
            notifyDataSetChanged();
        }
    }
}
