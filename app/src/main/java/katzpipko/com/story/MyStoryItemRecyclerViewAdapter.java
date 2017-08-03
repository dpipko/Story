package katzpipko.com.story;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import katzpipko.com.story.Model.Story;
import katzpipko.com.story.StoryFragment.OnListFragmentInteractionListener;

public class MyStoryItemRecyclerViewAdapter extends RecyclerView.Adapter<MyStoryItemRecyclerViewAdapter.ViewHolder> {

    private  View _v;
    private final List<Story> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyStoryItemRecyclerViewAdapter(List<Story> items, OnListFragmentInteractionListener listener) {
         mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_storyitem, parent, false);
        _v=view;
        return new ViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.storyTitle.setText(holder.mItem.title);
        Picasso.with(_v.getContext()).load(holder.mItem.imageURL).into(holder.storyImage);

      //  holder.mIdView.setText(mValues.get(position).id);
      //  holder.mContentView.setText(mValues.get(position).content);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public  final ImageView storyImage;
        public  final TextView storyTitle;


        public Story mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            storyImage = (ImageView) view.findViewById(R.id.storyItemImage);
            storyTitle = (TextView) view.findViewById(R.id.storyItemTitle);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
