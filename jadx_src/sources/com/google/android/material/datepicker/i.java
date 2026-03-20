package com.google.android.material.datepicker;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class i<S> extends m<S> {

    /* renamed from: q0  reason: collision with root package name */
    private int f17864q0;

    /* renamed from: r0  reason: collision with root package name */
    private DateSelector<S> f17865r0;

    /* renamed from: s0  reason: collision with root package name */
    private CalendarConstraints f17866s0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends l<S> {
        a() {
        }

        @Override // com.google.android.material.datepicker.l
        public void a() {
            Iterator<l<S>> it = i.this.f17883p0.iterator();
            while (it.hasNext()) {
                it.next().a();
            }
        }

        @Override // com.google.android.material.datepicker.l
        public void b(S s8) {
            Iterator<l<S>> it = i.this.f17883p0.iterator();
            while (it.hasNext()) {
                it.next().b(s8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> i<T> L1(DateSelector<T> dateSelector, int i8, CalendarConstraints calendarConstraints) {
        i<T> iVar = new i<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i8);
        bundle.putParcelable("DATE_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        iVar.t1(bundle);
        return iVar;
    }

    @Override // androidx.fragment.app.Fragment
    public void I0(Bundle bundle) {
        super.I0(bundle);
        bundle.putInt("THEME_RES_ID_KEY", this.f17864q0);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.f17865r0);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.f17866s0);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = p();
        }
        this.f17864q0 = bundle.getInt("THEME_RES_ID_KEY");
        this.f17865r0 = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.f17866s0 = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public View t0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.f17865r0.j0(layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), this.f17864q0)), viewGroup, bundle, this.f17866s0, new a());
    }
}
