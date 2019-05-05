package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofit.R;
import com.example.retrofit.model.Temperature;

import java.util.List;

public class Adapter_2 extends RecyclerView.Adapter<Adapter_2.Holer> {

    private Context mContext;
    private List<Temperature> temperatureList;
    int counter=5;


    public Adapter_2(Context mContext, List<Temperature> temperatureList) {
        this.mContext = mContext;
        this.temperatureList = temperatureList;



    }

    @NonNull
    @Override
    public Holer onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.cardview_layout,parent,false);
        return new Holer(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holer holder, int position) {
        Temperature temperature=temperatureList.get(position);
        holder.temp.setText("Current temperature is :"+temperature.getTemp());
        holder.ph.setText("Current Ph Level is :"+temperature.getPh());
    }

    @Override
    public int getItemCount() {
        if (temperatureList.size()==0){
            return 0;
        }
        else if (temperatureList.size()==1){
            return 1;
        }
        else if (temperatureList.size()==2){
            return 2;
        }
        else if (temperatureList.size()==3){
            return 3;
        }
        else if (temperatureList.size()==4){
            return 4;
        }
        else {
            return 5;
        }

    }

    public class Holer extends RecyclerView.ViewHolder {
        TextView temp,ph;
        public Holer(@NonNull View itemView) {
            super(itemView);
            temp=itemView.findViewById(R.id.nameTextView);
            ph=itemView.findViewById(R.id.emailTextView);
        }
    }

}
