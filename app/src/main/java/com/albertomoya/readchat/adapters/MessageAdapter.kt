package com.albertomoya.readchat.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.albertomoya.readchat.R
import com.albertomoya.readchat.others.inflate
import com.albertomoya.readchat.persistance.Message
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_chat_item_left.view.*
import kotlinx.android.synthetic.main.fragment_chat_item_right.view.*
import java.text.SimpleDateFormat

class MessageAdapter(val items: List<Message>, val userId: String ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val GLOBAL_MESSAGE = 1
    private val MY_MESSAGE = 2

    private val layoutRight = R.layout.fragment_chat_item_right
    private val layoutLeft = R.layout.fragment_chat_item_left

    override fun getItemViewType(position: Int) = if (items[position].uidUser == userId) MY_MESSAGE else GLOBAL_MESSAGE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MY_MESSAGE -> ViewHolderRight(parent.inflate(layoutRight))
            else -> ViewHolderLeft(parent.inflate(layoutLeft))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType){
            MY_MESSAGE -> (holder as ViewHolderRight).bind(items[position])
            GLOBAL_MESSAGE -> (holder as ViewHolderLeft).bind(items[position])
        }
    }

    override fun getItemCount() = items.size

    class ViewHolderRight(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Message) = with(itemView){
            textViewMessageRight.text = message.message
            textViewTimeRight.text = SimpleDateFormat("hh:mm").format(message.timestamp)
            if (message.userPhoto.isEmpty()){
                Glide.with(context).load(R.drawable.ic_person).circleCrop().override(100,100).into(imageViewProfileRight)
                //Picasso.with(context).load(R.drawable.ic_person).resize(100,100)
                //    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            } else {
                Glide.with(context).load(message.userPhoto).circleCrop().override(100,100).into(imageViewProfileRight)
                //Picasso.with(context).load(message.profileImageURL).resize(100,100)
                //    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            }
        }
    }

    class ViewHolderLeft(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bind(message: Message) = with(itemView){
            textViewMessageLeft.text = message.message
            textViewTimeLeft.text = SimpleDateFormat("hh:mm").format(message.timestamp)
            if (message.userPhoto.isEmpty()){
                Glide.with(context).load(R.drawable.ic_person).circleCrop().override(100,100).into(imageViewProfileLeft)
                //Picasso.with(context).load(R.drawable.ic_person).resize(100,100)
                //    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            } else {
                Glide.with(context).load(message.userPhoto).circleCrop().override(100,100).into(imageViewProfileLeft)
                //Picasso.with(context).load(message.profileImageURL).resize(100,100)
                //    .centerCrop().transform(CircleTransform()).into(imageViewProfileLeft)
            }
        }
    }
}