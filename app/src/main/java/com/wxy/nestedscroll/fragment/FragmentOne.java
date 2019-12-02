package com.wxy.nestedscroll.fragment;

import com.wxy.nestedscroll.R;
import com.wxy.nestedscroll.adapter.TestAdapter;
import com.wxy.nestedscroll.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class FragmentOne extends BaseFragment {
    @BindView(R.id.recycler_name)
    RecyclerView recyclerName;
    private TestAdapter testAdapter;
    private List<String> stringList;
    @Override
    public int getLayoutId() {
        return R.layout.fragment_one;
    }
    @Override
    public void initView() {
                stringList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            stringList.add("第 " + (i + 1) + " 名");
        }
        testAdapter = new TestAdapter(R.layout.recycler_name, stringList);
        recyclerName.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerName.setAdapter(testAdapter);
    }

}
