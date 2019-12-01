package com.wxy.nestedscroll;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wxy.nestedscroll.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NestedScrollActivity extends AppCompatActivity {

    @BindView(R.id.iv_second)
    ImageView ivSecond;
    @BindView(R.id.recycler_name)
    RecyclerView recyclerName;
    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.tv_two)
    TextView tvTwo;
    @BindView(R.id.tv_three)
    TextView tvThree;
    @BindView(R.id.tv_four)
    TextView tvFour;
    private TestAdapter testAdapter;
    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nestedscroll);
        ButterKnife.bind(this);
        initRecycler();
    }

    private void initRecycler() {
        stringList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            stringList.add("第 " + (i + 1) + " 名");
        }
        testAdapter = new TestAdapter(R.layout.recycler_name, stringList);
        recyclerName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerName.setAdapter(testAdapter);
    }


    @OnClick({R.id.iv_second,R.id.tv_one, R.id.tv_two, R.id.tv_three, R.id.tv_four})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_second:
                Toast.makeText(this, "iv_second", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_one:
                Toast.makeText(this, "tv_one", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_two:
                Toast.makeText(this, "tv_two", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_three:
                Toast.makeText(this, "tv_three", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_four:
                Toast.makeText(this, "tv_four", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
