package com.baidu.drawview;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private DrawView drawView;

    private List<DrawView.Item> constructDataList() {
        List<DrawView.Item> list = new ArrayList<DrawView.Item>();
        DrawView.Item item = null;
        for (int i = 0; i < 9; i++) {
            item = new DrawView.Item();
            item.bgDrawableId = R.drawable.bnav_bg_draw_item;
            item.bgDrawableIds = R.drawable.bnav_bg_draw_item_s;
            item.imgDrawableId = R.drawable.bnav_src_draw_item;
            item.imgDrawableIds = R.drawable.bnav_src_draw_item_s;
            item.descStrColorId = android.R.color.background_dark;
            item.descStrColorIds = android.R.color.holo_blue_dark;
            item.descStr = "index:" + i;
            list.add(item);
        }
        return list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawView = findViewById(R.id.draw_view);
        List<DrawView.Item> list = constructDataList();
        drawView.setList(list);
        int MIN_ROUND = 5;
        int maxNum = (DrawView.CELLS_COUNT - 1) * MIN_ROUND + new Random().nextInt(DrawView.CELLS_COUNT - 1);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, maxNum);
        valueAnimator.setDuration(5_000);
        final int[] vals = {0, 1, 2, 5, 8, 7, 6, 3};
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int val = (int) animation.getAnimatedValue();
                val = val % 7;
                val = vals[val];
                drawView.updateHighLightIndex(val);
            }
        });

        valueAnimator.setInterpolator(new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return (float) (Math.cos((input + 1) * Math.PI) / 2.0f) + 0.5f;
            }
        });
        valueAnimator.start();
    }

}
