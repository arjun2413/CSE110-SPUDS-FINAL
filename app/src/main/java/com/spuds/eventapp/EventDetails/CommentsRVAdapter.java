package com.spuds.eventapp.EventDetails;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Comment;

import java.util.List;

/**
 * Created by tina on 5/4/16.
 */

public class CommentsRVAdapter extends RecyclerView.Adapter<CommentsRVAdapter.CommentViewHolder> {

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        ImageView commentPic;
        TextView commentName;
        TextView commentDate;
        TextView commentText;
        Button commentFollow;
        Button commentReply;
        Button commentViewReplies;

        CommentViewHolder(View itemView) {
            super(itemView);

            commentPic = (ImageView) itemView.findViewById(R.id.comment_pic);
            commentName = (TextView) itemView.findViewById(R.id.comment_date);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);
            commentDate = (TextView) itemView.findViewById(R.id.comment_date);
            commentFollow = (Button) itemView.findViewById(R.id.button_follow);
            commentReply = (Button) itemView.findViewById(R.id.button_reply);
            commentViewReplies = (Button) itemView.findViewById(R.id.button_view_replies);
        }

    }

    List<Comment> comments;

    public CommentsRVAdapter(List<Comment> comments){
        this.comments = comments;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        CommentViewHolder cvh = new CommentViewHolder(v);
        return cvh;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder commentViewHolder, int i) {
        /* Picasso for commentPic*/
        commentViewHolder.commentName.setText(comments.get(i).commentName);
        commentViewHolder.commentDate.setText(comments.get(i).commentDate);
        commentViewHolder.commentText.setText(comments.get(i).commentText);

        //on click listeners
        commentViewHolder.commentFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        commentViewHolder.commentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        commentViewHolder.commentViewReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
