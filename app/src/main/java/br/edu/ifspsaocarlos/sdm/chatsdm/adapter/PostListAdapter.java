package br.edu.ifspsaocarlos.sdm.chatsdm.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import br.edu.ifspsaocarlos.sdm.chatsdm.R;
import br.edu.ifspsaocarlos.sdm.chatsdm.model.Mensagem;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.PostListViewHolder> {
    private ArrayList<Mensagem> messageList;
    private String idSharedPreferences;

    public class PostListViewHolder extends RecyclerView.ViewHolder {
        public TextView postsList;
        public ImageView imageViewUser;

        public PostListViewHolder(final View itemView) {
            super(itemView);
            postsList = itemView.findViewById(R.id.tv_post);
            imageViewUser = itemView.findViewById(R.id.iv_icon_user);
        }
    }

    public PostListAdapter (ArrayList<Mensagem> exampleList, String idSharedPreferences) {
        messageList = exampleList;
        this.idSharedPreferences = idSharedPreferences;
    }

    @NonNull
    @Override
    public PostListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_chat, parent, false);
        PostListViewHolder postListViewHolder = new PostListViewHolder(view);
        return postListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostListViewHolder holder, final int position) {
        Mensagem currentItem = messageList.get(position);
        if (currentItem.getOrigemId().equals(idSharedPreferences)) {
            holder.postsList.setText(currentItem.getCorpo());
            holder.imageViewUser.setImageResource(R.drawable.ic_user_one);
        } if (!currentItem.getOrigemId().equals(idSharedPreferences)){
            holder.postsList.setText(currentItem.getCorpo());
            holder.imageViewUser.setImageResource(R.drawable.ic_user_two);
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
