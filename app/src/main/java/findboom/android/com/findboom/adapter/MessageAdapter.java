package findboom.android.com.findboom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * Created by Administrator on 2016/11/2.
 */

public class MessageAdapter extends android.widget.BaseAdapter {
    private List<EMMessage> msgs;
    private Context context;
    private LayoutInflater inflater;

    public MessageAdapter(List<EMMessage> msgs, Context context_) {
        this.msgs = msgs;
        this.context = context_;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public EMMessage getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItem(position);
        return message.direct() == EMMessage.Direct.RECEIVE ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        EMMessage message = getItem(position);
        int viewType = getItemViewType(position);
        if (convertView == null) {
            if (viewType == 0) {
                convertView = inflater.inflate(R.layout.item_message_received, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_message_sent, parent, false);
            }
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
            holder.imgPhoto = (CircleImageView) convertView.findViewById(R.id.iv_userhead);
            convertView.setTag(holder);
        }

        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        holder.tv.setText(txtBody.getMessage());
        if (viewType != 0) {
            Glide.with(context).load(BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(context)).Avatar).error(R.mipmap.ic_logo).into(holder.imgPhoto);
        } else {
            try {
                Glide.with(context).load(message.getStringAttribute("avatar")).error(R.mipmap.ic_logo).into(holder.imgPhoto);
            } catch (HyphenateException e) {
                e.printStackTrace();
                Glide.with(context).load(R.mipmap.ic_logo).into(holder.imgPhoto);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView tv;
        CircleImageView imgPhoto;
    }
}
