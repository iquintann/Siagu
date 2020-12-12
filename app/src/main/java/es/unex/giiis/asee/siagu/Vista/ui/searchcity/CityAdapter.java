package es.unex.giiis.asee.siagu.Vista.ui.searchcity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.R;
import es.unex.giiis.asee.siagu.model.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    private final List<City> mItems = new ArrayList<City>();

    public interface OnCityClickListener {
        void onItemClick(City item);     //Type of the element to be returned
    }

    private final OnCityClickListener listener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CityAdapter(OnCityClickListener listener) {

        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CityAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        //Inflate the View for every element
        View v = (View) LayoutInflater.from (parent.getContext()).inflate(R.layout.city_item,parent,false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mItems.get(position),listener);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(City item) {

        mItems.add(item);
        notifyDataSetChanged();

    }

    public void clear(){

        mItems.clear();
        notifyDataSetChanged();

    }

    public Object getItem(int pos) {

        return mItems.get(pos);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView region;
        private TextView country;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.itemCityName);
            region=itemView.findViewById(R.id.itemCityRegion);
            country=itemView.findViewById(R.id.itemCityCountry);

        }

        public void bind(final City city, final OnCityClickListener listener) {

            name.setText(city.getLocation().getName());
            region.setText("Region: "+city.getLocation().getRegion());
            country.setText("País: "+city.getLocation().getCountry());

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    listener.onItemClick(city);
                }
            });
        }
    }
}
