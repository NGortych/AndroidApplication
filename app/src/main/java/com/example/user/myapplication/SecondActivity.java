package com.example.user.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;

public class SecondActivity extends AppCompatActivity {

    int[] margins = {15,10,15,10};

    private GraphicalView wykres;
    private GraphicalView wykres2;
    private GraphicalView wykres3;

    private XYSeries xSeria;
    private XYSeries ySeria;
    private XYSeries xSeria2;
    private XYSeries ySeria2;
    private XYSeries xSeria3;
    private XYSeries ySeria3;

    private XYMultipleSeriesDataset data_set;
    private XYMultipleSeriesRenderer multi_renderer;

    private XYMultipleSeriesDataset data_set2;
    private XYMultipleSeriesRenderer multi_renderer2;

    private XYMultipleSeriesDataset data_set3;
    private XYMultipleSeriesRenderer multi_renderer3;

    XYSeriesRenderer x_series_renderer;
    XYSeriesRenderer y_series_renderer;
    XYSeriesRenderer x_series_renderer2;
    XYSeriesRenderer y_series_renderer2;
    XYSeriesRenderer x_series_renderer3;
    XYSeriesRenderer y_series_renderer3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);

     /*   DataWrapper dw1 = (DataWrapper) getIntent().getSerializableExtra("listaX");
        DataWrapper dw2 = (DataWrapper) getIntent().getSerializableExtra("listaY");
        DataWrapper dw3 = (DataWrapper) getIntent().getSerializableExtra("listaZ");
        DataWrapper dw4 = (DataWrapper) getIntent().getSerializableExtra("listaX_IFFT");
        DataWrapper dw5 = (DataWrapper) getIntent().getSerializableExtra("listaY_IFFT");
        DataWrapper dw6 = (DataWrapper) getIntent().getSerializableExtra("listaZ_IFFT");

*/

        List<Double> listaX = Global.lista_globalnaX;
        List<Double> listaY = Global.lista_globalnaY;
        List<Double> listaZ = Global.lista_globalnaZ;
        List<Double> listaX_IFFT = Global.lista_globalnaIFFTX;
        List<Double> listaY_IFFT = Global.lista_globalnaIFFTY;
        List<Double> listaZ_IFFT = Global.lista_globalnaIFFTZ;
    /*   // List<Double> listaX = dw1.getParliaments();
        List<Double> listaY = dw2.getParliaments();
        List<Double> listaZ = dw3.getParliaments();
        List<Double> listaX_IFFT = dw4.getParliaments();
        List<Double> listaY_IFFT = dw5.getParliaments();
        List<Double> listaZ_IFFT = dw6.getParliaments();
*/
        setupChart();
        setupChart2();
        setupChart3();

        xSeria.clear();
        xSeria2.clear();
        xSeria3.clear();
        ySeria.clear();
        ySeria2.clear();
        ySeria3.clear();

        int n = listaX.size();

        for (int j = 0; j < n; j++)
        {
            xSeria.add(j,listaX.get(j));
            xSeria2.add(j,listaY.get(j));
            xSeria3.add(j,listaZ.get(j));

     //       if(listaX.get(j) > -0.05 && listaX.get(j) <0.05) listaX_IFFT.set(j,0.0) ;
      //      if(listaY.get(j) > -0.05 && listaY.get(j) <0.05) listaY_IFFT.set(j,0.0) ;
       //     if(listaZ.get(j) > -0.05 && listaZ.get(j) <0.05) listaZ_IFFT.set(j,0.0) ;

            ySeria.add(j,listaX_IFFT.get(j));
            ySeria2.add(j,listaY_IFFT.get(j));
            ySeria3.add(j,listaZ_IFFT.get(j));


        }

        multi_renderer.setXAxisMin(0);
        multi_renderer.setXAxisMax(200);

        multi_renderer2.setXAxisMin(0);
        multi_renderer2.setXAxisMax(200);

        multi_renderer3.setXAxisMin(0);
        multi_renderer3.setXAxisMax(200);

        wykres.repaint();
        wykres2.repaint();
        wykres3.repaint();
    }

    public void Powrot(View v){
    finish();
    }

    private void setupChart() {
        xSeria = new XYSeries("X ");
        ySeria = new XYSeries("Y");

        data_set = new XYMultipleSeriesDataset();

        data_set.addSeries(xSeria);
        data_set.addSeries(ySeria);

        x_series_renderer = new XYSeriesRenderer();
        x_series_renderer.setColor(Color.RED);
        x_series_renderer.setLineWidth(3);

        y_series_renderer = new XYSeriesRenderer();
        y_series_renderer.setColor(Color.BLUE);
        y_series_renderer.setLineWidth(3);

        x_series_renderer.setShowLegendItem(false);
        y_series_renderer.setShowLegendItem(false);

        multi_renderer = new XYMultipleSeriesRenderer();
        multi_renderer.setChartTitle("X");

        multi_renderer.setChartTitleTextSize(getResources().getDimension(R.dimen.chart_title_text_size));
        multi_renderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.axis_title_text_size));
        multi_renderer.setLegendTextSize(getResources().getDimension(R.dimen.legend_text_size));
        multi_renderer.setLabelsTextSize(getResources().getDimension(R.dimen.labels_text_size));
        multi_renderer.setPointSize(5);

        multi_renderer.setMargins(margins);

        multi_renderer.setBackgroundColor(Color.GRAY);
        multi_renderer.setApplyBackgroundColor(true);

        multi_renderer.setShowGrid(true);
        multi_renderer.setPanEnabled(true, true);
        multi_renderer.setZoomEnabled(true, true);

        multi_renderer.addSeriesRenderer(x_series_renderer);
        multi_renderer.addSeriesRenderer(y_series_renderer);

        wykres = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), data_set, multi_renderer);
        ((LinearLayout) findViewById(R.id.chart_container)).addView(wykres);
    }

    private void setupChart2() {
        xSeria2 = new XYSeries("X ");
        ySeria2 = new XYSeries("Y");

        data_set2 = new XYMultipleSeriesDataset();

        data_set2.addSeries(xSeria2);
        data_set2.addSeries(ySeria2);

        x_series_renderer2 = new XYSeriesRenderer();
        x_series_renderer2.setColor(Color.RED);
        x_series_renderer2.setLineWidth(3);

        y_series_renderer2 = new XYSeriesRenderer();
        y_series_renderer2.setColor(Color.BLUE);
        y_series_renderer2.setLineWidth(3);

        x_series_renderer2.setShowLegendItem(false);
        y_series_renderer2.setShowLegendItem(false);

        multi_renderer2 = new XYMultipleSeriesRenderer();
        multi_renderer2.setChartTitle("Y");

        multi_renderer2.setChartTitleTextSize(getResources().getDimension(R.dimen.chart_title_text_size));
        multi_renderer2.setAxisTitleTextSize(getResources().getDimension(R.dimen.axis_title_text_size));
        multi_renderer2.setLegendTextSize(getResources().getDimension(R.dimen.legend_text_size));
        multi_renderer2.setLabelsTextSize(getResources().getDimension(R.dimen.labels_text_size));
        multi_renderer2.setPointSize(5);

        multi_renderer2.setMargins(margins);

        multi_renderer2.setBackgroundColor(Color.GRAY);
        multi_renderer2.setApplyBackgroundColor(true);

        multi_renderer2.setShowGrid(true);
        multi_renderer2.setPanEnabled(true, true);
        multi_renderer2.setZoomEnabled(true, true);

        multi_renderer2.addSeriesRenderer(x_series_renderer2);
        multi_renderer2.addSeriesRenderer(y_series_renderer2);

        wykres2 = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), data_set2, multi_renderer2);
        ((LinearLayout) findViewById(R.id.chart_container2)).addView(wykres2);

    }

    private void setupChart3() {
        xSeria3 = new XYSeries("Sygnał przed FFT      ");
        ySeria3 = new XYSeries("Sygnał po FFT");

        data_set3 = new XYMultipleSeriesDataset();

        data_set3.addSeries(xSeria3);
        data_set3.addSeries(ySeria3);

        x_series_renderer3 = new XYSeriesRenderer();
        x_series_renderer3.setColor(Color.RED);
        x_series_renderer3.setLineWidth(3);

        y_series_renderer3 = new XYSeriesRenderer();
        y_series_renderer3.setColor(Color.BLUE);
        y_series_renderer3.setLineWidth(3);

        multi_renderer3 = new XYMultipleSeriesRenderer();
        multi_renderer3.setChartTitle("Z");

        multi_renderer3.setChartTitleTextSize(getResources().getDimension(R.dimen.chart_title_text_size));
        multi_renderer3.setAxisTitleTextSize(getResources().getDimension(R.dimen.axis_title_text_size));
        multi_renderer3.setLegendTextSize(getResources().getDimension(R.dimen.legend_text_size));
        multi_renderer3.setLabelsTextSize(getResources().getDimension(R.dimen.labels_text_size));
        multi_renderer3.setPointSize(5);

        multi_renderer3.setMargins(margins);

        multi_renderer3.setBackgroundColor(Color.GRAY);
        multi_renderer3.setApplyBackgroundColor(true);

        multi_renderer3.setShowGrid(true);
        multi_renderer3.setPanEnabled(true, true);
        multi_renderer3.setZoomEnabled(true, true);

        multi_renderer3.addSeriesRenderer(x_series_renderer3);
        multi_renderer3.addSeriesRenderer(y_series_renderer3);

        wykres3 = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), data_set3, multi_renderer3);
        ((LinearLayout) findViewById(R.id.chart_container3)).addView(wykres3);

    }

}
