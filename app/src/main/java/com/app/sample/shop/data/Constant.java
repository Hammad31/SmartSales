package com.app.sample.shop.data;

import android.util.Log;

import com.app.sample.shop.model.CompanyUnit;
import com.app.sample.shop.model.Order;
import com.app.sample.shop.model.Product;

public class Constant {
    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(android.os.Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {
            Log.e("", "erro ao recuperar a versão da API" + e.getMessage());
        }

        return f.floatValue();
    }

    public static Product currentProduct;
    public static CompanyUnit currentComapnyUnit;
    public static Order currentOrder;
}
