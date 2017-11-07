package cyber.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * Created by MS on 2017/11/7.
 */
public class CommonViewHolder extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    private  View mItemView;

    public CommonViewHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray<View>();
        mItemView = itemView;
    }

    public void setText(int id_num, String s) {
        TextView tv = getView(id_num);
        tv.setText(s);
    }

    public CommonViewHolder setOnClickListener(int viewId,View.OnClickListener listener)
    {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mItemView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }
}
