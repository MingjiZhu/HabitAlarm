package com.gm.habitalarm.utils;

import android.graphics.Canvas;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MultiLineXAxisRenderer extends XAxisRenderer {

    public MultiLineXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(
            Canvas c,
            String formattedLabel,
            float x,
            float y,
            MPPointF anchor,
            float angleDegrees) {

        String[] lines = formattedLabel.split("\n");
        Utils.drawXAxisValue(c, lines[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
        for (int i = 1; i < lines.length; i++) {
            Utils.drawXAxisValue(
                    c,
                    lines[i],
                    x,
                    y + mAxisLabelPaint.getTextSize() * i,
                    mAxisLabelPaint,
                    anchor,
                    angleDegrees);
        }
    }
}
