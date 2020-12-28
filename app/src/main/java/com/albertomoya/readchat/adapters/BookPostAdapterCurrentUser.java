package com.albertomoya.readchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albertomoya.readchat.R;
import com.albertomoya.readchat.persistance.Book;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class BookPostAdapterCurrentUser extends FirestoreRecyclerAdapter<Book, BookPostAdapterCurrentUser.ViewHolder> {
    private Context context;

    public BookPostAdapterCurrentUser(FirestoreRecyclerOptions<Book> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Book book) {
        holder.textViewBookTitle.setText(book.getTitleBook());
        holder.textViewBookCategory.setText(book.getCategoryBook());
        if (book.getPhotoBook()!=null)
            if (!book.getPhotoBook().isEmpty())
                Glide.with(context).load(book.getPhotoBook()).override(350,350).into(holder.imageViewBookPhoto);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post_book_current_user, parent, false);

        return new BookPostAdapterCurrentUser.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBookTitle, textViewBookCategory;
        ImageView imageViewBookPhoto;

        public ViewHolder(View view){
            super(view);
            textViewBookTitle = view.findViewById(R.id.textViewTitleBookPostCurrentUser);
            textViewBookCategory = view.findViewById(R.id.textViewCategoryBookPostCurrentUser);
            imageViewBookPhoto = view.findViewById(R.id.imageViewPhotoBookPostCurrentUser);

        }
    }
}