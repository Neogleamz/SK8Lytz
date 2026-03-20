package com.google.android.material.timepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.c0;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.chip.Chip;
import k7.h;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
class TimePickerView extends ConstraintLayout {
    private final Chip E;
    private final Chip F;
    private final ClockHandView G;
    private final ClockFaceView H;
    private final MaterialButtonToggleGroup K;
    private final View.OnClickListener L;
    private f O;
    private g P;
    private e Q;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (TimePickerView.this.P != null) {
                TimePickerView.this.P.a(((Integer) view.getTag(k7.f.R)).intValue());
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements MaterialButtonToggleGroup.e {
        b() {
        }

        @Override // com.google.android.material.button.MaterialButtonToggleGroup.e
        public void a(MaterialButtonToggleGroup materialButtonToggleGroup, int i8, boolean z4) {
            int i9 = i8 == k7.f.f21162k ? 1 : 0;
            if (TimePickerView.this.O == null || !z4) {
                return;
            }
            TimePickerView.this.O.a(i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends GestureDetector.SimpleOnGestureListener {
        c() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public boolean onDoubleTap(MotionEvent motionEvent) {
            boolean onDoubleTap = super.onDoubleTap(motionEvent);
            if (TimePickerView.this.Q != null) {
                TimePickerView.this.Q.a();
            }
            return onDoubleTap;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements View.OnTouchListener {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ GestureDetector f18735a;

        d(GestureDetector gestureDetector) {
            this.f18735a = gestureDetector;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (((Checkable) view).isChecked()) {
                return this.f18735a.onTouchEvent(motionEvent);
            }
            return false;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface e {
        void a();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface f {
        void a(int i8);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface g {
        void a(int i8);
    }

    public TimePickerView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TimePickerView(Context context, AttributeSet attributeSet, int i8) {
        super(context, attributeSet, i8);
        this.L = new a();
        LayoutInflater.from(context).inflate(h.f21197t, this);
        this.H = (ClockFaceView) findViewById(k7.f.f21160i);
        MaterialButtonToggleGroup materialButtonToggleGroup = (MaterialButtonToggleGroup) findViewById(k7.f.f21163l);
        this.K = materialButtonToggleGroup;
        materialButtonToggleGroup.g(new b());
        this.E = (Chip) findViewById(k7.f.f21166o);
        this.F = (Chip) findViewById(k7.f.f21164m);
        this.G = (ClockHandView) findViewById(k7.f.f21161j);
        D();
        C();
    }

    private void C() {
        Chip chip = this.E;
        int i8 = k7.f.R;
        chip.setTag(i8, 12);
        this.F.setTag(i8, 10);
        this.E.setOnClickListener(this.L);
        this.F.setOnClickListener(this.L);
    }

    @SuppressLint({"ClickableViewAccessibility"})
    private void D() {
        d dVar = new d(new GestureDetector(getContext(), new c()));
        this.E.setOnTouchListener(dVar);
        this.F.setOnTouchListener(dVar);
    }

    private void E() {
        if (this.K.getVisibility() == 0) {
            androidx.constraintlayout.widget.b bVar = new androidx.constraintlayout.widget.b();
            bVar.j(this);
            bVar.h(k7.f.f21159h, c0.E(this) == 0 ? 2 : 1);
            bVar.d(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        E();
    }

    @Override // android.view.View
    protected void onVisibilityChanged(View view, int i8) {
        super.onVisibilityChanged(view, i8);
        if (view == this && i8 == 0) {
            E();
        }
    }
}
