package findboom.android.com.findboom.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import findboom.android.com.findboom.BaseFragmentActivity;
import findboom.android.com.findboom.R;
import findboom.android.com.findboom.adapter.FragmentWithTitleAdapter;
import findboom.android.com.findboom.application.FindBoomApplication;
import findboom.android.com.findboom.chat.activity.ConversationFragment;
import findboom.android.com.findboom.fragment.InviteFragment;

/**
 * Created by Administrator on 2016/10/28.
 */

public class FriendMessage extends BaseFragmentActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments;
    private List<String> titles;
    private InviteFragment inviteFragment;
    private FragmentWithTitleAdapter fragmentWithTitleAdapter;
    private ConversationFragment conversationFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_message);
        initView();
        initFragment();
    }

    private void initFragment() {
        conversationFragment = new ConversationFragment();
        fragments.add(conversationFragment);
        inviteFragment = new InviteFragment();
        fragments.add(inviteFragment);
        fragmentWithTitleAdapter.notifyDataSetChanged();
    }

    private void initView() {
        titles = new ArrayList<>();
        titles.add("好友消息");
        titles.add("好友申请");
        fragments = new ArrayList<>();
        fragmentWithTitleAdapter = new FragmentWithTitleAdapter(getSupportFragmentManager(), fragments, titles);
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(fragmentWithTitleAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void boomClick(View v) {
        FindBoomApplication.getInstance().playClickSound();
        finish();
    }

}
