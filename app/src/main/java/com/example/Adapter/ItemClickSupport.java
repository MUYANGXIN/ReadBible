package com.example.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shujuku.R;

/**
 * Created by qxx on 2017/5/16.
 */

public class ItemClickSupport {
    //这是从网上找的代码，可以支持RecicleView的点击事件，原理尚不清楚

        private final RecyclerView mRecyclerView;
        private com.example.Adapter.ItemClickSupport.OnItemClickListener mOnItemClickListener;
        private com.example.Adapter.ItemClickSupport.OnItemLongClickListener mOnItemLongClickListener;

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                    mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(), v);
                }
            }
        };

        private View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemLongClickListener != null) {
                    RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                    return mOnItemLongClickListener.onItemLongClicked(mRecyclerView, holder.getAdapterPosition(), v);
                }
                return false;
            }
        };

        private RecyclerView.OnChildAttachStateChangeListener mAttachListener
                = new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (mOnItemClickListener != null) {
                    view.setOnClickListener(mOnClickListener);
                }
                if (mOnItemLongClickListener != null) {
                    view.setOnLongClickListener(mOnLongClickListener);
                }
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {}
        };

        private ItemClickSupport(RecyclerView recyclerView) {
            mRecyclerView = recyclerView;
            mRecyclerView.setTag(R.id.item_click_support, this);
            mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
        }

        public static com.example.Adapter.ItemClickSupport addTo(RecyclerView view) {
            com.example.Adapter.ItemClickSupport support = (com.example.Adapter.ItemClickSupport) view.getTag(R.id.item_click_support);
            if (support == null) {
                support = new com.example.Adapter.ItemClickSupport(view);
            }
            return support;
        }

        public static com.example.Adapter.ItemClickSupport removeFrom(RecyclerView view) {
            com.example.Adapter.ItemClickSupport support = (com.example.Adapter.ItemClickSupport) view.getTag(R.id.item_click_support);
            if (support != null) {
                support.detach(view);
            }
            return support;
        }

        public com.example.Adapter.ItemClickSupport setOnItemClickListener(com.example.Adapter.ItemClickSupport.OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        public com.example.Adapter.ItemClickSupport setOnItemLongClickListener(com.example.Adapter.ItemClickSupport.OnItemLongClickListener listener) {
            mOnItemLongClickListener = listener;
            return this;
        }

        private void detach(RecyclerView view) {
            view.removeOnChildAttachStateChangeListener(mAttachListener);
            view.setTag(R.id.item_click_support, null);
        }

        public interface OnItemClickListener {
            void onItemClicked(RecyclerView recyclerView, int position, View v);
        }

        public interface OnItemLongClickListener {
            boolean onItemLongClicked(RecyclerView recyclerView, int position, View v);
        }

}
