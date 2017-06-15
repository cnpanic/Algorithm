package com.polkapolka.bluetooth.le;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
/**
 * Created by crypto_lab on 2017-06-08.
 */

public class ChartActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        GraphView line_graph = (GraphView)findViewById(R.id.graph);
        LineGraphSeries<DataPoint> line_series =
                new LineGraphSeries<DataPoint>(new DataPoint[] {
                        new DataPoint(0, 2),
                        new DataPoint(1, 120),
                        new DataPoint(2, 21),
                        new DataPoint(4, 27),
                        new DataPoint(5, 2),
                        new DataPoint(6, 120),
                        new DataPoint(7, 21),
                        new DataPoint(8, 139),
                        new DataPoint(9, 27),
                        new DataPoint(10, 2),
                        new DataPoint(11, 120),
                        new DataPoint(12, 21),
                        new DataPoint(13, 139),
                        new DataPoint(14, 27),
                        new DataPoint(15, 2),
                        new DataPoint(16, 120),
                        new DataPoint(17, 21),
                        new DataPoint(18, 139),
                        new DataPoint(19, 27),
                });
        line_graph.addSeries(line_series);


        // set the bound

        // set manual X bounds // x값 최대 최소
        line_graph.getViewport().setXAxisBoundsManual(true);
        line_graph.getViewport().setMinX(0.0);
        line_graph.getViewport().setMaxX(10.0);

        // set manual Y bounds
        line_graph.getViewport().setYAxisBoundsManual(true);
        line_graph.getViewport().setMinY(0);
        line_graph.getViewport().setMaxY(160);

        line_graph.getViewport().setScrollable(true);



    }
}
