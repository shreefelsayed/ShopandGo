package com.armjld.shopandgo.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.armjld.shopandgo.Models.Product;
import com.armjld.shopandgo.databinding.ItemProductBinding;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Product> modelList;

    public ProductAdapter(Context mContext, ArrayList<Product> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    public ArrayList<Product> getList() {
        return modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ItemProductBinding binding = ItemProductBinding.inflate(inflater, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Product model = modelList.get(position);
        holder.updateUI(model);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface OnProductClicked {
        void productClicked(Product product, int pos);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public MyViewHolder(@NonNull ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("SetTextI18n")
        public void updateUI(Product model) {

            Glide.with(mContext).load(model.getImageLink()).diskCacheStrategy(DiskCacheStrategy.DATA).into(binding.imgProduct);

            binding.txtName.setText(model.getName());
            binding.tvPrice.setText("£ " + model.getPrice());
            binding.tvCategory.setText(model.getCategory());
        }
    }
}