package com.google.android.material.datepicker;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import androidx.core.view.c0;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.v;
import com.google.android.libraries.barhopper.RecognitionOptions;
import com.google.android.material.button.MaterialButton;
import java.util.Calendar;
import java.util.Iterator;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class f<S> extends m<S> {
    static final Object A0 = "MONTHS_VIEW_GROUP_TAG";
    static final Object B0 = "NAVIGATION_PREV_TAG";
    static final Object C0 = "NAVIGATION_NEXT_TAG";
    static final Object D0 = "SELECTOR_TOGGLE_TAG";

    /* renamed from: q0  reason: collision with root package name */
    private int f17831q0;

    /* renamed from: r0  reason: collision with root package name */
    private DateSelector<S> f17832r0;

    /* renamed from: s0  reason: collision with root package name */
    private CalendarConstraints f17833s0;

    /* renamed from: t0  reason: collision with root package name */
    private Month f17834t0;

    /* renamed from: u0  reason: collision with root package name */
    private k f17835u0;

    /* renamed from: v0  reason: collision with root package name */
    private com.google.android.material.datepicker.b f17836v0;

    /* renamed from: w0  reason: collision with root package name */
    private RecyclerView f17837w0;

    /* renamed from: x0  reason: collision with root package name */
    private RecyclerView f17838x0;

    /* renamed from: y0  reason: collision with root package name */
    private View f17839y0;

    /* renamed from: z0  reason: collision with root package name */
    private View f17840z0;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements Runnable {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ int f17841a;

        a(int i8) {
            this.f17841a = i8;
        }

        @Override // java.lang.Runnable
        public void run() {
            f.this.f17838x0.t1(this.f17841a);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b extends androidx.core.view.a {
        b() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            super.g(view, cVar);
            cVar.e0(null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class c extends n {
        final /* synthetic */ int I;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        c(Context context, int i8, boolean z4, int i9) {
            super(context, i8, z4);
            this.I = i9;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // androidx.recyclerview.widget.LinearLayoutManager
        public void N1(RecyclerView.y yVar, int[] iArr) {
            if (this.I == 0) {
                iArr[0] = f.this.f17838x0.getWidth();
                iArr[1] = f.this.f17838x0.getWidth();
                return;
            }
            iArr[0] = f.this.f17838x0.getHeight();
            iArr[1] = f.this.f17838x0.getHeight();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements l {
        d() {
        }

        @Override // com.google.android.material.datepicker.f.l
        public void a(long j8) {
            if (f.this.f17833s0.f().s0(j8)) {
                f.this.f17832r0.O0(j8);
                Iterator<com.google.android.material.datepicker.l<S>> it = f.this.f17883p0.iterator();
                while (it.hasNext()) {
                    it.next().b((S) f.this.f17832r0.G0());
                }
                f.this.f17838x0.getAdapter().h();
                if (f.this.f17837w0 != null) {
                    f.this.f17837w0.getAdapter().h();
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class e extends RecyclerView.n {

        /* renamed from: a  reason: collision with root package name */
        private final Calendar f17845a = p.q();

        /* renamed from: b  reason: collision with root package name */
        private final Calendar f17846b = p.q();

        e() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.n
        public void i(Canvas canvas, RecyclerView recyclerView, RecyclerView.y yVar) {
            if ((recyclerView.getAdapter() instanceof q) && (recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                q qVar = (q) recyclerView.getAdapter();
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                for (androidx.core.util.d<Long, Long> dVar : f.this.f17832r0.M()) {
                    Long l8 = dVar.f4889a;
                    if (l8 != null && dVar.f4890b != null) {
                        this.f17845a.setTimeInMillis(l8.longValue());
                        this.f17846b.setTimeInMillis(dVar.f4890b.longValue());
                        int E = qVar.E(this.f17845a.get(1));
                        int E2 = qVar.E(this.f17846b.get(1));
                        View D = gridLayoutManager.D(E);
                        View D2 = gridLayoutManager.D(E2);
                        int Z2 = E / gridLayoutManager.Z2();
                        int Z22 = E2 / gridLayoutManager.Z2();
                        int i8 = Z2;
                        while (i8 <= Z22) {
                            View D3 = gridLayoutManager.D(gridLayoutManager.Z2() * i8);
                            if (D3 != null) {
                                canvas.drawRect(i8 == Z2 ? D.getLeft() + (D.getWidth() / 2) : 0, D3.getTop() + f.this.f17836v0.f17812d.c(), i8 == Z22 ? D2.getLeft() + (D2.getWidth() / 2) : recyclerView.getWidth(), D3.getBottom() - f.this.f17836v0.f17812d.b(), f.this.f17836v0.f17816h);
                            }
                            i8++;
                        }
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.google.android.material.datepicker.f$f  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0133f extends androidx.core.view.a {
        C0133f() {
        }

        @Override // androidx.core.view.a
        public void g(View view, androidx.core.view.accessibility.c cVar) {
            f fVar;
            int i8;
            super.g(view, cVar);
            if (f.this.f17840z0.getVisibility() == 0) {
                fVar = f.this;
                i8 = k7.j.I;
            } else {
                fVar = f.this;
                i8 = k7.j.G;
            }
            cVar.n0(fVar.Q(i8));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class g extends RecyclerView.s {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.datepicker.k f17849a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ MaterialButton f17850b;

        g(com.google.android.material.datepicker.k kVar, MaterialButton materialButton) {
            this.f17849a = kVar;
            this.f17850b = materialButton;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.s
        public void a(RecyclerView recyclerView, int i8) {
            if (i8 == 0) {
                CharSequence text = this.f17850b.getText();
                if (Build.VERSION.SDK_INT >= 16) {
                    recyclerView.announceForAccessibility(text);
                } else {
                    recyclerView.sendAccessibilityEvent(RecognitionOptions.PDF417);
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.s
        public void b(RecyclerView recyclerView, int i8, int i9) {
            LinearLayoutManager Z1 = f.this.Z1();
            int a22 = i8 < 0 ? Z1.a2() : Z1.d2();
            f.this.f17834t0 = this.f17849a.D(a22);
            this.f17850b.setText(this.f17849a.E(a22));
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class h implements View.OnClickListener {
        h() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            f.this.e2();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class i implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.datepicker.k f17853a;

        i(com.google.android.material.datepicker.k kVar) {
            this.f17853a = kVar;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int a22 = f.this.Z1().a2() + 1;
            if (a22 < f.this.f17838x0.getAdapter().c()) {
                f.this.c2(this.f17853a.D(a22));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class j implements View.OnClickListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ com.google.android.material.datepicker.k f17855a;

        j(com.google.android.material.datepicker.k kVar) {
            this.f17855a = kVar;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int d22 = f.this.Z1().d2() - 1;
            if (d22 >= 0) {
                f.this.c2(this.f17855a.D(d22));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum k {
        DAY,
        YEAR
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface l {
        void a(long j8);
    }

    private void S1(View view, com.google.android.material.datepicker.k kVar) {
        MaterialButton materialButton = (MaterialButton) view.findViewById(k7.f.f21168r);
        materialButton.setTag(D0);
        c0.t0(materialButton, new C0133f());
        MaterialButton materialButton2 = (MaterialButton) view.findViewById(k7.f.f21170t);
        materialButton2.setTag(B0);
        MaterialButton materialButton3 = (MaterialButton) view.findViewById(k7.f.f21169s);
        materialButton3.setTag(C0);
        this.f17839y0 = view.findViewById(k7.f.B);
        this.f17840z0 = view.findViewById(k7.f.f21173w);
        d2(k.DAY);
        materialButton.setText(this.f17834t0.q(view.getContext()));
        this.f17838x0.l(new g(kVar, materialButton));
        materialButton.setOnClickListener(new h());
        materialButton3.setOnClickListener(new i(kVar));
        materialButton2.setOnClickListener(new j(kVar));
    }

    private RecyclerView.n T1() {
        return new e();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int Y1(Context context) {
        return context.getResources().getDimensionPixelSize(k7.d.R);
    }

    public static <T> f<T> a2(DateSelector<T> dateSelector, int i8, CalendarConstraints calendarConstraints) {
        f<T> fVar = new f<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i8);
        bundle.putParcelable("GRID_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        bundle.putParcelable("CURRENT_MONTH_KEY", calendarConstraints.i());
        fVar.t1(bundle);
        return fVar;
    }

    private void b2(int i8) {
        this.f17838x0.post(new a(i8));
    }

    @Override // androidx.fragment.app.Fragment
    public void I0(Bundle bundle) {
        super.I0(bundle);
        bundle.putInt("THEME_RES_ID_KEY", this.f17831q0);
        bundle.putParcelable("GRID_SELECTOR_KEY", this.f17832r0);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.f17833s0);
        bundle.putParcelable("CURRENT_MONTH_KEY", this.f17834t0);
    }

    @Override // com.google.android.material.datepicker.m
    public boolean J1(com.google.android.material.datepicker.l<S> lVar) {
        return super.J1(lVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CalendarConstraints U1() {
        return this.f17833s0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public com.google.android.material.datepicker.b V1() {
        return this.f17836v0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Month W1() {
        return this.f17834t0;
    }

    public DateSelector<S> X1() {
        return this.f17832r0;
    }

    LinearLayoutManager Z1() {
        return (LinearLayoutManager) this.f17838x0.getLayoutManager();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void c2(Month month) {
        RecyclerView recyclerView;
        int i8;
        com.google.android.material.datepicker.k kVar = (com.google.android.material.datepicker.k) this.f17838x0.getAdapter();
        int F = kVar.F(month);
        int F2 = F - kVar.F(this.f17834t0);
        boolean z4 = Math.abs(F2) > 3;
        boolean z8 = F2 > 0;
        this.f17834t0 = month;
        if (!z4 || !z8) {
            if (z4) {
                recyclerView = this.f17838x0;
                i8 = F + 3;
            }
            b2(F);
        }
        recyclerView = this.f17838x0;
        i8 = F - 3;
        recyclerView.l1(i8);
        b2(F);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void d2(k kVar) {
        this.f17835u0 = kVar;
        if (kVar == k.YEAR) {
            this.f17837w0.getLayoutManager().y1(((q) this.f17837w0.getAdapter()).E(this.f17834t0.f17781c));
            this.f17839y0.setVisibility(0);
            this.f17840z0.setVisibility(8);
        } else if (kVar == k.DAY) {
            this.f17839y0.setVisibility(8);
            this.f17840z0.setVisibility(0);
            c2(this.f17834t0);
        }
    }

    void e2() {
        k kVar = this.f17835u0;
        k kVar2 = k.YEAR;
        if (kVar == kVar2) {
            d2(k.DAY);
        } else if (kVar == k.DAY) {
            d2(kVar2);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = p();
        }
        this.f17831q0 = bundle.getInt("THEME_RES_ID_KEY");
        this.f17832r0 = (DateSelector) bundle.getParcelable("GRID_SELECTOR_KEY");
        this.f17833s0 = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.f17834t0 = (Month) bundle.getParcelable("CURRENT_MONTH_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public View t0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        int i8;
        int i9;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getContext(), this.f17831q0);
        this.f17836v0 = new com.google.android.material.datepicker.b(contextThemeWrapper);
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(contextThemeWrapper);
        Month j8 = this.f17833s0.j();
        if (com.google.android.material.datepicker.g.o2(contextThemeWrapper)) {
            i8 = k7.h.f21202y;
            i9 = 1;
        } else {
            i8 = k7.h.f21200w;
            i9 = 0;
        }
        View inflate = cloneInContext.inflate(i8, viewGroup, false);
        GridView gridView = (GridView) inflate.findViewById(k7.f.f21174x);
        c0.t0(gridView, new b());
        gridView.setAdapter((ListAdapter) new com.google.android.material.datepicker.e());
        gridView.setNumColumns(j8.f17782d);
        gridView.setEnabled(false);
        this.f17838x0 = (RecyclerView) inflate.findViewById(k7.f.A);
        this.f17838x0.setLayoutManager(new c(getContext(), i9, false, i9));
        this.f17838x0.setTag(A0);
        com.google.android.material.datepicker.k kVar = new com.google.android.material.datepicker.k(contextThemeWrapper, this.f17832r0, this.f17833s0, new d());
        this.f17838x0.setAdapter(kVar);
        int integer = contextThemeWrapper.getResources().getInteger(k7.g.f21178b);
        RecyclerView recyclerView = (RecyclerView) inflate.findViewById(k7.f.B);
        this.f17837w0 = recyclerView;
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            this.f17837w0.setLayoutManager(new GridLayoutManager((Context) contextThemeWrapper, integer, 1, false));
            this.f17837w0.setAdapter(new q(this));
            this.f17837w0.h(T1());
        }
        if (inflate.findViewById(k7.f.f21168r) != null) {
            S1(inflate, kVar);
        }
        if (!com.google.android.material.datepicker.g.o2(contextThemeWrapper)) {
            new v().b(this.f17838x0);
        }
        this.f17838x0.l1(kVar.F(this.f17834t0));
        return inflate;
    }
}
