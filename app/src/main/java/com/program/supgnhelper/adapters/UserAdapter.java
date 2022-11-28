package com.program.supgnhelper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.program.supgnhelper.R;
import com.program.supgnhelper.model.User;

import java.util.List;

// Адаптер для списка работников
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private final List<User> users;
    private ClickListener listener;

    public void setClickListener(ClickListener listener) {
        this.listener = listener;
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    // Подкласс олицетворяющий элемент списка
    public class UserHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView name;
        User user;

        public UserHolder(View userView) {
            super(userView);
            name = userView.findViewById(R.id.name);
            userView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.userClick(getBindingAdapterPosition());
        }
    }

    // Генерация списка
    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View user = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new UserHolder(user);
    }

    // Генерация элементов списка
    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.name.setText(users.get(position).getFullName());
        holder.user = users.get(position);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public interface ClickListener{
        void userClick(int position);
    }
}