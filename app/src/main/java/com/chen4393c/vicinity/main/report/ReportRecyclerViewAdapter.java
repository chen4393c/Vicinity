package com.chen4393c.vicinity.main.report;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chen4393c.vicinity.R;
import com.chen4393c.vicinity.model.Item;

import java.util.List;

public class ReportRecyclerViewAdapter
        extends RecyclerView.Adapter<ReportRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Item> mItems;
    private LayoutInflater mInflater;

    public ReportRecyclerViewAdapter(Context context, List<Item> items) {
        mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mItems = items;
    }

    /**
     * Step 2: create holder prepare listview to show
     * @param parent the listview
     * @param viewType view type
     * @return created view holder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Step 3: render view holder on screen
     * @param holder view holder created by onCreateViewHolder
     * @param position corresponding position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final Item item = mItems.get(position);
        holder.bind(item);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Step1 : declare the view holder structure
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        Item mItem;
        TextView mTextView;
        ImageView mImageView;
        View view;

        ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            mTextView = (TextView) itemView.findViewById(R.id.info_text);
            mImageView = (ImageView) itemView.findViewById(R.id.info_image);
        }

        private void bind(Item item) {
            mItem = item;
            mTextView.setText(mItem.getDrawableLabel());
            mImageView.setImageResource(mItem.getDrawableId());
        }
    }
}
