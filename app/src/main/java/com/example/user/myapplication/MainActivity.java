package com.example.user.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.ArrayList;
import java.util.List;

import edu.emory.mathcs.jtransforms.fft.DoubleFFT_1D;

import static com.example.user.myapplication.FFT.fft;
import static com.example.user.myapplication.FFT.ifft;
import static com.example.user.myapplication.FFT.show;
import static java.lang.Math.sqrt;


public class MainActivity extends AppCompatActivity {

    final int ILOSC_ELEMENTOW = 128;
    final int SENSOR_SPEED = SensorManager.SENSOR_DELAY_GAME;
    final int CZESTOTLIWOSC_ODCZYTOW = 50;
    Stoper stoper = new Stoper("Test");

    double freq;
    int nx;


    double re,im;

    CharSequence[] wybor_fft = {"jTransform", "FFT introcs"};
    CharSequence[] btnText = {"jTrans", "introcs"};

    Complex[] x;
    Complex[] y;
    Complex[] z;

    Complex[] x_ifft ;
    Complex[] y_ifft ;
    Complex[] z_ifft ;

    Complex[] x_Complex = new Complex[ILOSC_ELEMENTOW];
    Complex[] y_Complex = new Complex[ILOSC_ELEMENTOW];
    Complex[] z_Complex = new Complex[ILOSC_ELEMENTOW];

    Double[] xNormalizacja = new Double[ILOSC_ELEMENTOW];
    Double[] yNormalizacja = new Double[ILOSC_ELEMENTOW];
    Double[] zNormalizacja = new Double[ILOSC_ELEMENTOW];

    List<Double> listaX = new ArrayList();
    List<Double> listaY = new ArrayList();
    List<Double> listaZ = new ArrayList();

    List<Double> listaX_IFFT = new ArrayList();
    List<Double> listaY_IFFT = new ArrayList();
    List<Double> listaZ_IFFT = new ArrayList();

    double[] grawitacja = {0.0, 0.0, 9.81};
    double[] odczyt_liniowy = new double[3];
    double alpha = 0.8;
    int petla = 0;
    int sposob_liczenia = 0;


    DoubleFFT_1D fftD_X = new DoubleFFT_1D(ILOSC_ELEMENTOW);
    DoubleFFT_1D fftD_Y = new DoubleFFT_1D(ILOSC_ELEMENTOW);
    DoubleFFT_1D fftD_Z = new DoubleFFT_1D(ILOSC_ELEMENTOW);

    double[] fftX = new double[ILOSC_ELEMENTOW * 2];
    double[] fftY = new double[ILOSC_ELEMENTOW * 2];
    double[] fftZ = new double[ILOSC_ELEMENTOW * 2];

    double[] ifft_x = new double[ILOSC_ELEMENTOW];
    double[] ifft_y = new double[ILOSC_ELEMENTOW];
    double[] ifft_z = new double[ILOSC_ELEMENTOW];

    double[] inputX = new double[ILOSC_ELEMENTOW];
    double[] inputY = new double[ILOSC_ELEMENTOW];
    double[] inputZ = new double[ILOSC_ELEMENTOW];

    int index = 0;

    private SensorManager sensor_manager = null;
    private Sensor accelerometer = null;


    private GraphicalView wykres;
    private GraphicalView wykres2;

    private XYSeries xSeria;
    private XYSeries ySeria;
    private XYSeries zSeria;
    private XYSeries xSeria2;
    private XYSeries ySeria2;
    private XYSeries zSeria2;

    private XYMultipleSeriesDataset data_set;
    private XYMultipleSeriesRenderer renderer;

    private XYMultipleSeriesDataset data_set2;
    private XYMultipleSeriesRenderer renderer2;

    XYSeriesRenderer x_series_renderer;
    XYSeriesRenderer y_series_renderer;
    XYSeriesRenderer z_series_renderer;

    XYSeriesRenderer x_series_renderer2;
    XYSeriesRenderer y_series_renderer2;
    XYSeriesRenderer z_series_renderer2;

    int numer_odczytu = 0;
    private final int MAX_PLOT_VALUES = 30;


    private SensorEventListener mListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            grawitacja[0] = alpha * grawitacja[0] + (1 - alpha) * sensorEvent.values[0];
            grawitacja[1] = alpha * grawitacja[1] + (1 - alpha) * sensorEvent.values[1];
            grawitacja[2] = alpha * grawitacja[2] + (1 - alpha) * sensorEvent.values[2];

            odczyt_liniowy[0] = sensorEvent.values[0] - grawitacja[0];
            odczyt_liniowy[1] = sensorEvent.values[1] - grawitacja[1];
            odczyt_liniowy[2] = sensorEvent.values[2] - grawitacja[2];

         //   if(odczyt_liniowy[0] > -0.05 && odczyt_liniowy[0] <0.05) odczyt_liniowy[0] = 0;
         //   if(odczyt_liniowy[1] > -0.05 && odczyt_liniowy[1] <0.05) odczyt_liniowy[1] = 0;
         //   if(odczyt_liniowy[2] > -0.05 && odczyt_liniowy[2] <0.05) odczyt_liniowy[2] = 0;

            xSeria.add(numer_odczytu, odczyt_liniowy[0]);
            ySeria.add(numer_odczytu, odczyt_liniowy[1]);
            zSeria.add(numer_odczytu, odczyt_liniowy[2]);

            listaX.add(odczyt_liniowy[0]);
            listaY.add(odczyt_liniowy[1]);
            listaZ.add(odczyt_liniowy[2]);

            renderer.setXAxisMin(numer_odczytu - MAX_PLOT_VALUES);
            renderer.setXAxisMax(numer_odczytu);

            if (petla == ILOSC_ELEMENTOW) {
                petla = 0;
            }
            inputX[petla] =  odczyt_liniowy[0];
            inputY[petla] =  odczyt_liniowy[1];
            inputZ[petla] =  odczyt_liniowy[2];


            if (numer_odczytu >= ILOSC_ELEMENTOW) index++;
            if (index == ILOSC_ELEMENTOW) index = 0;

            System.arraycopy(inputX, index, fftX, 0, ILOSC_ELEMENTOW - index);
            System.arraycopy(inputX, 0, fftX, ILOSC_ELEMENTOW - index, index);
            System.arraycopy(inputY, index, fftY, 0, ILOSC_ELEMENTOW - index);
            System.arraycopy(inputY, 0, fftY, ILOSC_ELEMENTOW - index, index);
            System.arraycopy(inputZ, index, fftZ, 0, ILOSC_ELEMENTOW - index);
            System.arraycopy(inputZ, 0, fftZ, ILOSC_ELEMENTOW - index, index);

            xSeria2.clear();
            ySeria2.clear();
            zSeria2.clear();

            if (sposob_liczenia == 0) {

                stoper.start();
                fftD_X.realForwardFull(fftX);
                fftD_Y.realForwardFull(fftY);
                fftD_Z.realForwardFull(fftZ);

                double max =0;
                nx = ILOSC_ELEMENTOW * 2;

                for (int j = 0; j < ILOSC_ELEMENTOW; j++) {

                    freq = 50 * (double) j / (double) ILOSC_ELEMENTOW;
                    if (freq > 10 && freq <40   ) {
                        fftX[2*j] = 0.0;
                       fftX[2*j+1] = 0.0;
                        fftY[2*j] = 0.0;
                        fftY[2*j+1] = 0.0;
                        fftZ[2*j] = 0.0;
                        fftZ[2*j+1] = 0.0;
                    }

                    re = fftX[2*j];
                    im = fftX[2*j+1];
                    xNormalizacja[j] = sqrt(re*re+im*im);

                    re = fftY[2*j];
                    im = fftY[2*j+1];
                    yNormalizacja[j] = sqrt(re*re+im*im);

                    re = fftZ[2*j];
                    im = fftZ[2*j+1];
                    zNormalizacja[j] = sqrt(re*re+im*im);
                }

                for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
                    if(xNormalizacja[j] > max || yNormalizacja[j] > max || zNormalizacja[j] > max ){
                        if (xNormalizacja[j] > yNormalizacja[j]){
                            if (xNormalizacja[j] > zNormalizacja[j])
                                max = xNormalizacja[j];
                            else max = zNormalizacja[j];
                        }
                        else if (yNormalizacja[j] > zNormalizacja[j])
                            max = yNormalizacja[j];
                        else max = zNormalizacja[j];
                    }
                }

                for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
                    freq = 50 * (double) j / (double) ILOSC_ELEMENTOW;
           //         if (freq > 10) {
                 //       xNormalizacja[j] = 0.0;
               // //        yNormalizacja[j] = 0.0;
                //        zNormalizacja[j] = 0.0;
               //     }
                    xSeria2.add(freq, xNormalizacja[j]/max);
                    ySeria2.add(freq, yNormalizacja[j]/max);
                    zSeria2.add(freq, zNormalizacja[j]/max);
                }

                fftD_X.complexInverse(fftX, true);
                fftD_Y.complexInverse(fftY, true);
                fftD_Z.complexInverse(fftZ, true);

                for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
                    ifft_x[j] = fftX[j * 2];
                    ifft_y[j] = fftY[j * 2];
                    ifft_z[j] = fftZ[j * 2];
                }

                if (numer_odczytu < ILOSC_ELEMENTOW) {
                    listaX_IFFT.add(ifft_x[numer_odczytu]);
                    listaY_IFFT.add(ifft_y[numer_odczytu]);
                    listaZ_IFFT.add(ifft_z[numer_odczytu]);
                } else {
                    listaX_IFFT.add(ifft_x[ILOSC_ELEMENTOW-1]);
                    listaY_IFFT.add(ifft_y[ILOSC_ELEMENTOW-1]);
                    listaZ_IFFT.add(ifft_z[ILOSC_ELEMENTOW-1]);
                }
                stoper.stop();
            }

            else if(sposob_liczenia==1){

                stoper.start();
                for (int i = 0; i < ILOSC_ELEMENTOW; i++) {
                    x_Complex[i] = new Complex(fftX[i], 0);
                    y_Complex[i] = new Complex(fftY[i], 0);
                    z_Complex[i] = new Complex(fftZ[i], 0);
                }

                x = fft(x_Complex);
                y = fft(y_Complex);
                z = fft(z_Complex);


                for (int j = 0; j < ILOSC_ELEMENTOW/2; j++) {

                    freq = 50 * (double) j / (double) ILOSC_ELEMENTOW;
                    if (freq > 9) {
                        x[j] = x[j].minus(x[j]);
                        y[j] = y[j].minus(y[j]);
                        z[j] = z[j].minus(z[j]);
                    }

                    re = x[j].re();
                    im = x[j].im();
                    xNormalizacja[j] = sqrt(re*re+im*im);

                    re = y[j].re();
                    im = y[j].im();
                    yNormalizacja[j] = sqrt(re*re+im*im);

                    re = z[j].re();
                    im = z[j].im();
                    zNormalizacja[j] = sqrt(re*re+im*im);
                }

                double max =0.0;

                for (int j = 0; j < ILOSC_ELEMENTOW/2; j++) {
                    if(xNormalizacja[j] > max || yNormalizacja[j] > max || zNormalizacja[j] > max ){
                        if (xNormalizacja[j] > yNormalizacja[j]){
                            if (xNormalizacja[j] > zNormalizacja[j])
                                max = xNormalizacja[j];
                            else max = zNormalizacja[j];
                        }
                        else if (yNormalizacja[j] > zNormalizacja[j])
                            max = yNormalizacja[j];
                        else max = zNormalizacja[j];
                    }
                }

                for (int j = 0; j < ILOSC_ELEMENTOW/2-1; j++) {
                    freq = 50 * (double) j / (double) ILOSC_ELEMENTOW;
                    xSeria2.add(freq, xNormalizacja[j]/max);
                    ySeria2.add(freq, yNormalizacja[j]/max);
                    zSeria2.add(freq, zNormalizacja[j]/max);
                }

                x_ifft = ifft(x);
                y_ifft = ifft(y);
                z_ifft = ifft(z);

                for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
                    ifft_x[j] = x_ifft[j].re();
                    ifft_y[j] = y_ifft[j].re();
                   ifft_z[j] = z_ifft[j].re();
                }

                if (numer_odczytu < ILOSC_ELEMENTOW) {
                    listaX_IFFT.add(ifft_x[numer_odczytu]);
                    listaY_IFFT.add(ifft_y[numer_odczytu]);
                   listaZ_IFFT.add(ifft_z[numer_odczytu]);
                } else {
                    listaX_IFFT.add(ifft_x[ILOSC_ELEMENTOW-1]);
                    listaY_IFFT.add(ifft_y[ILOSC_ELEMENTOW-1]);
                    listaZ_IFFT.add(ifft_z[ILOSC_ELEMENTOW-1]);
                }
                stoper.stop();
            }


            System.out.println( "czas wykonania:");
            System.out.println(stoper);

            renderer2.setXAxisMin(0);
            renderer2.setXAxisMax(50);

            petla++;
            numer_odczytu++;

            wykres.repaint();
            wykres2.repaint();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensor_manager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if (accelerometer != null) {
            setupChart();
            setupChart2();
            ((Button) findViewById(R.id.btnStart)).setEnabled(true);
            ((Button) findViewById(R.id.btnChoose)).setEnabled(true);

            for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
                inputX[j] = 0;
            }

        } else {
            Toast.makeText(this, "Akcelerometr niedostępny", Toast.LENGTH_LONG).show();
        }

    }

    private void setupChart() {
        xSeria = new XYSeries("X");
        ySeria = new XYSeries("Y");
        zSeria = new XYSeries("Z");

        data_set = new XYMultipleSeriesDataset();

        data_set.addSeries(xSeria);
        data_set.addSeries(ySeria);
        data_set.addSeries(zSeria);

        x_series_renderer = new XYSeriesRenderer();
        x_series_renderer.setColor(Color.RED);
        x_series_renderer.setLineWidth(3);

        y_series_renderer = new XYSeriesRenderer();
        y_series_renderer.setColor(Color.BLUE);
        y_series_renderer.setLineWidth(3);

        z_series_renderer = new XYSeriesRenderer();
        z_series_renderer.setColor(Color.YELLOW);
        z_series_renderer.setLineWidth(3);

        renderer = new XYMultipleSeriesRenderer();
        renderer.setChartTitle("Odczyt z akcelerometru");
        renderer.setXTitle(" ");
        renderer.setYTitle("Amplituda");

        renderer.setChartTitleTextSize(getResources().getDimension(R.dimen.chart_title_text_size));
        renderer.setAxisTitleTextSize(getResources().getDimension(R.dimen.axis_title_text_size));
        renderer.setLegendTextSize(getResources().getDimension(R.dimen.legend_text_size));
        renderer.setLabelsTextSize(getResources().getDimension(R.dimen.labels_text_size));
        renderer.setPointSize(5);

        renderer.setMargins(new int[]{
                (int) getResources().getDimension(R.dimen.margin_top),
                (int) getResources().getDimension(R.dimen.margin_left),
                (int) getResources().getDimension(R.dimen.margin_bottom),
                (int) getResources().getDimension(R.dimen.margin_right)
        });

        renderer.setBackgroundColor(Color.GRAY);
        renderer.setApplyBackgroundColor(true);

        renderer.setShowGrid(true);
        renderer.setPanEnabled(true, false);
        renderer.setZoomEnabled(false, false);

        renderer.addSeriesRenderer(x_series_renderer);
        renderer.addSeriesRenderer(y_series_renderer);
        renderer.addSeriesRenderer(z_series_renderer);

        wykres = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), data_set, renderer);
        ((LinearLayout) findViewById(R.id.chart_container)).addView(wykres);

    }

    private void setupChart2() {
        xSeria2 = new XYSeries("x ");
        ySeria2 = new XYSeries("Y");
        zSeria2 = new XYSeries("Z");

        data_set2 = new XYMultipleSeriesDataset();

        data_set2.addSeries(xSeria2);
        data_set2.addSeries(ySeria2);
        data_set2.addSeries(zSeria2);

        x_series_renderer2 = new XYSeriesRenderer();
        x_series_renderer2.setColor(Color.RED);
        x_series_renderer2.setLineWidth(3);

        y_series_renderer2 = new XYSeriesRenderer();
        y_series_renderer2.setColor(Color.BLUE);
        y_series_renderer2.setLineWidth(3);

        z_series_renderer2 = new XYSeriesRenderer();
        z_series_renderer2.setColor(Color.YELLOW);
        z_series_renderer2.setLineWidth(3);

        renderer2 = new XYMultipleSeriesRenderer();
        renderer2.setChartTitle("FFT");
        renderer2.setXTitle("Czestotliwosc [Hz]");
        renderer2.setYTitle("Amplituda");

        renderer2.setChartTitleTextSize(getResources().getDimension(R.dimen.chart_title_text_size));
        renderer2.setAxisTitleTextSize(getResources().getDimension(R.dimen.axis_title_text_size));
        renderer2.setLegendTextSize(getResources().getDimension(R.dimen.legend_text_size));
        renderer2.setLabelsTextSize(getResources().getDimension(R.dimen.labels_text_size));
        renderer2.setPointSize(5);

        renderer2.setMargins(new int[]{
                (int) getResources().getDimension(R.dimen.margin_top),
                (int) getResources().getDimension(R.dimen.margin_left),
                (int) getResources().getDimension(R.dimen.margin_bottom),
                (int) getResources().getDimension(R.dimen.margin_right)
        });

        renderer2.setBackgroundColor(Color.GRAY);
        renderer2.setApplyBackgroundColor(true);

        renderer2.setShowGrid(true);
        renderer2.setPanEnabled(false, false);
        renderer2.setZoomEnabled(false, false);

        renderer2.addSeriesRenderer(x_series_renderer2);
        renderer2.addSeriesRenderer(y_series_renderer2);
        renderer2.addSeriesRenderer(z_series_renderer2);

        wykres2 = (GraphicalView) ChartFactory.getLineChartView(getBaseContext(), data_set2, renderer2);
        ((LinearLayout) findViewById(R.id.chart_container2)).addView(wykres2);

    }

    public void startRecording(View v) {

        xSeria.clear();
        ySeria.clear();
        zSeria.clear();

        xSeria2.clear();
        ySeria2.clear();
        zSeria2.clear();

        listaX.clear();
        listaY.clear();
        listaZ.clear();

        listaX_IFFT.clear();
        listaY_IFFT.clear();
        listaZ_IFFT.clear();

        numer_odczytu = 0;
        petla = 0;
        index = 0;

        for (int j = 0; j < ILOSC_ELEMENTOW; j++) {
            inputX[j] = 0;
            inputY[j] = 0;
            inputZ[j] = 0;
                    }

        renderer.setChartTitle("Odczyt z akcelerometru");
        renderer.setXTitle(" ");
        renderer.setYTitle("Amplituda");

        sensor_manager.registerListener(mListener, accelerometer, SENSOR_SPEED);

        ((Button) findViewById(R.id.btnStart)).setEnabled(false);
        ((Button) findViewById(R.id.btnStop)).setEnabled(true);
        ((Button) findViewById(R.id.btnChoose)).setEnabled(false);
        ((Button) findViewById(R.id.btnSygnal)).setEnabled(false);
        ((Button) findViewById(R.id.btnIFFT)).setEnabled(false);
        ((Button) findViewById(R.id.btnOdczyt)).setEnabled(false);
    }

    public void stopRecording(View v) {

        sensor_manager.unregisterListener(mListener);

        ((Button) findViewById(R.id.btnStart)).setEnabled(true);
        ((Button) findViewById(R.id.btnStop)).setEnabled(false);
        ((Button) findViewById(R.id.btnChoose)).setEnabled(true);
        ((Button) findViewById(R.id.btnSygnal)).setEnabled(true);
        ((Button) findViewById(R.id.btnIFFT)).setEnabled(true);
        ((Button) findViewById(R.id.btnOdczyt)).setEnabled(false);
    }

    public void FFT_jtransform(View v) {

        Intent nowyEkran = new Intent(getApplicationContext(), SecondActivity.class);
        Global.lista_globalnaX = listaX;
        Global.lista_globalnaY = listaY;
        Global.lista_globalnaZ = listaZ;
        Global.lista_globalnaIFFTX = listaX_IFFT;
        Global.lista_globalnaIFFTY = listaY_IFFT;
        Global.lista_globalnaIFFTZ = listaZ_IFFT;
       // nowyEkran.putExtra("listaX", new DataWrapper(listaX));
    //    nowyEkran.putExtra("listaY", new DataWrapper(listaY));
   ///     nowyEkran.putExtra("listaZ", new DataWrapper(listaZ));
    //   nowyEkran.putExtra("listaX_IFFT", new DataWrapper(listaX_IFFT));
    //    nowyEkran.putExtra("listaY_IFFT", new DataWrapper(listaY_IFFT));
    //    nowyEkran.putExtra("listaZ_IFFT", new DataWrapper(listaZ_IFFT));
        startActivity(nowyEkran);
    }

    public void Test(View v) {

        double[] input = new double[]{
                0.0176,
                -0.0620,
                0.2467,
                0.4599,
                -0.0582,
                0.4694,
                0.0001,
                -0.2873};
        DoubleFFT_1D fftDo = new DoubleFFT_1D(input.length);
        double[] fft = new double[input.length * 2];
        System.arraycopy(input, 0, fft, 0, input.length);
        fftDo.realForwardFull(fft);



        for(double d: fft) {
            System.out.println(d);
        }

        fftDo.complexInverse(fft,true);
        for (double d: fft)
            System.out.println(d);


        Complex[] x = new Complex[8];

        // original data
        for (int i = 0; i < 8; i++) {
            x[i] = new Complex(input[i], 0);
        }
        show(x, "x");

        // FFT of original data
        Complex[] y = fft(x);
        show(y, "y = fft(x)");

        Complex[] z = ifft(y);
        show(z, "z = ifft(y)");




    }

    public void odczyt(View v) {
        xSeria.clear();
        ySeria.clear();
        zSeria.clear();

        int n= listaX.size();

        for (int j=0; j<n;j++) {
            xSeria.add(j, listaX.get(j));
            ySeria.add(j, listaY.get(j));
            zSeria.add(j, listaZ.get(j));
        }

        renderer.setChartTitle("Odczyt z akcelerometru");
        renderer.setXTitle("");
        renderer.setYTitle("Amplituda");

        renderer.setXAxisMin(n-30);
        renderer.setXAxisMax(n);

        wykres.repaint();


        ((Button) findViewById(R.id.btnSygnal)).setEnabled(true);
        ((Button) findViewById(R.id.btnOdczyt)).setEnabled(false);
    }

    public void sygnal(View v) {

        xSeria.clear();
        ySeria.clear();
        zSeria.clear();

        int n = listaX.size();
        int i=0;

        if (n > ILOSC_ELEMENTOW) {
            for (int j = n - ILOSC_ELEMENTOW; j < n; j++) {
                xSeria.add(i, listaX.get(j));
                ySeria.add(i, listaY.get(j));
                zSeria.add(i, listaZ.get(j));
                i++;
            }
            renderer.setXAxisMin(0);
            renderer.setXAxisMax(i);
        }
        else{
            for(int j=0; j<n;j++){
                xSeria.add(j, listaX.get(j));
                ySeria.add(j, listaY.get(j));
                zSeria.add(j, listaZ.get(j));
                 }
            renderer.setXAxisMin(0);
            renderer.setXAxisMax(n);
        }

        renderer.setChartTitle("Sygnał");
        renderer.setXTitle("");
        renderer.setYTitle("Amplituda");

        wykres.repaint();

        ((Button) findViewById(R.id.btnSygnal)).setEnabled(false);
        ((Button) findViewById(R.id.btnOdczyt)).setEnabled(true);

    }

    public void chooseMethod(View v) {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Wybierz sposób liczenia transformaty: ");
        alert.setItems(wybor_fft, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                sposob_liczenia = i;
                Toast.makeText(getApplicationContext(), "Twój wybór to " + wybor_fft[i], Toast.LENGTH_LONG).show();
                ((Button) findViewById(R.id.btnChoose)).setText(btnText[i]);
            }
        });
        AlertDialog alert_dialog = alert.create();
        alert_dialog.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
   //     mSensorManager.unregisterListener(mListener);
    //    ((Button) findViewById(R.id.btnStop)).performClick();
    }


}
