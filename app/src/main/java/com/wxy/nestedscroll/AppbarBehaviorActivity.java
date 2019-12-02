package com.wxy.nestedscroll;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wxy.nestedscroll.adapter.MainPagerAdapter;
import com.wxy.nestedscroll.fragment.FragmentOne;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AppbarBehaviorActivity extends AppCompatActivity {

    @BindView(R.id.tv_fragment_one)
    TextView tvFragmentOne;
    @BindView(R.id.tv_fragment_two)
    TextView tvFragmentTwo;
    @BindView(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appbar_behavior);
        ButterKnife.bind(this);
        initViewPager();

    }

    private void initViewPager() {
        List<Fragment> list=new ArrayList<>();
        list.add(new FragmentOne());
        list.add(new FragmentOne());
        list.add(new FragmentOne());
        list.add(new FragmentOne());
        viewpager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(),list));
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
             switch (position){
                 case 0:
                     tvFragmentOne.setTextColor(getResources().getColor(R.color.colorAccent));
                     tvFragmentTwo.setTextColor(getResources().getColor(R.color.black));
                     break;
                 case 1:
                     tvFragmentOne.setTextColor(getResources().getColor(R.color.black));
                     tvFragmentTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                     break;
             }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.tv_fragment_one, R.id.tv_fragment_two})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_fragment_one:
                viewpager.setCurrentItem(0);
                tvFragmentOne.setTextColor(getResources().getColor(R.color.colorAccent));
                tvFragmentTwo.setTextColor(getResources().getColor(R.color.black));
                break;
            case R.id.tv_fragment_two:
                viewpager.setCurrentItem(1);
                tvFragmentOne.setTextColor(getResources().getColor(R.color.black));
                tvFragmentTwo.setTextColor(getResources().getColor(R.color.colorAccent));
                break;
        }
    }
}
