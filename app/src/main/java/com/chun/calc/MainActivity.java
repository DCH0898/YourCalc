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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText cost;// 成本
    private EditText bilge;// 底仓
    private EditText total;// 总额
    private EditText unit_price;// 单价
    private EditText share;// 份额
    private EditText value;// 市值
    private EditText valuation;// 估价
    private EditText have_share;// 持有份额
    private EditText have_value;// 持有市值
    private EditText increase;// 涨幅
    private EditText profit;// 盈亏额
    private EditText buy_share;// 买卖份额
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        cost = (EditText) findViewById(R.id.cost);
        bilge = (EditText) findViewById(R.id.bilge);
        total = (EditText) findViewById(R.id.total);
        unit_price = (EditText) findViewById(R.id.unit_price);
        share = (EditText) findViewById(R.id.share);
        value = (EditText) findViewById(R.id.value);
        valuation = (EditText) findViewById(R.id.valuation);
        have_share = (EditText) findViewById(R.id.have_share);
        have_value = (EditText) findViewById(R.id.have_value);
        increase = (EditText) findViewById(R.id.increase);
        profit = (EditText) findViewById(R.id.profit);
        buy_share = (EditText) findViewById(R.id.buy_share);
        name = (EditText) findViewById(R.id.name);
        table = (TableLayout) findViewById(R.id.table);
        ac1 = (AppCompatButton) findViewById(R.id.ac1);
        ac2 = (AppCompatButton) findViewById(R.id.ac2);
        ac3 = (AppCompatButton) findViewById(R.id.ac3);
        button_save = (AppCompatButton) findViewById(R.id.button_save);

        bilge.addTextChangedListener(costWatcher);
        total.addTextChangedListener(costWatcher);
        unit_price.addTextChangedListener(firstWatcher);
        share.addTextChangedListener(firstWatcher);
        valuation.addTextChangedListener(valuationWatcher);
        have_share.addTextChangedListener(valuationWatcher);
        profit.addTextChangedListener(profitWatcher);
        buy_share.addTextChangedListener(colorWatcher);
        ac1.setOnClickListener(this);
        ac2.setOnClickListener(this);
        ac3.setOnClickListener(this);
        button_save.setOnClickListener(this);

        TextView tv = new TextView(this);
        tv.setText("查看记录");
        tv.setTextColor(Color.WHITE);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, ScrollingActivity.class), 0);
            }
        });
        toolbar.addView(tv);

        // 添加


//        table.addView(tv);
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
            if (total.getText().length() > 0 && bilge.getText().length() > 0) {
                double total_ = Double.parseDouble(total.getText().toString());
                double bilge_ = Double.parseDouble(bilge.getText().toString());
                cost.setText(Calc.getCost(total_, bilge_) + "");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher firstWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (unit_price.getText().length() > 0 && share.getText().length() > 0) {
                double number = Double.parseDouble(unit_price.getText().toString());
                double share_ = Double.parseDouble(share.getText().toString());
                value.setText(df.format(Calc.getValue(number, share_)));
                have_share.setText(share.getText());
            }
            if (valuation.getText().length() > 0
                    && unit_price.getText().length() > 0
                    && !unit_price.getText().toString().equals("0")) {
                double valuation_ = Double.parseDouble(valuation.getText().toString());
                double unit_price_ = Double.parseDouble(unit_price.getText().toString());
                increase.setText(df.format(Calc.getIncrease(valuation_, unit_price_)) + "%");// 涨跌幅
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher valuationWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (valuation.getText().length() > 0
                    && value.getText().length() > 0
                    && have_share.getText().length() > 0
                    && !valuation.getText().toString().equals("0")) {
                double valuation_ = Double.parseDouble(valuation.getText().toString());
                double value_ = Double.parseDouble(value.getText().toString());
                double have_share_ = Double.parseDouble(have_share.getText().toString());
                have_value.setText(df.format(Calc.getHaveValue(valuation_, have_share_)));// 现有市值
                if (valuation.hasFocus()) {
                    profit.setText(
                            df.format(
                                    Calc.getProfit(
                                            Double.parseDouble(
                                                    have_value.getText().toString()), value_)));// 盈亏额
                }
                buy_share.setText(df.format(Calc.getBayShare(Calc.getHaveValue(valuation_, have_share_), value_, valuation_)));// 买卖份额
            }
            if (valuation.getText().length() > 0
                    && unit_price.getText().length() > 0
                    && !unit_price.getText().toString().equals("0")) {
                double valuation_ = Double.parseDouble(valuation.getText().toString());
                double unit_price_ = Double.parseDouble(unit_price.getText().toString());
                increase.setText(df.format(Calc.getIncrease(valuation_, unit_price_)) + "%");// 涨跌幅
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher newValueWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (valuation.getText().length() > 0
                    && value.getText().length() > 0
                    && !valuation.getText().toString().equals("0")) {
                double valuation_ = Double.parseDouble(valuation.getText().toString());
                double value_ = Double.parseDouble(value.getText().toString());
                double have_value_ = Double.parseDouble(have_value.getText().toString());
                buy_share.setText(df.format(Calc.getBayShare(have_value_, value_, valuation_)));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher colorWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (count > 0) {
                double number = Double.parseDouble(s.toString());
                if (number > 0) {
                    buy_share.setTextColor(Color.parseColor("#FF0000"));
                } else {
                    buy_share.setTextColor(Color.parseColor("#008000"));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher profitWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (have_share.getText().length() > 0
                    && profit.getText().length() > 0
                    && value.getText().length() > 0
                    && profit.hasFocus()
                    && !have_share.getText().toString().equals("0")) {
                double value_ = Double.parseDouble(value.getText().toString());
                double profit_ = Double.parseDouble(profit.getText().toString());
                double have_share_ = Double.parseDouble(have_share.getText().toString());
                valuation.setText(df.format(Calc.getValuation(value_, profit_, have_share_)));
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
        unit_price.setText(sp.getString("unit_price", "0"));
        share.setText(sp.getString("share", "0"));
        value.setText(sp.getString("value", "0"));
        valuation.setText(sp.getString("valuation", "0"));
        have_share.setText(sp.getString("have_share", "0"));
        have_value.setText(sp.getString("have_value", "0"));
        profit.setText(sp.getString("profit", "0"));
        buy_share.setText(sp.getString("buy_share", "0"));
        increase.setText(sp.getString("new_value", "0"));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ac1:
                unit_price.setText(kong);
                share.setText(kong);
                value.setText(kong);
                break;
            case R.id.ac2:
                valuation.setText(kong);
                have_share.setText(kong);
                have_value.setText(kong);
                break;
            case R.id.ac3:
                profit.setText(kong);
                buy_share.setText(kong);
                increase.setText(kong);
                break;
            case R.id.button_save:
                showSaveView();
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
            String spiteee = "\\#";
            String[] befores = before.split(spiteee);
            boolean isExist = false;
            for (String s : befores) {
                if (s.equals(name_)) {
                    isExist = true;
                }
            }
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
        editorr.putString("unit_price", unit_price.getText().toString());
        editorr.putString("share", share.getText().toString());
        editorr.putString("value", value.getText().toString());
        editorr.putString("valuation", valuation.getText().toString());
        editorr.putString("have_share", have_share.getText().toString());
        editorr.putString("have_value", have_value.getText().toString());
        editorr.putString("profit", profit.getText().toString());
        editorr.putString("buy_share", buy_share.getText().toString());
        editorr.putString("new_value", increase.getText().toString());
        editorr.apply();
    }

}
