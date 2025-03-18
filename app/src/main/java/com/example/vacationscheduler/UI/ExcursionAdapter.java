package com.example.vacationscheduler.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacationscheduler.R;
import com.example.vacationscheduler.database.Repository;
import com.example.vacationscheduler.entities.Excursion;
import com.example.vacationscheduler.entities.Vacation;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;
    public ExcursionAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionItemView;
        private final TextView excursionItemView2;
        private final TextView excursionItemView3;


        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.textView3);
            excursionItemView2 = itemView.findViewById(R.id.textView4);
            excursionItemView3 = itemView.findViewById(R.id.textView5);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcursionId());
                    intent.putExtra("title", current.getExcursionTitle());
                    intent.putExtra("date", current.getExcursionDate());
                    intent.putExtra("vacayId", current.getVacationId());
                    context.startActivity(intent);
                }
            });
        }

    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if(mExcursions != null){
            Excursion current = mExcursions.get(position);
            String title = current.getExcursionTitle();
            int vacayId = current.getVacationId();
            String date = current.getExcursionDate();
            holder.excursionItemView.setText(title);
            holder.excursionItemView2.setText(Integer.toString(vacayId));
            holder.excursionItemView3.setText(date);
        } else {
            holder.excursionItemView.setText("No Excursion Title");
            holder.excursionItemView.setText("No Vacation Id");
            holder.excursionItemView3.setText("No date");

        }
    }

    @Override
    public int getItemCount() {
        if(mExcursions!= null){
            return mExcursions.size();
        } else {
            return 0;
        }
    }

    public void setExcursions(List<Excursion> excursions){
        mExcursions = excursions;
        notifyDataSetChanged();
    }
}
