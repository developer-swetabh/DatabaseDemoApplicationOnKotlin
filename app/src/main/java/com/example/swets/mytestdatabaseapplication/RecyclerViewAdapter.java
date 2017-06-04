package com.example.swets.mytestdatabaseapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
 * Created by swets on 07-01-2016.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter {

    private LayoutInflater inflater;
    private Context context;
    List<Model> modelArrayList= Collections.emptyList();

    public RecyclerViewAdapter(Context context,List<Model> modelArrayList){
        this.context=context;
        this.modelArrayList=modelArrayList;
        inflater=LayoutInflater.from(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.recyclerview_item, parent,false);
        return new ItemHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemHolder itemHolder= (ItemHolder) holder;
        Model model=modelArrayList.get(position);
        itemHolder.tv_Id.setText(model.getId() + "");
        itemHolder.tv_name.setText(model.getName() + "");
        itemHolder.tv_address.setText(model.getAddress()+"");
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public void refreshData() {
        DBHelper dbHelper=new DBHelper(context);
        modelArrayList=dbHelper.getAllRows();
        notifyDataSetChanged();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        TextView tv_Id,tv_name,tv_address;

        public ItemHolder(View itemView) {
            super(itemView);
            tv_Id=(TextView)itemView.findViewById(R.id.tv_Id);
            tv_name=(TextView)itemView.findViewById(R.id.tv_name);
            tv_address=(TextView)itemView.findViewById(R.id.tv_address);
        }
    }
}
