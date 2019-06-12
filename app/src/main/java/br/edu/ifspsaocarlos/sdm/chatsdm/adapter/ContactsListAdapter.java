package br.edu.ifspsaocarlos.sdm.chatsdm.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Contato;
import br.edu.ifspsaocarlos.sdm.chatsdm.view.ChatActivity;

public class ContactsListAdapter extends RecyclerView.Adapter<ContactsListAdapter.ContactsListViewHolder> {

    private ArrayList<Contato> contactList;
    private static ItemClickListener itemClickListener;

    public class ContactsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView nameContactsList;

        public ContactsListViewHolder(final View itemView) {
            super(itemView);
            nameContactsList = itemView.findViewById(R.id.tv_name_contacts_list);
            nameContactsList.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == this.nameContactsList) {
                Contato c = contactList.get(getAdapterPosition());

                Intent intent = new Intent(view.getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.RECEIVER_ID, c.getId());
                intent.putExtra(ChatActivity.RECEIVER_NAME, c.getNomeCompleto());
                view.getContext().startActivity(intent);
            }
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public ContactsListAdapter(ArrayList<Contato> exampleList) {
        contactList = exampleList;
    }

    @NonNull
    @Override
    public ContactsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacts_list, parent, false);
        ContactsListViewHolder contactsListViewHolder = new ContactsListViewHolder(view);
        return contactsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactsListViewHolder holder, final int position) {
        Contato currentItem = contactList.get(position);
        holder.nameContactsList.setText(currentItem.getNomeCompleto());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }
}