package com.albertomoya.readchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.albertomoya.readchat.R;
import com.albertomoya.readchat.activities.others.BookWriteChapterActivity;
import com.albertomoya.readchat.activities.others.ReadChapterBookActivity;
import com.albertomoya.readchat.persistance.Chapter;
import com.albertomoya.readchat.utilities.NamesCollection;
import com.albertomoya.readchat.utilities.providers.BookProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookChapterFavAdapter extends FirestoreRecyclerAdapter<Chapter, BookChapterFavAdapter.ViewHolder> {

    private Context context;

    public BookChapterFavAdapter(FirestoreRecyclerOptions<Chapter> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chapter chapter) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String chapterId = document.getId();
        String idBook = document.getString(NamesCollection.COLLECTION_BOOK_CAPS_UID_BOOK);

        holder.textViewChapterTitle.setText(chapter.getTitleChapter());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ReadChapterBookActivity.class);
                intent.putExtra("id",chapterId);
                intent.putExtra("idBook",idBook);
                context.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chapter_without_option, parent, false);

        return new BookChapterFavAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewChapterTitle;
        View mView;

        public ViewHolder(View view){
            super(view);
            textViewChapterTitle = view.findViewById(R.id.textViewChapterTitleCardViewFav);
            mView = view;
        }
    }


}
