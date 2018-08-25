package com.example.alvarogomez.tfg2018;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alvaro Gomez on 25/08/2018.
 */

public class EventDayDecorator implements DayViewDecorator{

    private final int mResource;
    private final List<CalendarDay> mDates;
    private final Context mContext;

    public EventDayDecorator(int mResource, List<CalendarDay> mDates, Context mContext) {
        this.mResource = mResource;
        this.mDates = mDates;
        this.mContext = mContext;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return mDates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        Drawable drawable = ContextCompat.getDrawable(mContext, mResource);
        view.setBackgroundDrawable(drawable);
    }

}
