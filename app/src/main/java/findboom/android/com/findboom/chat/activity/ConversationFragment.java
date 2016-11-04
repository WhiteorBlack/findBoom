package findboom.android.com.findboom.chat.activity;/**
 * Created by Administrator on 2016/10/30.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.widget.CircleImageView;

/**
 * author:${白曌勇} on 2016/10/30
 * TODO:
 */
public class ConversationFragment extends Fragment {
    private View view;
    private ListView listView;
    private List<EMConversation> conversationList = new ArrayList<EMConversation>();
    private EaseConversationAdapater adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.activity_conversation, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    private void initView() {
        conversationList.addAll(loadConversationList());
        listView = (ListView) view.findViewById(R.id.listView);
        adapter = new EaseConversationAdapater(getContext(), 1, conversationList);
        listView.setAdapter(adapter);
        final String st2 = getResources().getString(R.string.Cant_chat_with_yourself);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = adapter.getItem(position);
                String userId = conversation.getUserName();
                String userName = "玩家";
                try {
                    userName = conversation.getLastMessage().getStringAttribute("name");
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                if (userId.equals(FindBoomApplication.getInstance().getCurrentUserName()))
                    Toast.makeText(getContext(), st2, Toast.LENGTH_SHORT).show();
                else {
                    // 进入聊天页面
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra("username", userName);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    /**
     * 获取会话列表
     *
     * @return +
     */
    protected List<EMConversation> loadConversationList() {
        // 获取所有会话，包括陌生人
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(
                            new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));

                }
            }
        }
        try {
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }
        return list;
    }

    /**
     * 根据最后一条消息的时间排序
     */
    private void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first == con2.first) {
                    return 0;
                } else if (con2.first > con1.first) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }

    public class EaseConversationAdapater extends ArrayAdapter<EMConversation> {
        private List<EMConversation> conversationList;
        private List<EMConversation> copyConversationList;

        private boolean notiyfyByFilter;

        protected int primaryColor;
        protected int secondaryColor;
        protected int timeColor;
        protected int primarySize;
        protected int secondarySize;
        protected float timeSize;

        public EaseConversationAdapater(Context context, int resource, List<EMConversation> objects) {
            super(context, resource, objects);
            conversationList = objects;
            copyConversationList = new ArrayList<EMConversation>();
            copyConversationList.addAll(objects);
        }

        @Override
        public int getCount() {
            return conversationList.size();
        }

        @Override
        public EMConversation getItem(int arg0) {
            if (arg0 < conversationList.size()) {
                return conversationList.get(arg0);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_conversation, parent, false);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            if (holder == null) {
                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.name);
                holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
                holder.message = (TextView) convertView.findViewById(R.id.message);
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.msgState = convertView.findViewById(R.id.msg_state);
                holder.imgPhoto = (CircleImageView) convertView.findViewById(R.id.avatar);
                convertView.setTag(holder);
            }
            // 获取与此用户/群组的会话
            EMConversation conversation = getItem(position);
            // 获取用户username或者群组groupid
            String username = conversation.getUserName();
            EMMessage msg = conversation.getLastMessage();
            try {
                holder.name.setText(msg.getStringAttribute("name"));
            } catch (HyphenateException e) {
                e.printStackTrace();
                holder.name.setText("玩儿家");
            }
            if (conversation.getUnreadMsgCount() > 0) {
                // 显示与此用户的消息未读数
                holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
                holder.unreadLabel.setVisibility(View.VISIBLE);
            } else {
                holder.unreadLabel.setVisibility(View.INVISIBLE);
            }
            if (conversation.getAllMsgCount() != 0) {
                // 把最后一条消息的内容作为item的message内容
                EMMessage lastMessage = conversation.getLastMessage();
                String avatar="";
                try {
                    avatar=lastMessage.getStringAttribute("avatar");

                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                Glide.with(getContext()).load(avatar).error(R.mipmap.ic_logo).into(holder.imgPhoto);
                String message=lastMessage.getBody().toString();
                if (!TextUtils.isEmpty(message)){
                    message=message.substring(message.indexOf(":")+2,message.length()-1);
                }
                holder.message.setText(message);
                holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
                if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                    holder.msgState.setVisibility(View.VISIBLE);
                } else {
                    holder.msgState.setVisibility(View.GONE);
                }
            }

            return convertView;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
            if (!notiyfyByFilter) {
                copyConversationList.clear();
                copyConversationList.addAll(conversationList);
                notiyfyByFilter = false;
            }
        }

        public void setPrimaryColor(int primaryColor) {
            this.primaryColor = primaryColor;
        }

        public void setSecondaryColor(int secondaryColor) {
            this.secondaryColor = secondaryColor;
        }

        public void setTimeColor(int timeColor) {
            this.timeColor = timeColor;
        }

        public void setPrimarySize(int primarySize) {
            this.primarySize = primarySize;
        }

        public void setSecondarySize(int secondarySize) {
            this.secondarySize = secondarySize;
        }

        public void setTimeSize(float timeSize) {
            this.timeSize = timeSize;
        }

    }

    private static class ViewHolder {
        /**
         * 和谁的聊天记录
         */
        TextView name;
        /**
         * 消息未读数
         */
        TextView unreadLabel;
        /**
         * 最后一条消息的内容
         */
        TextView message;
        /**
         * 最后一条消息的时间
         */
        TextView time;
        /**
         * 最后一条消息的发送状态
         */
        View msgState;
        /**
         * 整个list中每一行总布局
         */
        CircleImageView imgPhoto;

    }
}
