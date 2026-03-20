package com.google.android.material.datepicker;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Collection;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public interface DateSelector<S> extends Parcelable {
    Collection<Long> F0();

    S G0();

    String K(Context context);

    Collection<androidx.core.util.d<Long, Long>> M();

    void O0(long j8);

    View j0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, l<S> lVar);

    int p0(Context context);

    boolean z0();
}
