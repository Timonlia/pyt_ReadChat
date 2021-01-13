package com.albertomoya.readchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albertomoya.readchat.R;
import com.albertomoya.readchat.activities.others.BookPostDetailActivity;
import com.albertomoya.readchat.activities.others.SelectChapterToReadBookActivity;
import com.albertomoya.readchat.persistance.Book;
import com.albertomoya.readchat.persistance.FavouriteBook;
import com.albertomoya.readchat.utilities.NamesCollection;
import com.albertomoya.readchat.utilities.providers.AuthProvider;
import com.albertomoya.readchat.utilities.providers.BookProvider;
import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookFavAdapter extends FirestoreRecyclerAdapter<FavouriteBook, BookFavAdapter.ViewHolder> {
    private Context context;

    public BookFavAdapter(FirestoreRecyclerOptions<FavouriteBook> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FavouriteBook book) {

        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String postBookId = document.getId();

        new BookProvider().getPostById(postBookId).addOnSuccessListener(it -> {
            if (it.exists()) {
                String title = it.getString(NamesCollection.COLLECTION_BOOK_TITLE);
                String category = it.getString(NamesCollection.COLLECTION_BOOK_CATEGORY);
                String tituloReducido = "";
                if (title.length() > 15) {
                    tituloReducido = title.substring(0, 15) + "...";
                    holder.textViewBookTitle.setText(tituloReducido);
                } else {
                    holder.textViewBookTitle.setText(title);
                }

                holder.textViewBookCategory.setText(category);
                if (it.getString(NamesCollection.COLLECTION_BOOK_PHOTO) != null)
                    if (!it.getString(NamesCollection.COLLECTION_BOOK_PHOTO).isEmpty())
                        Glide.with(context).load(it.getString(NamesCollection.COLLECTION_BOOK_PHOTO)).override(350, 350).into(holder.imageViewBookPhoto);
                    else
                        Glide.with(context).load(R.drawable.new_photo_post).override(350, 350).into(holder.imageViewBookPhoto);
            }

        });
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SelectChapterToReadBookActivity.class);
                intent.putExtra("id",postBookId);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_post_book_fav, parent, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBookTitle, textViewBookCategory;
        ImageView imageViewBookPhoto;
        View mView;

        public ViewHolder(View view){
            super(view);
            textViewBookTitle = view.findViewById(R.id.textViewTitleBookPostFav);
            textViewBookCategory = view.findViewById(R.id.textViewCategoryBookPostFav);
            imageViewBookPhoto = view.findViewById(R.id.imageViewPhotoBookPostFav);
            mView = view;
        }
    }
}

