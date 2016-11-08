package findboom.android.com.findboom.chat.activity;

import java.util.Date;
import java.util.List;

import com.bumptech.glide.Glide;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import findboom.android.com.findboom.R;
import findboom.android.com.findboom.bean.Bean_UserInfo;
import findboom.android.com.findboom.chat.Constant;
import findboom.android.com.findboom.chat.utils.EaseCommonUtils;
import findboom.android.com.findboom.dailog.BasePopupwind;
import findboom.android.com.findboom.database.BoomDBManager;
import findboom.android.com.findboom.utils.AppPrefrence;
import findboom.android.com.findboom.widget.CircleImageView;

public class ChatActivity extends Activity {
    private ListView listView;
    private int chatType = 1;
    private String toChatUsername;
    private String toChatUserId;
    private Button btn_send;
    private EditText et_content;
    private List<EMMessage> msgList;
    MessageAdapter adapter;
    private EMConversation conversation;
    protected int pagesize = 20;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_chat);
        toChatUserId = this.getIntent().getStringExtra("userId");
        toChatUsername = this.getIntent().getStringExtra("username");
        Constant.CHAT_USER = toChatUserId;
        TextView tv_toUsername = (TextView) this.findViewById(R.id.tv_toUsername);
        tv_toUsername.setText(toChatUsername);
        listView = (ListView) this.findViewById(R.id.listView);
        btn_send = (Button) this.findViewById(R.id.btn_send);
        et_content = (EditText) this.findViewById(R.id.et_content);
        findViewById(R.id.img_close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getAllMessage();
        msgList = conversation.getAllMessages();
        adapter = new MessageAdapter(msgList, ChatActivity.this);
        listView.setAdapter(adapter);
        listView.setSelection(listView.getCount() - 1);
        btn_send.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    return;
                }
                setMesaage(content);
            }

        });
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
        Constant.isRuning = true;
    }

    protected void getAllMessage() {
        // 获取当前conversation对象

        conversation = EMClient.getInstance().chatManager().getConversation(toChatUserId,
                EaseCommonUtils.getConversationType(chatType), true);
        // 把此会话的未读数置为0
        conversation.markAllMessagesAsRead();
        // 初始化db时，每个conversation加载数目是getChatOptions().getNumberOfMessagesLoaded
        // 这个数目如果比用户期望进入会话界面时显示的个数不一样，就多加载一些
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }

    }

    private void setMesaage(String content) {

        // 创建一条文本消息，content为消息文字内容，toChatUserId为对方用户或者群聊的id，后文皆是如此
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUserId);
        // 如果是群聊，设置chattype，默认是单聊
        if (chatType == Constant.CHATTYPE_GROUP)
            message.setChatType(ChatType.GroupChat);
        Bean_UserInfo.GameUser user = BoomDBManager.getInstance().getUserData(AppPrefrence.getUserName(ChatActivity.this));
        message.setAttribute("avatar", user.Avatar);

        message.setAttribute("name", TextUtils.isEmpty(user.NickName) ? user.GameUserId : user.NickName);
        // 发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
        msgList.add(message);

        adapter.notifyDataSetChanged();
        if (msgList.size() > 0) {
            listView.setSelection(listView.getCount() - 1);
        }
        et_content.setText("");
        et_content.clearFocus();
    }

    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }
                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(toChatUserId)) {
                    msgList.addAll(messages);
                    adapter.notifyDataSetChanged();
                    if (msgList.size() > 0) {
                        et_content.setSelection(listView.getCount() - 1);

                    }

                }
            }

            // 收到消息
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            // 收到透传消息
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            // 收到已读回执
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            // 收到已送达回执
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            // 消息状态变动
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        Constant.CHAT_USER = "";
        Constant.isRuning = false;
    }

    @SuppressLint("InflateParams")
    class MessageAdapter extends BaseAdapter {
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

        @SuppressLint("InflateParams")
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
                holder.txtTime = (TextView) convertView.findViewById(R.id.timestamp);
                convertView.setTag(holder);
            }

            EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
            holder.tv.setText(txtBody.getMessage());
            if (holder.txtTime != null) {
                if (position == 0) {
                    holder.txtTime.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    holder.txtTime.setVisibility(View.VISIBLE);
                } else {
                    // 两条消息时间离得如果稍长，显示时间
                    EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1);
                    if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                        holder.txtTime.setVisibility(View.GONE);
                    } else {
                        holder.txtTime.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                        holder.txtTime.setVisibility(View.VISIBLE);
                    }
                }
            }
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

    }

    public static class ViewHolder {
        TextView tv;
        CircleImageView imgPhoto;
        TextView txtTime;
    }


}
