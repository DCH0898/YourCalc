package com.chun.calc.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chun.calc.BeanTwo;
import com.chun.calc.R;
import com.chun.calc.adapter.MyAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 第二版
 * Created by Dachun Li on 2018/3/11.
 */

public class SecondActivity extends AppCompatActivity {

    private final String KEY_NAME = "second";
    private MyAdapter myAdapter;
    private RecyclerView recyclerView;
    private TextView firstTabText;
    private TextView secondTabText;
    Gson gson1 = new Gson();
    List<BeanTwo> list1 = new ArrayList<>();
    List<BeanTwo> list2 = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter(R.layout.item_second);
        firstTabText = (TextView) findViewById(R.id.first_tab_text);
        secondTabText = (TextView) findViewById(R.id.second_tab_text);

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
        String json = sp.getString("one", "");
        String json2 = sp.getString("two", "");
        if ("".equals(json)) {
            for (int i = 1; i < 5; i++) {
                BeanTwo beanTwo = new BeanTwo();
                if (i == 1) {
                    beanTwo.setTitle("上证50");
                } else if (i == 2) {
                    beanTwo.setTitle("创业板");
                } else if (i == 3) {
                    beanTwo.setTitle("沪深300");
                } else if (i == 4) {
                    beanTwo.setTitle("中证500");
                }
                beanTwo.setNotice("");
                beanTwo.setProfit(0);
                beanTwo.setQuota(0);
                beanTwo.setShare(0);
                beanTwo.setValuation(0);
                list1.add(beanTwo);
            }
        } else {
            list1 = gson1.fromJson(json, new TypeToken<ArrayList<BeanTwo>>() {
            }.getType());

        }

        if ("".equals(json2)) {
            for (int i = 1; i < 4; i++) {
                BeanTwo beanTwo = new BeanTwo();
                beanTwo.setNotice("");
                beanTwo.setProfit(0);
                beanTwo.setQuota(0);
                beanTwo.setShare(0);
                beanTwo.setTitle("");
                beanTwo.setValuation(0);
                list2.add(beanTwo);
            }
        } else {
            list2 = gson1.fromJson(json2, new TypeToken<ArrayList<BeanTwo>>() {
            }.getType());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void select(int p) {
        if (p == 1) {
            saveView(1);
            showView(2);
        } else if (p == 2) {
            saveView(2);
            showView(1);
        }
    }

    private void saveView(int p) {
        SharedPreferences sp = getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (p == 1) {
            editor.putString("one", gson1.toJson(myAdapter.getData()));
        } else if (p == 2) {
            editor.putString("two", gson1.toJson(myAdapter.getData()));
        }
        editor.apply();
    }

    private void showView(int p) {
        if (p == 1) {
            firstTabText.setTextColor(getResources().getColor(R.color.blue));
            secondTabText.setTextColor(getResources().getColor(R.color.gray));
            myAdapter.setNewData(list1);
            recyclerView.scrollToPosition(0);
        } else if (p == 2) {
            firstTabText.setTextColor(getResources().getColor(R.color.gray));
            secondTabText.setTextColor(getResources().getColor(R.color.blue));
            myAdapter.setNewData(list2);
            recyclerView.scrollToPosition(0);
        }
    }
}
