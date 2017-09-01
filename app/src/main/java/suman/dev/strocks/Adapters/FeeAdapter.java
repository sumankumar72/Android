package suman.dev.strocks.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import suman.dev.strocks.Model.FeeDetail;
import suman.dev.strocks.Model.Holiday;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.R;

/**
 * Created by suman on 22/7/17.
 */

public class FeeAdapter extends RecyclerView.Adapter<FeeAdapter.ViewHolder> {
    private List<FeeDetail> fees;
    private static ItemClickListener clickListener;


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView list_fee_name,list_fee_amount,list_fee_paid;
        public View view;

        public ViewHolder(View v)
        {
            super(v);
            list_fee_name = (TextView)itemView.findViewById(R.id.list_fee_name);
            list_fee_amount = (TextView)itemView.findViewById(R.id.list_fee_amount);
            list_fee_paid = (TextView)itemView.findViewById(R.id.list_fee_paid);
            view=v;
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
        }
    }


    @Override
    public FeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fee_list, parent, false);
        FeeAdapter.ViewHolder viewHolder = new FeeAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FeeAdapter.ViewHolder holder, int position) {
        FeeDetail feeDetail = fees.get(position);
        holder.list_fee_amount.setText(feeDetail.Amount);
        holder.list_fee_name.setText(feeDetail.Name);
    }

    @Override
    public int getItemCount() {
        if(fees==null)
            return 0;
        else
            return  fees.size();
    }
    public FeeAdapter(List<FeeDetail> feeDetails)
    {
        fees=feeDetails;
    }
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
