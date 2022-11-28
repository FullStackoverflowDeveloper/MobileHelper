package com.program.supgnhelper.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.program.supgnhelper.R;
import com.program.supgnhelper.model.Item;
import com.program.supgnhelper.model.enums.Role;
import com.program.supgnhelper.model.enums.Status;
import com.program.supgnhelper.model.enums.Type;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private final List<Item> items;
    private final Role role;
    private ClickListener listener;

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public ItemAdapter(List<Item> items, Role role) {
        this.items = items;
        this.role = role;
    }

    // Подкласс олицетворяющий элемент списка
    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView address, phone, name, comment;
        final ImageView drawComment;
        final Button execute;
        Item item;

        public ItemHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            name = itemView.findViewById(R.id.clientname);
            execute = itemView.findViewById(R.id.execute);
            comment = itemView.findViewById(R.id.desk_comment);
            drawComment = itemView.findViewById(R.id.draw_comment);

            if (role == Role.MASTER){
                execute.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(this);

            execute.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.itemClick(getBindingAdapterPosition());
                }
            });
        }

        @Override
        public void onClick(View view) {
            listener.viewClick(getBindingAdapterPosition());
        }
    }

    // Генерация списка
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item2, parent, false);
        return new ItemHolder(item);
    }

    // Генерация элементов списка
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item item = items.get(position);
        holder.name.setText(item.getFamily() + " "  + item.getName() + " " + item.getPatronym());
        holder.phone.setText(item.getPhoneNumber());
        holder.address.setText(item.getFullAddress());
        holder.item = items.get(position);
        holder.drawComment.setImageResource(R.drawable.comment);
        if (item.getStatus() == Status.WORK){
            if (item.getType() == Type.COUNTER_REPLACEMENT){
                holder.comment.setText("Замена счетчика");
            }else {
                holder.comment.setText("Утечка газа");
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface ClickListener{
        void viewClick(int position);
        void itemClick(int position);
    }
}
