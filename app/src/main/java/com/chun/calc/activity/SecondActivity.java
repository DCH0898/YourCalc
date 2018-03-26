package com.chun.calc.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chun.calc.BeanTwo;
import com.chun.calc.R;
import com.chun.calc.adapter.MyAdapter1;
import com.chun.calc.adapter.MyAdapter2;
import com.chun.calc.net.Ajax;
import com.chun.calc.net.OnLoadDataListener;
import com.chun.calc.util.FunUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * 第二版
 * Created by Dachun Li on 2018/3/11.
 */

public class SecondActivity extends AppCompatActivity implements View.OnClickListener {

    private final String KEY_NAME = "second";
    private String url = "http://fund.eastmoney.com/Data/FundCompare_Interface.aspx?t=0&bzdm=";
    private final String TAG = "SecondActivity";
    private MyAdapter1 myAdapter1;
    private MyAdapter2 myAdapter2;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private LinearLayoutManager linearLayoutManager1;
    private LinearLayoutManager linearLayoutManager2;
    private TextView firstTabText;
    private TextView secondTabText;
    Gson gson1 = new Gson();
    List<BeanTwo> list1 = new ArrayList<>();
    List<BeanTwo> list2 = new ArrayList<>();
    SharedPreferences sp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        initView();
    }

    private void initView() {
        recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview1);
        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView1.setLayoutManager(linearLayoutManager1);
        myAdapter1 = new MyAdapter1(R.layout.item_second);
        recyclerView1.setAdapter(myAdapter1);

        recyclerView2 = (RecyclerView) findViewById(R.id.recyclerview2);
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView2.setLayoutManager(linearLayoutManager2);
        myAdapter2 = new MyAdapter2(R.layout.item_second2);
        recyclerView2.setAdapter(myAdapter2);

        firstTabText = (TextView) findViewById(R.id.first_tab_text);
        secondTabText = (TextView) findViewById(R.id.second_tab_text);
        firstTabText.setOnClickListener(this);
        secondTabText.setOnClickListener(this);

        sp = getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String json = sp.getString("one", "");
        String json2 = sp.getString("two", "");
        if ("".equals(json)) {
            initListOne();
        } else {
            list1 = gson1.fromJson(json, new TypeToken<ArrayList<BeanTwo>>() {
            }.getType());
        }
        myAdapter1.setNewData(list1);

        if ("".equals(json2)) {
            initListTwo();
        } else {
            list2 = gson1.fromJson(json2, new TypeToken<ArrayList<BeanTwo>>() {
            }.getType());
        }
        myAdapter2.setNewData(list2);
        for (int i = 0; i < 4; i++) {
            recycleViewOneGetNetData(i);
        }
        for (int i = 0; i < 8; i++) {
            recycleViewTwoGetNetData(i);
        }
        select(1);
    }

    private void recycleViewOneGetNetData(final int position) {
        Ajax.getInst().get(url + list1.get(position).getBzdm(),
                new OnLoadDataListener() {
                    @Override
                    public void onDataReceiver(String dataContent) {
                        Log.d(TAG, dataContent);
                        String[] strings = dataContent.split(",");
                        ((BeanTwo) myAdapter1.getData().get(position)).setValuation(Double.parseDouble(strings[4]));
                        myAdapter1.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.d(TAG, errorMessage);
                    }
                });
    }

    private void recycleViewTwoGetNetData(final int position) {
        if (!FunUtil.isEmpty(list2.get(position).getBzdm()) && list2.get(position).getBzdm().length() == 6) {
            Ajax.getInst().get(url + list2.get(position).getBzdm(),
                    new OnLoadDataListener() {
                        @Override
                        public void onDataReceiver(String dataContent) {
                            Log.d(TAG, dataContent);
                            String[] strings = dataContent.split(",");
                            ((BeanTwo) myAdapter2.getData().get(position)).setValuation(Double.parseDouble(strings[4]));
                            myAdapter2.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Log.d(TAG, errorMessage);
                        }
                    });
        }
    }

    private void initListOne() {
        for (int i = 1; i < 5; i++) {
            BeanTwo beanTwo = new BeanTwo();
            if (i == 1) {
                beanTwo.setTitle("上证50");
                beanTwo.setBzdm("001549");
            } else if (i == 2) {
                beanTwo.setTitle("创业板");
                beanTwo.setBzdm("001593");
            } else if (i == 3) {
                beanTwo.setTitle("沪深300");
                beanTwo.setBzdm("000961");
            } else if (i == 4) {
                beanTwo.setTitle("中证500");
                beanTwo.setBzdm("002907");
            }
            beanTwo.setNotice("");
            beanTwo.setProfit(0);
            beanTwo.setQuota(0);
            beanTwo.setTotal(0);
            beanTwo.setShare(0);
            beanTwo.setValuation(0);
            list1.add(beanTwo);
        }
    }

    private void initListTwo() {
        for (int i = 1; i < 9; i++) {
            BeanTwo beanTwo = new BeanTwo();
            beanTwo.setNotice("");
            beanTwo.setProfit(0);
            beanTwo.setQuota(0);
            beanTwo.setTotal(0);
            beanTwo.setShare(0);
            beanTwo.setTitle("");
            beanTwo.setValuation(0);
            list2.add(beanTwo);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    private void select(int p) {
        if (p == 1) {
            saveView(2);
            showView(1);
        } else if (p == 2) {
            saveView(1);
            showView(2);
        }
    }

    private void saveView(int p) {
//        SharedPreferences.Editor editor = sp.edit();
//        if (p == 1) {
//            editor.putString("one", gson1.toJson(myAdapter1.getData()));
//        } else if (p == 2) {
//            editor.putString("two", gson1.toJson(myAdapter1.getData()));
//        }
//        editor.apply();
    }

    private void showView(int p) {
        if (p == 1) {
            firstTabText.setTextColor(getResources().getColor(R.color.blue));
            secondTabText.setTextColor(getResources().getColor(R.color.gray));
            recyclerView1.setVisibility(View.VISIBLE);
            recyclerView2.setVisibility(View.GONE);
        } else if (p == 2) {
            firstTabText.setTextColor(getResources().getColor(R.color.gray));
            secondTabText.setTextColor(getResources().getColor(R.color.blue));
            recyclerView1.setVisibility(View.GONE);
            recyclerView2.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_tab_text:
                select(1);
                break;
            case R.id.second_tab_text:
                select(2);
                break;
            default:

        }
    }

    private void saveData() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("one", gson1.toJson(myAdapter1.getData()));
        editor.putString("two", gson1.toJson(myAdapter2.getData()));
        editor.apply();
    }

    @Override
    public void finish() {
        moveTaskToBack(false);
    }
}
