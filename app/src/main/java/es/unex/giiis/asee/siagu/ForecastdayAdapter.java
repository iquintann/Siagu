package es.unex.giiis.asee.siagu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import es.unex.giiis.asee.siagu.model.City;
import es.unex.giiis.asee.siagu.model.Forecastday;
import es.unex.giiis.asee.siagu.ui.searchcity.CityAdapter;

public class ForecastdayAdapter extends RecyclerView.Adapter<ForecastdayAdapter.ViewHolder> {
    private final List<Forecastday> mItems = new ArrayList<Forecastday>();

    public ForecastdayAdapter(OnCityClickListener listener) {
        this.listener = listener;
    }

    public interface OnCityClickListener {
        void onItemClick(Forecastday item);     //Type of the element to be returned
    }

    private final OnCityClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.forecastday_item, parent, false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mItems.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
    public void add(Forecastday item) {

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
        private TextView date;
        //Soleado nublado (current.gettext)
        private TextView dateWeather;
        private TextView weather;
        private TextView maxTmp;
        private TextView minTmp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateWeather = itemView.findViewById(R.id.textViewDate);
            weather = itemView.findViewById(R.id.textViewWheater);
            maxTmp = itemView.findViewById(R.id.textViewTempMax);
            minTmp = itemView.findViewById(R.id.textViewTempMin);


        }

        public void bind(final Forecastday forecastday,final OnCityClickListener listener) {
            dateWeather.setText(forecastday.getDate());
            weather.setText(forecastday.getDay().getCondition().getText());
            maxTmp.setText(forecastday.getDay().getMaxtempC().toString()+"º");
            minTmp.setText(forecastday.getDay().getMintempC().toString()+"º");

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(forecastday);
                }
            });
        }
    }
}
