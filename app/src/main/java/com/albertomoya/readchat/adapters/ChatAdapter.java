package com.albertomoya.readchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albertomoya.readchat.R;
import com.albertomoya.readchat.activities.others.BookPostDetailActivity;
import com.albertomoya.readchat.activities.others.ChatActivity;
import com.albertomoya.readchat.persistance.Chat;
import com.albertomoya.readchat.persistance.ChatUser;
import com.albertomoya.readchat.utilities.NamesCollection;
import com.albertomoya.readchat.utilities.providers.ChatProvider;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.text.SimpleDateFormat;

public class ChatAdapter extends FirestoreRecyclerAdapter<ChatUser, ChatAdapter.ViewHolder> {
    private Context context;

    public ChatAdapter(FirestoreRecyclerOptions<ChatUser> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull ChatUser chat) {

        new ChatProvider().getChatById(chat.getUidChat()).addOnSuccessListener(it -> {
            if (it.exists()){
                holder.textViewLastMessage.setText(new SimpleDateFormat("dd-MM-yyyy").format(it.getDate(NamesCollection.COLLECTION_CHAT_TIMESTAMP)));
                String nameChat = it.getString(NamesCollection.COLLECTION_CHAT_NAME);
                String uriPhoto = it.getString(NamesCollection.COLLECTION_CHAT_PHOTO);
                holder.textViewNameChat.setText(nameChat);
                if (uriPhoto!=null)
                    if (!uriPhoto.isEmpty())
                        Glide.with(context).load(uriPhoto).override(350,350).into(holder.imageViewPhotoChat);
                    else
                        Glide.with(context).load(R.drawable.new_photo_post).override(350,350).into(holder.imageViewPhotoChat);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id",chat.getUidChat());
                context.startActivity(intent);
            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chat, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewNameChat, textViewLastMessage;
        ImageView imageViewPhotoChat;
        View mView;

        public ViewHolder(View view){
            super(view);
            textViewNameChat = view.findViewById(R.id.textViewNameChat);
            textViewLastMessage = view.findViewById(R.id.textViewLastMessageChat);
            imageViewPhotoChat = view.findViewById(R.id.circleImageViewPhotoBookChat);
            mView = view;
        }
    }
}


