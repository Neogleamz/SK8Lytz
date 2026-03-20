package com.google.android.material.datepicker;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.c0;
import androidx.fragment.app.r;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.internal.CheckableImageButton;
import java.util.Iterator;
import java.util.LinkedHashSet;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class g<S> extends androidx.fragment.app.c {
    static final Object W0 = "CONFIRM_BUTTON_TAG";
    static final Object X0 = "CANCEL_BUTTON_TAG";
    static final Object Y0 = "TOGGLE_BUTTON_TAG";
    private final LinkedHashSet<h<? super S>> F0 = new LinkedHashSet<>();
    private final LinkedHashSet<View.OnClickListener> G0 = new LinkedHashSet<>();
    private final LinkedHashSet<DialogInterface.OnCancelListener> H0 = new LinkedHashSet<>();
    private final LinkedHashSet<DialogInterface.OnDismissListener> I0 = new LinkedHashSet<>();
    private int J0;
    private DateSelector<S> K0;
    private m<S> L0;
    private CalendarConstraints M0;
    private f<S> N0;
    private int O0;
    private CharSequence P0;
    private boolean Q0;
    private int R0;
    private TextView S0;
    private CheckableImageButton T0;
    private x7.h U0;
    private Button V0;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements View.OnClickListener {
        a() {
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Iterator it = g.this.F0.iterator();
            while (it.hasNext()) {
                ((h) it.next()).a(g.this.l2());
            }
            g.this.M1();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements View.OnClickListener {
        b() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Iterator it = g.this.G0.iterator();
            while (it.hasNext()) {
                ((View.OnClickListener) it.next()).onClick(view);
            }
            g.this.M1();
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class c extends l<S> {
        c() {
        }

        @Override // com.google.android.material.datepicker.l
        public void a() {
            g.this.V0.setEnabled(false);
        }

        @Override // com.google.android.material.datepicker.l
        public void b(S s8) {
            g.this.s2();
            g.this.V0.setEnabled(g.this.K0.z0());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class d implements View.OnClickListener {
        d() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            g.this.V0.setEnabled(g.this.K0.z0());
            g.this.T0.toggle();
            g gVar = g.this;
            gVar.t2(gVar.T0);
            g.this.r2();
        }
    }

    private static Drawable h2(Context context) {
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{16842912}, h.a.b(context, k7.e.f21142b));
        stateListDrawable.addState(new int[0], h.a.b(context, k7.e.f21143c));
        return stateListDrawable;
    }

    private static int i2(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(k7.d.Z) + resources.getDimensionPixelOffset(k7.d.f21091a0) + resources.getDimensionPixelOffset(k7.d.Y);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(k7.d.T);
        int i8 = j.f17868f;
        return dimensionPixelSize + dimensionPixelSize2 + (resources.getDimensionPixelSize(k7.d.R) * i8) + ((i8 - 1) * resources.getDimensionPixelOffset(k7.d.X)) + resources.getDimensionPixelOffset(k7.d.P);
    }

    private static int k2(Context context) {
        Resources resources = context.getResources();
        int dimensionPixelOffset = resources.getDimensionPixelOffset(k7.d.Q);
        int i8 = Month.i().f17782d;
        return (dimensionPixelOffset * 2) + (resources.getDimensionPixelSize(k7.d.S) * i8) + ((i8 - 1) * resources.getDimensionPixelOffset(k7.d.W));
    }

    private int m2(Context context) {
        int i8 = this.J0;
        return i8 != 0 ? i8 : this.K0.p0(context);
    }

    private void n2(Context context) {
        this.T0.setTag(Y0);
        this.T0.setImageDrawable(h2(context));
        this.T0.setChecked(this.R0 != 0);
        c0.t0(this.T0, null);
        t2(this.T0);
        this.T0.setOnClickListener(new d());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean o2(Context context) {
        return q2(context, 16843277);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean p2(Context context) {
        return q2(context, k7.b.L);
    }

    static boolean q2(Context context, int i8) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(u7.b.c(context, k7.b.E, f.class.getCanonicalName()), new int[]{i8});
        boolean z4 = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void r2() {
        int m22 = m2(l1());
        this.N0 = f.a2(this.K0, m22, this.M0);
        this.L0 = this.T0.isChecked() ? i.L1(this.K0, m22, this.M0) : this.N0;
        s2();
        r l8 = q().l();
        l8.q(k7.f.f21175y, this.L0);
        l8.k();
        this.L0.J1(new c());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void s2() {
        String j22 = j2();
        this.S0.setContentDescription(String.format(Q(k7.j.f21219o), j22));
        this.S0.setText(j22);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void t2(CheckableImageButton checkableImageButton) {
        this.T0.setContentDescription(checkableImageButton.getContext().getString(this.T0.isChecked() ? k7.j.F : k7.j.H));
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public final void I0(Bundle bundle) {
        super.I0(bundle);
        bundle.putInt("OVERRIDE_THEME_RES_ID", this.J0);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.K0);
        CalendarConstraints.b bVar = new CalendarConstraints.b(this.M0);
        if (this.N0.W1() != null) {
            bVar.b(this.N0.W1().f17784f);
        }
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", bVar.a());
        bundle.putInt("TITLE_TEXT_RES_ID_KEY", this.O0);
        bundle.putCharSequence("TITLE_TEXT_KEY", this.P0);
    }

    @Override // androidx.fragment.app.c
    public final Dialog R1(Bundle bundle) {
        Dialog dialog = new Dialog(l1(), m2(l1()));
        Context context = dialog.getContext();
        this.Q0 = o2(context);
        int c9 = u7.b.c(context, k7.b.f21066s, g.class.getCanonicalName());
        x7.h hVar = new x7.h(context, null, k7.b.E, k7.k.E);
        this.U0 = hVar;
        hVar.P(context);
        this.U0.a0(ColorStateList.valueOf(c9));
        this.U0.Z(c0.y(dialog.getWindow().getDecorView()));
        return dialog;
    }

    public String j2() {
        return this.K0.K(getContext());
    }

    public final S l2() {
        return this.K0.G0();
    }

    @Override // androidx.fragment.app.c, android.content.DialogInterface.OnCancelListener
    public final void onCancel(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnCancelListener> it = this.H0.iterator();
        while (it.hasNext()) {
            it.next().onCancel(dialogInterface);
        }
        super.onCancel(dialogInterface);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = p();
        }
        this.J0 = bundle.getInt("OVERRIDE_THEME_RES_ID");
        this.K0 = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.M0 = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
        this.O0 = bundle.getInt("TITLE_TEXT_RES_ID_KEY");
        this.P0 = bundle.getCharSequence("TITLE_TEXT_KEY");
        this.R0 = bundle.getInt("INPUT_MODE_KEY");
    }

    @Override // androidx.fragment.app.c, android.content.DialogInterface.OnDismissListener
    public final void onDismiss(DialogInterface dialogInterface) {
        Iterator<DialogInterface.OnDismissListener> it = this.I0.iterator();
        while (it.hasNext()) {
            it.next().onDismiss(dialogInterface);
        }
        ViewGroup viewGroup = (ViewGroup) T();
        if (viewGroup != null) {
            viewGroup.removeAllViews();
        }
        super.onDismiss(dialogInterface);
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Window window = V1().getWindow();
        if (this.Q0) {
            window.setLayout(-1, -1);
            window.setBackgroundDrawable(this.U0);
        } else {
            window.setLayout(-2, -2);
            int dimensionPixelOffset = K().getDimensionPixelOffset(k7.d.U);
            Rect rect = new Rect(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset);
            window.setBackgroundDrawable(new InsetDrawable((Drawable) this.U0, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset));
            window.getDecorView().setOnTouchListener(new o7.a(V1(), rect));
        }
        r2();
    }

    @Override // androidx.fragment.app.c, androidx.fragment.app.Fragment
    public void onStop() {
        this.L0.K1();
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public final View t0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(this.Q0 ? k7.h.E : k7.h.D, viewGroup);
        Context context = inflate.getContext();
        if (this.Q0) {
            inflate.findViewById(k7.f.f21175y).setLayoutParams(new LinearLayout.LayoutParams(k2(context), -2));
        } else {
            View findViewById = inflate.findViewById(k7.f.f21176z);
            View findViewById2 = inflate.findViewById(k7.f.f21175y);
            findViewById.setLayoutParams(new LinearLayout.LayoutParams(k2(context), -1));
            findViewById2.setMinimumHeight(i2(l1()));
        }
        TextView textView = (TextView) inflate.findViewById(k7.f.F);
        this.S0 = textView;
        c0.v0(textView, 1);
        this.T0 = (CheckableImageButton) inflate.findViewById(k7.f.G);
        TextView textView2 = (TextView) inflate.findViewById(k7.f.K);
        CharSequence charSequence = this.P0;
        if (charSequence != null) {
            textView2.setText(charSequence);
        } else {
            textView2.setText(this.O0);
        }
        n2(context);
        this.V0 = (Button) inflate.findViewById(k7.f.f21153c);
        if (this.K0.z0()) {
            this.V0.setEnabled(true);
        } else {
            this.V0.setEnabled(false);
        }
        this.V0.setTag(W0);
        this.V0.setOnClickListener(new a());
        Button button = (Button) inflate.findViewById(k7.f.f21149a);
        button.setTag(X0);
        button.setOnClickListener(new b());
        return inflate;
    }
}
