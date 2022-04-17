package com.examples.jsondatamanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.examples.jsondatamanager.model.Person;

class PersonViewHolder extends RecyclerView.ViewHolder {
    private final TextView personName;

    private PersonViewHolder(View itemView) {
        super(itemView);
        personName = itemView.findViewById(R.id.text_view_person_name);
    }

    public void bind(Person person) {
        String fullName = person.getFirstName() + " " + person.getLastName();

        personName.setText(fullName);
    }

    static PersonViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_recycler_view_item, parent, false);
        return new PersonViewHolder(view);
    }
}

public class PersonListAdapter extends ListAdapter<Person, PersonViewHolder> {
    protected PersonListAdapter(@NonNull DiffUtil.ItemCallback<Person> diffCallback) {
        super(diffCallback);
    }

    public interface OnItemClickListener {
        void onItemClick(Person person);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    static class WordDiff extends DiffUtil.ItemCallback<Person> {
        @Override
        public boolean areItemsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Person oldItem, @NonNull Person newItem) {
            return oldItem.getId() == newItem.getId();
        }
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return PersonViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, int position) {
        Person person = getItem(position);
        holder.bind(person);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(person);
            }
        });
    }
}
