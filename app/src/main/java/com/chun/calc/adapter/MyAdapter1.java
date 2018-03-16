package com.chun.calc.adapter;


import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chun.calc.BeanTwo;
import com.chun.calc.R;
import com.chun.calc.util.FunUtil;

import java.text.DecimalFormat;

/**
 * Created by Dachun Li on 2018-3-14.
 */

public class MyAdapter1 extends BaseQuickAdapter {

    DecimalFormat df = new DecimalFormat("######0.00");

    public MyAdapter1(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        BeanTwo beanTwo = (BeanTwo) item;
        if (!FunUtil.isEmpty(beanTwo.getTitle())) {
            helper.setText(R.id.title, beanTwo.getTitle());
        }
        if (!FunUtil.isEmpty(beanTwo.getNotice())) {
            helper.setText(R.id.notice, beanTwo.getNotice());
        }
        if (!FunUtil.isEmpty(beanTwo.getProfit())) {
            helper.setText(R.id.profit, "" + beanTwo.getProfit());
        }
        if (!FunUtil.isEmpty(beanTwo.getQuota())) {
            helper.setText(R.id.quota, "" + beanTwo.getQuota());
        }
        if (!FunUtil.isEmpty(beanTwo.getShare())) {
            helper.setText(R.id.share, "" + beanTwo.getShare());
        }
        if (!FunUtil.isEmpty(beanTwo.getValuation())) {
            helper.setText(R.id.valuation, "" + beanTwo.getValuation());
        }

        final AppCompatEditText valuationACE = helper.getView(R.id.valuation);
        final AppCompatEditText shareACE = helper.getView(R.id.share);
        final AppCompatEditText quotaACE = helper.getView(R.id.quota);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (shareACE.getText().length() > 0
                        && valuationACE.getText().length() > 0
                        && quotaACE.getText().length() > 0
                        ) {
                    double share = Double.parseDouble(String.valueOf(shareACE.getText()));
                    double valuation = Double.parseDouble(String.valueOf(valuationACE.getText()));
                    double quota = Double.parseDouble(String.valueOf(quotaACE.getText()));
                    double profit = valuation * share - quota;
                    if (profit > 0) {
                        helper.setText(R.id.notice, "卖出" + df.format(Math.abs(profit / valuation)));
                    } else {
                        helper.setText(R.id.notice, "买进" + df.format(Math.abs(profit / valuation)));
                    }
                    helper.setText(R.id.profit, df.format(Math.abs(profit)));
                    helper.setTextColor(R.id.profit, profit > 0 ?
                            Color.parseColor("#FF0000") : Color.parseColor("#008000"));
                    helper.setTextColor(R.id.notice, profit > 0 ?
                            Color.parseColor("#FF0000") : Color.parseColor("#008000"));
                }
            }
        };
        valuationACE.addTextChangedListener(textWatcher);
        shareACE.addTextChangedListener(textWatcher);
        quotaACE.addTextChangedListener(textWatcher);
    }

}
