package com.mridx.accountsmanage.lib;

import java.text.DecimalFormat;

public class PercentageCalculator {

    private float sellerWeight, factoryWeight;

    public PercentageCalculator() {
    }

    public PercentageCalculator(String sellerWeight, String factoryWeight) { //previous month // current month
        float s = Float.parseFloat(sellerWeight);
        float f = Float.parseFloat(factoryWeight);
        this.sellerWeight = s;
        this.factoryWeight = f;
    }

    public float getPercentage() {
        float calc;
        calc = ((sellerWeight - factoryWeight) / sellerWeight) * 100;
        if (sellerWeight - factoryWeight > 0) {
            calc = calc * -1;
            //calc = ((sellerWeight - factoryWeight) / sellerWeight) * 100;
        } else {
            calc = calc * -1;
        }
        DecimalFormat decimalFormat = new DecimalFormat("##.##");
        calc = Float.parseFloat(decimalFormat.format(calc));
        return calc;
    }

    public String getDecimial(String amount) {

        String amt = null;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        amt = decimalFormat.format(Double.parseDouble(amount));

        return amt;
    }


}
