package findboom.android.com.findboom.adapter;/**
 * Created by Administrator on 2016/9/17.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.interfacer.OnClickInterface;
import findboom.android.com.findboom.widget.CircleImageView;
import findboom.android.com.findboom.widget.StrokeTextView;

/**
 * author:${白曌勇} on 2016/9/17
 * TODO:
 */
public abstract class BaseRecyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public List dataList;
    public Context context;
    private OnItemClickListener onItemClickListener;
    private OnClickInterface onClickInterface;

    public void setOnclick(OnClickInterface l) {
        this.onClickInterface = l;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        this.onItemClickListener = l;
    }

    public BaseRecyAdapter(Context context, List dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    public BaseRecyAdapter(List dataList) {
        this.dataList = dataList;
    }

    public BaseRecyAdapter(String[] typeList) {

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false));
    }

    public abstract int getLayout();

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClickListener(v, holder.getLayoutPosition());
                }
            });
        }
        setAnimation(holder.itemView, holder.getLayoutPosition());
    }

    private int lastPosition = -1;

    protected void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public interface OnItemClickListener {
        void onItemClickListener(View view, int position);

        void onItemLongClickListener(View view, int position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public void setText(int id, String text) {
            ((TextView) itemView.findViewById(id)).setText(text);
        }

        public View getView(int id) {
            return itemView.findViewById(id);
        }

        public void setStrokeText(int id, String text) {
            ((StrokeTextView) itemView.findViewById(id)).setText(text);
        }

        public void setImage(int id, String url) {
            Glide.with(itemView.getContext()).load(url).into((ImageView) itemView.findViewById(id));
        }

        public void setImage(int id, int url) {
            Glide.with(itemView.getContext()).load(url).into((ImageView) itemView.findViewById(id));
        }

        public void setRadiusImage(int id, String url) {
            Glide.with(itemView.getContext()).load(url).error(R.mipmap.ic_logo).into((CircleImageView) itemView.findViewById(id));
        }

        public void setOnClick(int id, final int position) {
            itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickInterface != null)
                        onClickInterface.onClick(v, position);
                }
            });
        }

        public void setOnClick(int id, final int position, int type) {
            itemView.findViewById(id).setTag(type);
            itemView.findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickInterface != null)
                        onClickInterface.onClick(v, position);
                }
            });
        }
    }
}
