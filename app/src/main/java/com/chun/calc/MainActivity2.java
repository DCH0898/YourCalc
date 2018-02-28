package com.chun.calc;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity2 extends AppCompatActivity implements View.OnClickListener {

    private EditText costE;// 成本
    private EditText lowPositionE;// 底仓
    private EditText totalE;// 总额
    private EditText netValueE;// 净值
    private EditText shareE;// 份额
    private EditText valueE;// 市值
    private EditText increaseE;// 涨幅
    private EditText profitE;// 盈亏额
    private EditText buy_shareE;// 买卖份额
    private EditText TNetValueE;// 当晚净值
    private EditText TShareE;// 当晚份额
    private EditText TValueE;// 当晚市值
    private EditText name;
    private EditText editText;
    private TableLayout table;

    private AppCompatButton ac1;
    private AppCompatButton ac2;
    private AppCompatButton ac3;
    private AppCompatButton button_save;
    String kong = "0";
    String KEY_NAME = "calc";

    private Bean bean;

    DecimalFormat df = new DecimalFormat("######0.00");
    DecimalFormat df2 = new DecimalFormat("######0.000000");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main2);

        costE = (EditText) findViewById(R.id.cost);
        lowPositionE = (EditText) findViewById(R.id.low_position);
        totalE = (EditText) findViewById(R.id.total);
        netValueE = (EditText) findViewById(R.id.net_value);
        shareE = (EditText) findViewById(R.id.share);
        valueE = (EditText) findViewById(R.id.value);
        increaseE = (EditText) findViewById(R.id.increase);
        profitE = (EditText) findViewById(R.id.profit);
        buy_shareE = (EditText) findViewById(R.id.buy_share);
        TNetValueE = (EditText) findViewById(R.id.T_net_value);
        TShareE = (EditText) findViewById(R.id.T_share);
        TValueE = (EditText) findViewById(R.id.T_value);


        name = (EditText) findViewById(R.id.name);
        table = (TableLayout) findViewById(R.id.table);
        ac1 = (AppCompatButton) findViewById(R.id.ac1);
        ac2 = (AppCompatButton) findViewById(R.id.ac2);
        ac3 = (AppCompatButton) findViewById(R.id.ac3);
        button_save = (AppCompatButton) findViewById(R.id.button_save);

        costE.addTextChangedListener(costWatcher);
        lowPositionE.addTextChangedListener(lowPositionWatcher);
        totalE.addTextChangedListener(totalWatcher);
        netValueE.addTextChangedListener(netValueWatcher);
        shareE.addTextChangedListener(shareWatcher);
        valueE.addTextChangedListener(valueWatcher);

        ac1.setOnClickListener(this);
        ac2.setOnClickListener(this);
        ac3.setOnClickListener(this);
        button_save.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    TextWatcher costWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double cost = Double.parseDouble(s.toString());
            if (increaseE.getText().length() > 0) {
                double increase = Double.parseDouble(increaseE.getText().toString());
                netValueE.setText(df2.format(Calcu.getCost(cost, increase)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher lowPositionWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double lowPosition = Double.parseDouble(s.toString());
            if (totalE.getText().length() > 0) {
                double total = Double.parseDouble(totalE.getText().toString());
                costE.setText(df2.format(Calcu.getCost(total, lowPosition)));
            }
            shareE.setText(lowPositionE.getText());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher totalWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double total = Double.parseDouble(s.toString());
            if (valueE.getText().length() > 0) {
                double value = Double.parseDouble(valueE.getText().toString());
                profitE.setText(df.format(Calcu.getProfit(total, value)));
            }
            if (lowPositionE.getText().length() > 0) {
                double lowPosition = Double.parseDouble(lowPositionE.getText().toString());
                costE.setText(df2.format(Calcu.getCost(total, lowPosition)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher netValueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double netValue = Double.parseDouble(s.toString());
            if (shareE.getText().length() > 0) {
                double share = Double.parseDouble(shareE.getText().toString());
                valueE.setText(df.format(Calcu.getValue(netValue, share)));
            }
            if (profitE.getText().length() > 0) {
                double profit = Double.parseDouble(profitE.getText().toString());
                costE.setText(df2.format(Calcu.getBayShare(profit, netValue)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher shareWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double share = Double.parseDouble(s.toString());
            if (netValueE.getText().length() > 0) {
                double netValue = Double.parseDouble(netValueE.getText().toString());
                valueE.setText(df.format(Calcu.getValue(netValue, share)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher valueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() <= 0) {
                return;
            }
            double value = Double.parseDouble(s.toString());
            if (totalE.getText().length() > 0) {
                double total = Double.parseDouble(totalE.getText().toString());
                valueE.setText(df.format(Calcu.getProfit(total, value)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    /**
     * 离开界面的时候保存数据
     */
    @Override
    protected void onPause() {
        super.onPause();
        saveRecode("calc");
    }

    /**
     * 进来时读取数据
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences(KEY_NAME, Context.MODE_PRIVATE);
        costE.setText(sp.getString("cost", "0"));
        lowPositionE.setText(sp.getString("low_position", "0"));
        totalE.setText(sp.getString("total", "0"));
        netValueE.setText(sp.getString("net_value", "0"));
        shareE.setText(sp.getString("share", "0"));
        valueE.setText(sp.getString("value", "0"));
        increaseE.setText(sp.getString("increase", "0"));
        profitE.setText(sp.getString("profit", "0"));
        buy_shareE.setText(sp.getString("buy_share", "0"));
        TNetValueE.setText(sp.getString("T_net_value", "0"));
        TShareE.setText(sp.getString("T_share", "0"));
        TValueE.setText(sp.getString("T_value", "0"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac1:
                costE.setText(kong);
                lowPositionE.setText(kong);
                totalE.setText(kong);
                break;
            case R.id.ac2:
                netValueE.setText(kong);
                shareE.setText(kong);
                valueE.setText(kong);
                break;
            case R.id.ac3:
                increaseE.setText(kong);
                profitE.setText(kong);
                buy_shareE.setText(kong);
                break;
            case R.id.button_save:
                showSaveView();
                break;
            case R.id.backButton:
                startActivityForResult(new Intent(MainActivity2.this, ScrollingActivity.class), 0);
                break;
        }
    }

    /**
     * 保存当前数值到SharedPreferences里面
     */
    public void save() {
        String name_ = name.getText().toString();
        SharedPreferences sp = getSharedPreferences("name_", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        String before = sp.getString("name000", "");
        if ("".equals(before)) {
            editor.putString("name000", name_);
            editor.apply();
            saveRecode(name_);
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            name.setText("");
        } else {
//            String spiteee = "\\#";
//            String[] befores = before.split(spiteee);
//            boolean isExist = false;
//            for (String s : befores) {
//                if (s.equals(name_)) {
//                    isExist = true;
//                }
//            }
//            if (isExist) {
//                Toast.makeText(this, "保存失败，该名字已存在", Toast.LENGTH_LONG).show();
//            } else {
            editor.putString("name000", before + "#" + name_);
            editor.apply();
            saveRecode(name_);
            Toast.makeText(this, "保存成功", Toast.LENGTH_LONG).show();
            name.setText("");
//            }
        }
    }

    /**
     * 弹出一个保存的弹出，输入名字保存
     */
    public void showSaveView() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("确定保存吗？");
        builder.setMessage("保存的名字为：" + name.getText().toString());
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                save();
                dialog.dismiss();
            }
        });
        builder.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        KEY_NAME = data.getStringExtra("name");
    }

    private void saveRecode(String spname) {
        SharedPreferences spp = getSharedPreferences(spname, MODE_PRIVATE);
        SharedPreferences.Editor editorr = spp.edit();
        editorr.putString("cost", costE.getText().toString());
        editorr.putString("low_position", lowPositionE.getText().toString());
        editorr.putString("total", totalE.getText().toString());
        editorr.putString("net_value", netValueE.getText().toString());
        editorr.putString("share", shareE.getText().toString());
        editorr.putString("net_value", netValueE.getText().toString());
        editorr.putString("increase", increaseE.getText().toString());
        editorr.putString("profit", profitE.getText().toString());
        editorr.putString("buy_share", buy_shareE.getText().toString());
        editorr.putString("T_net_value", TNetValueE.getText().toString());
        editorr.putString("T_share", TShareE.getText().toString());
        editorr.putString("T_value", TValueE.getText().toString());
        editorr.apply();
    }

}
