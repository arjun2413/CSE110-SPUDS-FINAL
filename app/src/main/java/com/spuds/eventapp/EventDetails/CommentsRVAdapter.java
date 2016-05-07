package com.spuds.eventapp.EventDetails;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.CreateComment.CreateCommentFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Comment;
import com.spuds.eventapp.Shared.MainActivity;

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
        ImageView commentFollow;
        Button commentReply;
        Button commentViewReplies;

        CommentViewHolder(View itemView) {
            super(itemView);

            commentPic = (ImageView) itemView.findViewById(R.id.comment_pic);
            commentName = (TextView) itemView.findViewById(R.id.comment_name);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);
            commentDate = (TextView) itemView.findViewById(R.id.comment_date);
            commentFollow = (ImageView) itemView.findViewById(R.id.button_follow);
            commentReply = (Button) itemView.findViewById(R.id.button_reply);
            commentViewReplies = (Button) itemView.findViewById(R.id.button_view_replies);

        }
    }

    List<Comment> comments;
    Fragment currentFragment;

    public CommentsRVAdapter(List<Comment> comments, Fragment currentFragment) {
        this.comments = comments;
        this.currentFragment = currentFragment;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        CommentViewHolder cvh = new CommentViewHolder(v);

        return cvh;
    }

    // Binding each card with information for each comment in comments list
    @Override
    public void onBindViewHolder(CommentViewHolder commentViewHolder, int i) {

        // Comment for ith card in card list
        final Comment comment = comments.get(i);

        // Comment information
        //TODO (M): Picasso for commentPic
        commentViewHolder.commentName.setText(comment.commentName);
        commentViewHolder.commentDate.setText(comment.commentDate);
        commentViewHolder.commentText.setText(comment.commentText);

        // Set up image for follow button
        // TODO (V): Connect image for following button
        /*if (comment.commentFollow) {
            commentViewHolder.commentFollow.setImageResource(R.drawable.button_following);
        } else {
            commentViewHolder.commentFollow.setImageResource(R.drawable.button_not_following);
        }*/

        // Click listener for follow button
        commentViewHolder.commentFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO (M): POST request to update comment follow boolean passing in !comments.get(i)
                // TODO (M): POST request signing up this user for notifications
                // TODO (V): Connect image for following button
                /*if (comment.commentFollow) {
                    commentViewHolder.commentFollow.setImageResource(R.drawable.button_following);
                } else {
                    commentViewHolder.commentFollow.setImageResource(R.drawable.button_not_following);
                }*/
            }
        });

        commentViewHolder.commentReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateCommentFragment createCommentFragment = new CreateCommentFragment();
                String createCommentFragmentTag = currentFragment.getString(R.string.fragment_create_comment);

                currentFragment.getFragmentManager().beginTransaction()
                        .add(R.id.fragment_frame_layout, createCommentFragment, createCommentFragmentTag)
                        .hide(createCommentFragment)
                        .commit();

                MainActivity mainActivity = (MainActivity) currentFragment.getActivity();
                mainActivity.switchTo(currentFragment, createCommentFragment, createCommentFragmentTag);

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
