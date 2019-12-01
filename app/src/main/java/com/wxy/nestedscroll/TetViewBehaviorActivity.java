package com.wxy.nestedscroll;

import android.os.Bundle;
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

public class TetViewBehaviorActivity extends AppCompatActivity {

    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.recycler_name)
    RecyclerView recyclerName;
    private TestAdapter testAdapter;
    private List<String> stringList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_textview_behavior);
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

    @OnClick(R.id.tv_second)
    public void onViewClicked() {
        Toast.makeText(this, "heihei", Toast.LENGTH_SHORT).show();
    }
}
