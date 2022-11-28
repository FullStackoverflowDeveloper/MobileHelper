package com.program.supgnhelper.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Item;

import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.ItemHolder> {
    private final List<Item> items;
    private Context context;
    private CatalogAdapter.ClickListener listener;

    public void setClickListener(CatalogAdapter.ClickListener listener) {
        this.listener = listener;
    }

    public CatalogAdapter(List<Item> items, Context context) {
        this.items = items;
        this.context = context;
    }

    // Подкласс олицетворяющий элемент списка
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView number, name;
        final CardView cardView;

        public ItemHolder(View itemView) {
            super(itemView);

            number = itemView.findViewById(R.id.number);
            name = itemView.findViewById(R.id.fio);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.itemClick(getBindingAdapterPosition());
        }
    }
    @NonNull
    @Override
    public CatalogAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item3, parent, false);
        return new CatalogAdapter.ItemHolder(item);
    }

    // Установка цветов и значений
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = items.get(position);
        holder.number.setText(String.valueOf(item.getNumberCounter()));
        holder.name.setText(item.getFamily() + " "  + item.getName().toCharArray()[0] + " " + item.getPatronym().toCharArray()[0]);

        int color;
        switch (item.getStatus()){
            case ADDRESS:
                color = context.getColor(R.color.light_grey);
                break;
            case PROBLEM:
                color = context.getColor(R.color.red);
                break;
            case WORK:
                color = context.getColor(R.color.dark);
                break;
            default:
                color = context.getColor(R.color.yellow);
                break;
        }
        holder.cardView.setCardBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ClickListener{
        void itemClick(int position);
    }
}
