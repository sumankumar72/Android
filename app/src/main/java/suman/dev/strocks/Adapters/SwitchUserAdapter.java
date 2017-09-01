package suman.dev.strocks.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import suman.dev.strocks.Model.ChildModel;
import suman.dev.strocks.Model.ItemClickListener;
import suman.dev.strocks.Model.UserClassData;
import suman.dev.strocks.R;

/**
 * Created by suman on 5/8/17.
 */

public class SwitchUserAdapter extends ArrayAdapter<ChildModel> implements View.OnClickListener {

    private ArrayList<ChildModel> dataset;
    private Context context;
    private Animation animShow, animHide;
    private static ItemClickListener clickListener;

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        if (clickListener != null)
            clickListener.onClick(v, position);
    }


    public static class ViewHolder{
        TextView txtName;

    }

    public SwitchUserAdapter(ArrayList<ChildModel> childs, Context context) {
        super(context, R.layout.user_list, childs);
        this.dataset = childs;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChildModel model = dataset.get(position);
        final ViewHolder holder;

        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.user_list, parent, false);

        if (convertView != null) {
            holder = new ViewHolder();
            holder.txtName = (TextView)convertView.findViewById(R.id.txtName);
            holder.txtName.setText(model.GetFullName());
            holder.txtName.setTag(position);
            holder.txtName.setOnClickListener(this);
            holder.txtName.setTextColor(Color.parseColor("#000000"));
            convertView.setTag(holder);

            if(model.Selected)
                holder.txtName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_circle_black_24dp, 0);
        }
        return convertView;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}
