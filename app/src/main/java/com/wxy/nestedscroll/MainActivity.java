package com.wxy.nestedscroll;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.wxy.nestedscroll.adapter.TestAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_name)
    RecyclerView recyclerName;
    private TestAdapter testAdapter;
    private List<String> stringList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRecycler();
        recyclerName.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });
    }

    private void initRecycler() {
        stringList=new ArrayList<>();
        for (int i=0;i<40;i++){
            stringList.add("第 "+(i+1)+" 名");
        }
        testAdapter=new TestAdapter(R.layout.recycler_name,stringList);
        recyclerName.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerName.setAdapter(testAdapter);
    }
}
