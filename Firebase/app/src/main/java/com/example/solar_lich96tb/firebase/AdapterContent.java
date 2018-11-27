package com.example.solar_lich96tb.firebase;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AdapterContent extends RecyclerView.Adapter<AdapterContent.ViewModel> {
    List<User> list;

    public AdapterContent(List<User> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewModel onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,viewGroup,false);
        return new ViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewModel viewModel, int i) {
//        viewModel.txtName.setText(list.get(i).getName());
//        viewModel.txtContent.setText(list.get(i).getContent());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewModel extends RecyclerView.ViewHolder {
        TextView txtName,txtContent;
        public ViewModel(@NonNull View itemView) {
            super(itemView);
            txtContent=itemView.findViewById(R.id.txtContent);
            txtName=itemView.findViewById(R.id.txtName);
        }
    }
}
