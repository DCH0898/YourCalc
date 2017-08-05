package com.chun.calc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class ScrollingActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] datas;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ScrollingActivity.this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        datas = getDatas();
        adapter = new MyAdapter(ScrollingActivity.this, datas);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private String[] getDatas() {
        String[] ss = {};
        SharedPreferences sp = getSharedPreferences("name_", Context.MODE_PRIVATE);
        String before = sp.getString("name000", "");
        ss = before.split("\\#");
        return ss;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("name", "calc");
        setResult(0, intent);
        finish();
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context mContext;
        private String[] datas;//数据

        public MyAdapter(Context context, String[] datas) {
            mContext = context;
            this.datas = datas;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_textview, parent,
                    false);//这个布局就是一个imageview用来显示图片
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            final String ss = datas[position];
            myViewHolder.textView.setText(datas[position]);
            myViewHolder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("name", ss);
                    setResult(0, intent);
                    finish();
                }
            });
        }

        @Override
        public int getItemCount() {
            return datas.length;//获取数据的个数
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public MyViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.textview);
        }
    }
}

