package com.mebee.mall.utils;

import android.util.Log;

import com.mebee.mall.bean.Ware;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mebee on 2017/8/25.
 */

public class SortWaresUtil {

    private static final String TAG = "SortWaresUtil";

    public static List<Ware> reverse(List<Ware> wares){
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));
        Collections.reverse(wares);
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        return wares;
    }

    public static List<Ware> sortByVolumeDown(List<Ware> wares){
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));
        Collections.sort(wares, new Comparator<Ware>() {
            @Override
            public int compare(Ware o1, Ware o2) {
                return o1.getSales_volume() >= o2.getSales_volume()? 1:-1;
            }
        });
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        return wares;
    }

    public static List<Ware> sortByVolumeUp(List<Ware> wares){
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));
        Collections.sort(wares, new Comparator<Ware>() {
            @Override
            public int compare(Ware o1, Ware o2) {
                return o1.getSales_volume() >= o2.getSales_volume()? 1:-1;
            }
        });
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        return wares;
    }

    public static List<Ware> sortByPriceDown(List<Ware> wares){
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        Collections.sort(wares, new Comparator<Ware>() {
            @Override
            public int compare(Ware o1, Ware o2) {
                return o1.getPrice() >= o2.getPrice()? 1:-1;
            }
        });
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));
        return wares;
    }

    public static List<Ware> sortByPriceUp(List<Ware> wares){
        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        Collections.sort(wares, new Comparator<Ware>() {
            @Override
            public int compare(Ware o1, Ware o2) {
                return o1.getPrice() >= o2.getPrice()? -1:1;
            }
        });

        Log.d(TAG, "sortByPriceUp: " + JSONUtil.toJSON(wares));

        return wares;
    }

}
