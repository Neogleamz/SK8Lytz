package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.lifecycle.k0;
import androidx.lifecycle.l0;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class c extends Fragment implements DialogInterface.OnCancelListener, DialogInterface.OnDismissListener {
    private Dialog A0;
    private boolean B0;
    private boolean C0;
    private boolean D0;

    /* renamed from: p0  reason: collision with root package name */
    private Handler f5590p0;

    /* renamed from: y0  reason: collision with root package name */
    private boolean f5599y0;

    /* renamed from: q0  reason: collision with root package name */
    private Runnable f5591q0 = new a();

    /* renamed from: r0  reason: collision with root package name */
    private DialogInterface.OnCancelListener f5592r0 = new b();

    /* renamed from: s0  reason: collision with root package name */
    private DialogInterface.OnDismissListener f5593s0 = new DialogInterface$OnDismissListenerC0055c();

    /* renamed from: t0  reason: collision with root package name */
    private int f5594t0 = 0;

    /* renamed from: u0  reason: collision with root package name */
    private int f5595u0 = 0;

    /* renamed from: v0  reason: collision with root package name */
    private boolean f5596v0 = true;

    /* renamed from: w0  reason: collision with root package name */
    private boolean f5597w0 = true;

    /* renamed from: x0  reason: collision with root package name */
    private int f5598x0 = -1;

    /* renamed from: z0  reason: collision with root package name */
    private androidx.lifecycle.q<androidx.lifecycle.j> f5600z0 = new d();
    private boolean E0 = false;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Runnable {
        a() {
        }

        @Override // java.lang.Runnable
        @SuppressLint({"SyntheticAccessor"})
        public void run() {
            c.this.f5593s0.onDismiss(c.this.A0);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class b implements DialogInterface.OnCancelListener {
        b() {
        }

        @Override // android.content.DialogInterface.OnCancelListener
        @SuppressLint({"SyntheticAccessor"})
        public void onCancel(DialogInterface dialogInterface) {
            if (c.this.A0 != null) {
                c cVar = c.this;
                cVar.onCancel(cVar.A0);
            }
        }
    }

    /* renamed from: androidx.fragment.app.c$c  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class DialogInterface$OnDismissListenerC0055c implements DialogInterface.OnDismissListener {
        DialogInterface$OnDismissListenerC0055c() {
        }

        @Override // android.content.DialogInterface.OnDismissListener
        @SuppressLint({"SyntheticAccessor"})
        public void onDismiss(DialogInterface dialogInterface) {
            if (c.this.A0 != null) {
                c cVar = c.this;
                cVar.onDismiss(cVar.A0);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class d implements androidx.lifecycle.q<androidx.lifecycle.j> {
        d() {
        }

        @Override // androidx.lifecycle.q
        @SuppressLint({"SyntheticAccessor"})
        /* renamed from: a */
        public void b(androidx.lifecycle.j jVar) {
            if (jVar == null || !c.this.f5597w0) {
                return;
            }
            View m12 = c.this.m1();
            if (m12.getParent() != null) {
                throw new IllegalStateException("DialogFragment can not be attached to a container view");
            }
            if (c.this.A0 != null) {
                if (FragmentManager.F0(3)) {
                    Log.d("FragmentManager", "DialogFragment " + this + " setting the content view on " + c.this.A0);
                }
                c.this.A0.setContentView(m12);
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class e extends androidx.fragment.app.e {

        /* renamed from: a  reason: collision with root package name */
        final /* synthetic */ androidx.fragment.app.e f5605a;

        e(androidx.fragment.app.e eVar) {
            this.f5605a = eVar;
        }

        @Override // androidx.fragment.app.e
        public View c(int i8) {
            return this.f5605a.d() ? this.f5605a.c(i8) : c.this.S1(i8);
        }

        @Override // androidx.fragment.app.e
        public boolean d() {
            return this.f5605a.d() || c.this.T1();
        }
    }

    private void O1(boolean z4, boolean z8) {
        if (this.C0) {
            return;
        }
        this.C0 = true;
        this.D0 = false;
        Dialog dialog = this.A0;
        if (dialog != null) {
            dialog.setOnDismissListener(null);
            this.A0.dismiss();
            if (!z8) {
                if (Looper.myLooper() == this.f5590p0.getLooper()) {
                    onDismiss(this.A0);
                } else {
                    this.f5590p0.post(this.f5591q0);
                }
            }
        }
        this.B0 = true;
        if (this.f5598x0 >= 0) {
            E().W0(this.f5598x0, 1);
            this.f5598x0 = -1;
            return;
        }
        r l8 = E().l();
        l8.p(this);
        if (z4) {
            l8.j();
        } else {
            l8.i();
        }
    }

    private void U1(Bundle bundle) {
        if (this.f5597w0 && !this.E0) {
            try {
                this.f5599y0 = true;
                Dialog R1 = R1(bundle);
                this.A0 = R1;
                if (this.f5597w0) {
                    X1(R1, this.f5594t0);
                    Context context = getContext();
                    if (context instanceof Activity) {
                        this.A0.setOwnerActivity((Activity) context);
                    }
                    this.A0.setCancelable(this.f5596v0);
                    this.A0.setOnCancelListener(this.f5592r0);
                    this.A0.setOnDismissListener(this.f5593s0);
                    this.E0 = true;
                } else {
                    this.A0 = null;
                }
            } finally {
                this.f5599y0 = false;
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void I0(Bundle bundle) {
        super.I0(bundle);
        Dialog dialog = this.A0;
        if (dialog != null) {
            Bundle onSaveInstanceState = dialog.onSaveInstanceState();
            onSaveInstanceState.putBoolean("android:dialogShowing", false);
            bundle.putBundle("android:savedDialogState", onSaveInstanceState);
        }
        int i8 = this.f5594t0;
        if (i8 != 0) {
            bundle.putInt("android:style", i8);
        }
        int i9 = this.f5595u0;
        if (i9 != 0) {
            bundle.putInt("android:theme", i9);
        }
        boolean z4 = this.f5596v0;
        if (!z4) {
            bundle.putBoolean("android:cancelable", z4);
        }
        boolean z8 = this.f5597w0;
        if (!z8) {
            bundle.putBoolean("android:showsDialog", z8);
        }
        int i10 = this.f5598x0;
        if (i10 != -1) {
            bundle.putInt("android:backStackId", i10);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void K0(Bundle bundle) {
        Bundle bundle2;
        super.K0(bundle);
        if (this.A0 == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.A0.onRestoreInstanceState(bundle2);
    }

    public void M1() {
        O1(false, false);
    }

    public void N1() {
        O1(true, false);
    }

    public Dialog P1() {
        return this.A0;
    }

    public int Q1() {
        return this.f5595u0;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.Fragment
    public void R0(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Bundle bundle2;
        super.R0(layoutInflater, viewGroup, bundle);
        if (this.T != null || this.A0 == null || bundle == null || (bundle2 = bundle.getBundle("android:savedDialogState")) == null) {
            return;
        }
        this.A0.onRestoreInstanceState(bundle2);
    }

    public Dialog R1(Bundle bundle) {
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "onCreateDialog called for DialogFragment " + this);
        }
        return new Dialog(l1(), Q1());
    }

    View S1(int i8) {
        Dialog dialog = this.A0;
        if (dialog != null) {
            return dialog.findViewById(i8);
        }
        return null;
    }

    boolean T1() {
        return this.E0;
    }

    public final Dialog V1() {
        Dialog P1 = P1();
        if (P1 != null) {
            return P1;
        }
        throw new IllegalStateException("DialogFragment " + this + " does not have a Dialog.");
    }

    public void W1(boolean z4) {
        this.f5597w0 = z4;
    }

    public void X1(Dialog dialog, int i8) {
        if (i8 != 1 && i8 != 2) {
            if (i8 != 3) {
                return;
            }
            Window window = dialog.getWindow();
            if (window != null) {
                window.addFlags(24);
            }
        }
        dialog.requestWindowFeature(1);
    }

    public void Y1(FragmentManager fragmentManager, String str) {
        this.C0 = false;
        this.D0 = true;
        r l8 = fragmentManager.l();
        l8.d(this, str);
        l8.i();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // androidx.fragment.app.Fragment
    public androidx.fragment.app.e f() {
        return new e(super.f());
    }

    @Override // androidx.fragment.app.Fragment
    public void n0(Context context) {
        super.n0(context);
        V().i(this.f5600z0);
        if (this.D0) {
            return;
        }
        this.C0 = false;
    }

    @Override // android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.f5590p0 = new Handler();
        this.f5597w0 = this.E == 0;
        if (bundle != null) {
            this.f5594t0 = bundle.getInt("android:style", 0);
            this.f5595u0 = bundle.getInt("android:theme", 0);
            this.f5596v0 = bundle.getBoolean("android:cancelable", true);
            this.f5597w0 = bundle.getBoolean("android:showsDialog", this.f5597w0);
            this.f5598x0 = bundle.getInt("android:backStackId", -1);
        }
    }

    @Override // android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        if (this.B0) {
            return;
        }
        if (FragmentManager.F0(3)) {
            Log.d("FragmentManager", "onDismiss called for DialogFragment " + this);
        }
        O1(true, true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        Dialog dialog = this.A0;
        if (dialog != null) {
            this.B0 = false;
            dialog.show();
            View decorView = this.A0.getWindow().getDecorView();
            k0.a(decorView, this);
            l0.a(decorView, this);
            s1.e.a(decorView, this);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        Dialog dialog = this.A0;
        if (dialog != null) {
            dialog.hide();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void v0() {
        super.v0();
        Dialog dialog = this.A0;
        if (dialog != null) {
            this.B0 = true;
            dialog.setOnDismissListener(null);
            this.A0.dismiss();
            if (!this.C0) {
                onDismiss(this.A0);
            }
            this.A0 = null;
            this.E0 = false;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void w0() {
        super.w0();
        if (!this.D0 && !this.C0) {
            this.C0 = true;
        }
        V().m(this.f5600z0);
    }

    @Override // androidx.fragment.app.Fragment
    public LayoutInflater x0(Bundle bundle) {
        StringBuilder sb;
        String str;
        LayoutInflater x02 = super.x0(bundle);
        if (this.f5597w0 && !this.f5599y0) {
            U1(bundle);
            if (FragmentManager.F0(2)) {
                Log.d("FragmentManager", "get layout inflater for DialogFragment " + this + " from dialog context");
            }
            Dialog dialog = this.A0;
            return dialog != null ? x02.cloneInContext(dialog.getContext()) : x02;
        }
        if (FragmentManager.F0(2)) {
            String str2 = "getting layout inflater for DialogFragment " + this;
            if (this.f5597w0) {
                sb = new StringBuilder();
                str = "mCreatingDialog = true: ";
            } else {
                sb = new StringBuilder();
                str = "mShowsDialog = false: ";
            }
            sb.append(str);
            sb.append(str2);
            Log.d("FragmentManager", sb.toString());
        }
        return x02;
    }
}
