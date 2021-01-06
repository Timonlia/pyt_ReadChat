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
import com.albertomoya.readchat.persistance.Chapter;
import com.albertomoya.readchat.utilities.NamesCollection;
import com.albertomoya.readchat.utilities.providers.BookProvider;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class BookChapterAdapter extends FirestoreRecyclerAdapter<Chapter, BookChapterAdapter.ViewHolder> {

    private Context context;

    public BookChapterAdapter(FirestoreRecyclerOptions<Chapter> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Chapter chapter) {
        DocumentSnapshot document = getSnapshots().getSnapshot(position);
        String chapterId = document.getId();
        String idBook = document.getString(NamesCollection.COLLECTION_BOOK_CAPS_UID_BOOK);

        holder.textViewChapterTitle.setText(chapter.getTitleChapter());
        holder.imageButtonOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(holder.imageButtonOption.getContext(),holder.imageButtonOption );
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_caps, popup.getMenu());
                popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position, chapterId,idBook));
                popup.show();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BookWriteChapterActivity.class);
                intent.putExtra("id",chapterId);
                intent.putExtra("idBook",idBook);
                context.startActivity(intent);
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_chapter, parent, false);

        return new BookChapterAdapter.ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewChapterTitle;
        ImageButton imageButtonOption;
        View mView;

        public ViewHolder(View view){
            super(view);
            textViewChapterTitle = view.findViewById(R.id.textViewChapterTitleCardView);
            imageButtonOption = view.findViewById(R.id.overflow);
            mView = view;
        }
    }

    static class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        private int position;
        private String uidChapter;
        private String uidBook;
        public MyMenuItemClickListener(int position, String chapter, String uidBook) {
            this.position=position;
            this.uidChapter=chapter;
            this.uidBook=uidBook;

        }
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.delete:
                    new BookProvider().deleteChapter(uidChapter,uidBook);
                default:
            }
            return false;
        }
    }
}
