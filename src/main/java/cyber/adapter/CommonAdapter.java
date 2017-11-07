package cyber.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by MS on 2017/11/7.
 */
public abstract  class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder> {
    private boolean isLoadingData = false;
    private boolean isFinishLoadData = false;


    private List<T> mDatas;
    private Context mContext;
    private int mItem_Layout;
    protected LayoutInflater mInflater;

    private  int ITEM_TYPE = 100000;
    private  int LOAD_TYPE = 100001;

    private  int EMPTY_TYPE= 100002;

    private CommonViewHolder mLoadMoreViewHolder;

    public CommonAdapter(Context c, int item_Layout, List<T> d) {
        mDatas = d;
        mContext = c;
        mItem_Layout = item_Layout;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {

        if(getItemCount() == 1){return EMPTY_TYPE;}

        if (position < mDatas.size()) {
            return ITEM_TYPE;
        }
        return LOAD_TYPE;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CommonViewHolder holder;
        if(viewType == ITEM_TYPE){
             holder = new CommonViewHolder(mInflater.inflate(mItem_Layout, parent,false));
        }
        else if(viewType == EMPTY_TYPE){
            holder = new CommonViewHolder(mInflater.inflate(R.layout.empty, parent,false));
        }
        else {
            mLoadMoreViewHolder = holder = new CommonViewHolder(mInflater.inflate(R.layout.loadmore, parent,false));
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        if(ITEM_TYPE == getItemViewType(position)){
            convert(holder,mDatas.get(position),position);
        }
    }

    protected abstract void convert(CommonViewHolder holder, T t,int p);
    protected abstract void onLoadMore();


    @Override
    public int getItemCount() {
        if(null == mDatas ){return 0;}
        return mDatas.size() + 1;
    }


    /***
     *  无数据可加载
     */
    public  void NoMoreLoad(){

        isFinishLoadData = true;
        View view = mLoadMoreViewHolder.getView(R.id.progressBar);
        view.setVisibility(View.GONE);

        view = mLoadMoreViewHolder.getView(R.id.textId);
        view.setVisibility(View.VISIBLE);
    }

    /**
     *   添加上拉加载更多
     * @param r
     */
    public void setloadMore(final RecyclerView r) {
        r.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int state) {
                super.onScrollStateChanged(recyclerView, state);

                if (state == RecyclerView.SCROLL_STATE_IDLE  && !isLoadingData && !isFinishLoadData){
                    RecyclerView.LayoutManager layoutManager = r.getLayoutManager();
                    int lastVisibleItemPosition;
                    if (layoutManager instanceof GridLayoutManager) {
                        lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = findMax(into);
                    }
                    else {
                        lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }
                    if (layoutManager.getChildCount() > 0
                            && lastVisibleItemPosition >= layoutManager.getItemCount() - 1
                            && layoutManager.getItemCount() > layoutManager.getChildCount()
                            ) {
                        isLoadingData = true;
                        onLoadMore();
                        isLoadingData = false;
                    }
                }
            }
        });
      }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }



    }