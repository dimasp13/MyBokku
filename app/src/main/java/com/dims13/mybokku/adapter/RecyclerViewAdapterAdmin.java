package com.dims13.mybokku.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dims13.mybokku.Akses.Admin.LihatBuku;
import com.dims13.mybokku.Akses.Admin.UpdateBuku;
import com.dims13.mybokku.model.Buku;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import dims13.mybokku.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapterAdmin extends RecyclerView.Adapter<RecyclerViewAdapterAdmin.ViewHolder> implements Filterable {


    private ArrayList<Buku> listBuku;
    private ArrayList<Buku> listBuku_full;
    private Context context;

    public interface dataListener {
        void onDeleteData(Buku data, int position);
    }

    dataListener listener2;

    public RecyclerViewAdapterAdmin(ArrayList<Buku> listBuku, Context context) {
        this.listBuku = listBuku;
        listBuku_full = new ArrayList<>(listBuku);
        this.context = context;
        listener2 = (LihatBuku)context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_design_buku, parent, false);
        return new ViewHolder(V);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String nama_buku = listBuku.get(position).getNama_buku();
        final String nama_loket = listBuku.get(position).getNama_loket();
        final String harga = listBuku.get(position).getHarga();
        final String gambar = listBuku.get(position).getGambar();

        //FUNGSI REPLACE PADA URL GAMBAR
        String sumber = gambar;
        String a1 = sumber.replace("o?name=", "o/");
        String a2 = a1.replace("&uploadType=resumable", "?alt=media&uploadType=resumable");
        String a3 = a2.replace("&upload_id", "&upload_ids");
        String a4 = a3.replace("&upload_protocol=resumable", "");
        String imgsumber = a4;

        holder.nama_buku.setText("Judul : " + nama_buku);
        holder.nama_loket.setText("Loket : " + nama_loket);
        Glide.with(context).load(imgsumber).into(holder.gambar);
        holder.harga.setText("Harga : " + harga + " /hari");

        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                Bundle bundle = new Bundle();
                                bundle.putString("datanama_buku", listBuku.get(position).getNama_buku());
                                bundle.putString("datanama_loket", listBuku.get(position).getNama_loket());
                                bundle.putString("datagambar", listBuku.get(position).getGambar());
                                bundle.putString("dataharga", listBuku.get(position).getHarga());
                                bundle.putString("getPrimaryKey", listBuku.get(position).getKey());

                                Intent intent = new Intent(v.getContext(), UpdateBuku.class);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                listener2.onDeleteData(listBuku.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBuku.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView nama_buku, nama_loket, harga;
        private ImageView gambar;
        private LinearLayout ListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nama_buku = itemView.findViewById(R.id.Nama_Buku);
            nama_loket = itemView.findViewById(R.id.Nama_Loket);
            gambar = itemView.findViewById(R.id.ImageView);
            harga = itemView.findViewById(R.id.Harga);
            ListItem = itemView.findViewById(R.id.list_item1);



        }
    }

    @Override
    public Filter getFilter() {
        return dataFilter;
    }
    private Filter dataFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Buku> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length()==0){
                filteredList.addAll(listBuku_full);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Buku item : listBuku_full){
                    if(item.getNama_buku().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        };
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listBuku.clear();
            listBuku.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

}
