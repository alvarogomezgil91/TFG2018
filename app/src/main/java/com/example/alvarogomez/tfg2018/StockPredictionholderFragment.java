package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import com.example.alvarogomez.remoteDB.RemotePredictionStocksData;
import com.example.alvarogomez.remoteDB.RemoteStocksData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.w3c.dom.Text;

/**
 * Created by Alvaro Gomez on 18/08/2018.
 */

public class StockPredictionholderFragment extends Fragment {

    private MaterialCalendarView mMCalendarView;
    private LinearLayout mPredictionLayout;
    private Context mContext;
    private View view;
    public static String mMetodo;
    public static String mURL;
    private List<String> mStockListFechas;
    private List<CalendarDay> mStockListCalendarFechasAlza;
    private List<CalendarDay> mStockListCalendarFechasBaja;
    private List<CalendarDay> mStockListCalendarFechasIgual;
    private List<Stock> mStockList;
    private static final String ARG_SECTION_NUMBER = "section_number2";

    int position = 0;
    private static String currentDay;
    private static String mSimbolo;


    private TextView tvFecha;
    private TextView tvApertura;
    private TextView tvCierre;
    private TextView tvPorcentaje;
    private ImageView ivIcono;


    private String mFecha;
    private float mApertura;
    private float mCierre;
    private float mPorcentaje;
    private String mTextPorcentaje;

    public StockPredictionholderFragment() {
    }

    public static StockPredictionholderFragment newInstance(int sectionNumber, String simbolo) {

        StockPredictionholderFragment fragment = new StockPredictionholderFragment();

        mSimbolo = simbolo;
        mMetodo = Constants.GET_REMOTE_PREDICTION_STOCKS_DATA;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        currentDay = dateFormat.format(date);

        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.i("audit", this.getClass().getSimpleName() + " >>>>>> Entrando en el método " + Thread.currentThread().getStackTrace()[2].getMethodName());

        view = inflater.inflate(R.layout.fragment_prediction_calendar, container, false);
        mContext = getActivity();
        setHasOptionsMenu(true);

        mMCalendarView = (MaterialCalendarView) view.findViewById(R.id.prediction_calendar);

        tvFecha = (TextView) view.findViewById(R.id.tv_fecha_calendar);
        tvApertura = (TextView) view.findViewById(R.id.tv_apertura);
        tvCierre = (TextView) view.findViewById(R.id.tv_cierre);
        tvPorcentaje = (TextView) view.findViewById(R.id.tv_porcentaje);
        ivIcono = (ImageView) view.findViewById(R.id.imageView2);

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();

        mMCalendarView.state().edit()
                .setFirstDayOfWeek(Calendar.MONDAY)
                .setMinimumDate(CalendarDay.from(1900, 1, 1))
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        final CalendarDay cd = CalendarDay.from(new Date());

        mMCalendarView.addDecorators(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {
                return day.equals(cd);
            }

            @Override
            public void decorate(DayViewFacade view) {
                //view.addSpan(new ForegroundColorSpan(Color.RED));
                //view.addSpan(new BackgroundColorSpan(Color.GREEN));
                Drawable drawable = ContextCompat.getDrawable(mContext, R.drawable.current_day_calendar);
                view.setBackgroundDrawable(drawable);
            }
        });

        mMCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

                Date dateAux = date.getDate();
                SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                String fechaAux = dateFormat1.format(dateAux);
                mFecha = dateFormat2.format(dateAux);

                if (mStockListFechas.contains(fechaAux)) {

                    int position = mStockListFechas.indexOf(fechaAux);

                    mApertura = mStockList.get(position).getApertura();
                    mCierre = mStockList.get(position).getCierre();
                    mPorcentaje = (mCierre - mApertura) / 100;

                    tvFecha.setText(mFecha);
                    tvApertura.setText(String.format(Locale.US, "%.2f", mApertura));
                    tvCierre.setText(String.format(Locale.US, "%.2f", mCierre));
                    mTextPorcentaje = String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                    tvPorcentaje.setText(mTextPorcentaje);

                    if (mApertura > mCierre) {
                        mTextPorcentaje = String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                        tvPorcentaje.setText(mTextPorcentaje);
                        tvPorcentaje.setTextColor(Color.RED);
                        ivIcono.setImageResource(R.drawable.flecha_baja);
                    } else if (mApertura < mCierre) {
                        mTextPorcentaje = "+" + String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                        tvPorcentaje.setText(mTextPorcentaje);
                        tvPorcentaje.setTextColor(Color.GREEN);
                        ivIcono.setImageResource(R.drawable.flecha_alza);
                    } else {
                        mTextPorcentaje = String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                        tvPorcentaje.setText(mTextPorcentaje);
                        tvPorcentaje.setTextColor(Color.BLUE);
                        ivIcono.setImageResource(R.drawable.igual);
                    }
                }

            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();

        ThreadCreation threadCreation = new ThreadCreation();
        threadCreation.execute().toString();
        //lock screen to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onPause() {
        super.onPause();
        //set rotation to sensor dependent
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
    }

    private void setIconInMenu(Menu menu, int menuItemId, int labelId, int iconId) {

        MenuItem item = menu.findItem(menuItemId);
        SpannableStringBuilder builder = new SpannableStringBuilder("   " + getResources().getString(labelId));
        builder.setSpan(new ImageSpan(mContext, iconId), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        item.setTitle(builder);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        setIconInMenu(menu, R.id.item_log_out, R.string.log_out, R.drawable.ic_action_log_out);
        menu.removeItem(R.id.item_search);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        switch (item.getItemId()){

            case R.id.item_log_out:
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("logOut", "1");
                startActivity(intent);
                getActivity().finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class ThreadCreation extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            mStockList = new ArrayList<>();
            mStockListFechas = new ArrayList<>();
            mStockListCalendarFechasAlza = new ArrayList<>();
            mStockListCalendarFechasBaja = new ArrayList<>();
            mStockListCalendarFechasIgual = new ArrayList<>();

            List<Stock> stockDataList = new ArrayList<Stock>();
            java.lang.reflect.Method method = null;

            try {
                method = RemotePredictionStocksData.class.getMethod(mMetodo);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            RemotePredictionStocksData remoteStocksData = new RemotePredictionStocksData(mSimbolo, currentDay);

            try {
                stockDataList = (List<Stock>) method.invoke(remoteStocksData);

            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            int cont = 0;

            for (ListIterator<Stock> iter = stockDataList.listIterator(); iter.hasNext(); ) {

                Stock stockData;

                stockData = stockDataList.get(cont);

                stockData = iter.next();
                String simbolo = stockData.getSimbolo();
                String fecha = stockData.getFecha();
                float apertura = stockData.getApertura();
                float cierre = stockData.getCierre();

                Stock stockAux = new Stock();

                stockAux.setSimbolo(simbolo);
                stockAux.setFecha(fecha);
                stockAux.setApertura(apertura);
                stockAux.setCierre(cierre);

                mStockList.add(stockAux);
                mStockListFechas.add(fecha);

                DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (cierre > apertura) {
                        mStockListCalendarFechasAlza.add(CalendarDay.from(dateFormat1.parse(fecha)));
                    } else if (cierre < apertura) {
                        mStockListCalendarFechasBaja.add(CalendarDay.from(dateFormat1.parse(fecha)));
                    } else {
                        mStockListCalendarFechasIgual.add(CalendarDay.from(dateFormat1.parse(fecha)));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                cont++;

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void voids) {
            super.onPostExecute(voids);

            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            currentDay = dateFormat1.format(date);
            mFecha = dateFormat2.format(date);

            String previousBussinessDate;

            if (mStockListFechas.contains(currentDay)) {
                position = mStockListFechas.indexOf(currentDay);
            } else {
                try {
                    previousBussinessDate = previousBussinessDateString(currentDay, mStockListFechas);
                    position = mStockListFechas.indexOf(previousBussinessDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (position > 0 ) {

                mApertura = mStockList.get(position).getApertura();
                mCierre = mStockList.get(position).getCierre();
                mPorcentaje = (mCierre - mApertura) / 100;

                tvFecha.setText(mFecha);
                tvApertura.setText(String.format(Locale.US, "%.2f", mApertura));
                tvCierre.setText(String.format(Locale.US, "%.2f", mCierre));

                if (mApertura > mCierre) {
                    mTextPorcentaje = String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                    tvPorcentaje.setText(mTextPorcentaje);
                    tvPorcentaje.setTextColor(Color.RED);
                    ivIcono.setImageResource(R.drawable.flecha_baja);
                } else if (mApertura < mCierre) {
                    mTextPorcentaje = "+" + String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                    tvPorcentaje.setText(mTextPorcentaje);
                    tvPorcentaje.setTextColor(Color.GREEN);
                    ivIcono.setImageResource(R.drawable.flecha_alza);
                } else {
                    mTextPorcentaje = String.format(Locale.US, "%.2f", mPorcentaje) + "%";
                    tvPorcentaje.setText(mTextPorcentaje);
                    tvPorcentaje.setTextColor(Color.BLUE);
                    ivIcono.setImageResource(R.drawable.igual);
                }
            } else {
                tvApertura.setText("----");
                tvCierre.setText("----");
                tvPorcentaje.setText("----");
                tvPorcentaje.setTextColor(Color.BLUE);
                ivIcono.setImageResource(R.drawable.igual);
            }

            mMCalendarView.addDecorators(new EventDayDecorator(R.drawable.bg_green_holo, mStockListCalendarFechasAlza, mContext));
            mMCalendarView.addDecorators(new EventDayDecorator(R.drawable.bg_red_holo, mStockListCalendarFechasBaja, mContext));
            mMCalendarView.addDecorators(new EventDayDecorator(R.drawable.bg_green_holo, mStockListCalendarFechasIgual, mContext));

        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            //Toast.makeText(getBaseContext(), "Tarea pesada cancelada", Toast.LENGTH_SHORT).show();
        }

    }


    public static String previousBussinessDateString(String dateString, List<String> mStockListFechas) throws ParseException {

        String result = dateString;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (mStockListFechas.size() != 0) {

            String firstDateString = mStockListFechas.get(0);
            Date firstDate = dateFormat.parse(firstDateString);

            if (firstDate.after(new Date())) {

                return "";

            }

            while (!mStockListFechas.contains(result)) {

                result = previousDateString(result);
            }
        }

        return result;

    }

    public static String previousDateString(String dateString)
            throws ParseException {
        // Create a date formatter using your format string
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Parse the given date string into a Date object.
        // Note: This can throw a ParseException.
        Date myDate = dateFormat.parse(dateString);

        // Use the Calendar class to subtract one day
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(myDate);
        calendar.add(Calendar.DATE, -1);

        // Use the date formatter to produce a formatted date string
        Date previousDate = calendar.getTime();
        String result = dateFormat.format(previousDate);

        return result;
    }

}
