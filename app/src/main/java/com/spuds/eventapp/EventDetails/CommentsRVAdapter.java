package com.spuds.eventapp.EventDetails;

import android.graphics.Point;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spuds.eventapp.CreateComment.CreateCommentFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.Comment;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tina on 5/4/16.
 */

public class CommentsRVAdapter extends RecyclerView.Adapter<CommentsRVAdapter.CommentViewHolder> {

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView commentPic;
        TextView commentName;
        TextView commentDate;
        TextView commentText;
        Button commentFollow;
        Button commentReply;
        Button commentViewReplies;
        RecyclerView recyclerViewReplies;

        // Checked if View Replies button has been pressed or not
        boolean viewReplies = false;
        // Reference to view
        View itemView;

        CommentViewHolder(View itemView) {
            super(itemView);

            // Make view objects for each comment component
            cardView = (CardView) itemView.findViewById(R.id.cv);
            commentPic = (ImageView) itemView.findViewById(R.id.comment_pic);
            commentName = (TextView) itemView.findViewById(R.id.comment_name);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);
            commentDate = (TextView) itemView.findViewById(R.id.comment_date);
            commentFollow = (Button) itemView.findViewById(R.id.button_follow);
            commentReply = (Button) itemView.findViewById(R.id.button_reply);
            commentViewReplies = (Button) itemView.findViewById(R.id.button_view_replies);
            recyclerViewReplies = (RecyclerView) itemView.findViewById(R.id.rv);
            this.itemView = itemView;

        }
    }

    // Contains all comments
    List<Comment> comments;
    // Reference to calling fragment
    Fragment currentFragment;


    public CommentsRVAdapter(List<Comment> comments, Fragment currentFragment) {
        this.comments = comments;
        this.currentFragment = currentFragment;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment, viewGroup, false);
        CommentViewHolder commentViewHolder = new CommentViewHolder(v);

        return commentViewHolder;
    }

    // Binding each card with information for each comment in comments list
    @Override
    public void onBindViewHolder(final CommentViewHolder commentViewHolder, int i) {

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

                ((MainActivity) currentFragment.getActivity()).removeSearchToolbar();
                currentFragment.getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, createCommentFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(currentFragment.getString(R.string.fragment_create_comment))
                        .commit();

            }
        });

        commentViewHolder.commentViewReplies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!commentViewHolder.viewReplies) {

                    LinearLayoutManager llm = new LinearLayoutManager(commentViewHolder.itemView.getContext());
                    commentViewHolder.recyclerViewReplies.setLayoutManager(llm);
                    commentViewHolder.recyclerViewReplies.setHasFixedSize(true);

                    // TODO (M): POST request to get replies to this comment
                    List<Comment> comments = new ArrayList<>();
                    comments.add(new Comment("1", "2", "Christine Wu", "tina.jpg", "05.06.16", "This event is pretty bad", false));


                    CommentsRVAdapter adapter = new CommentsRVAdapter(comments, currentFragment);
                    commentViewHolder.recyclerViewReplies.setAdapter(adapter);

                    commentViewHolder.viewReplies = true;

                } else {

                    commentViewHolder.recyclerViewReplies.setAdapter(null);

                    commentViewHolder.viewReplies = false;

                }
            }
        });

        if(comment.commentParentId != null && comment.commentParentId != "") {

            Display display = currentFragment.getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)commentViewHolder.recyclerViewReplies.getLayoutParams();
            params.width = (int) (size.x * .85);
            params.leftMargin = (int) (size.x*.13);

            commentViewHolder.cardView.setLayoutParams(params);

            // Remove the buttons on reply comments
            ((ViewManager)commentViewHolder.commentFollow.getParent()).removeView(commentViewHolder.commentFollow);
            ((ViewManager)commentViewHolder.commentReply.getParent()).removeView(commentViewHolder.commentReply);
            ((ViewManager)commentViewHolder.commentViewReplies.getParent()).removeView(commentViewHolder.commentViewReplies);

        }

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
