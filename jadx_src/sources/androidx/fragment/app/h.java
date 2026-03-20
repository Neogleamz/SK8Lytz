package androidx.fragment.app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class h<E> extends e {

    /* renamed from: a  reason: collision with root package name */
    private final Activity f5627a;

    /* renamed from: b  reason: collision with root package name */
    private final Context f5628b;

    /* renamed from: c  reason: collision with root package name */
    private final Handler f5629c;

    /* renamed from: d  reason: collision with root package name */
    private final int f5630d;

    /* renamed from: e  reason: collision with root package name */
    final FragmentManager f5631e;

    h(Activity activity, Context context, Handler handler, int i8) {
        this.f5631e = new k();
        this.f5627a = activity;
        this.f5628b = (Context) androidx.core.util.h.i(context, "context == null");
        this.f5629c = (Handler) androidx.core.util.h.i(handler, "handler == null");
        this.f5630d = i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(FragmentActivity fragmentActivity) {
        this(fragmentActivity, fragmentActivity, new Handler(), 0);
    }

    @Override // androidx.fragment.app.e
    public View c(int i8) {
        return null;
    }

    @Override // androidx.fragment.app.e
    public boolean d() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Activity e() {
        return this.f5627a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Context f() {
        return this.f5628b;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Handler g() {
        return this.f5629c;
    }

    public abstract E h();

    public LayoutInflater i() {
        return LayoutInflater.from(this.f5628b);
    }

    @Deprecated
    public void j(Fragment fragment, String[] strArr, int i8) {
    }

    public boolean k(Fragment fragment) {
        return true;
    }

    public boolean l(String str) {
        return false;
    }

    public void m(Fragment fragment, @SuppressLint({"UnknownNullness"}) Intent intent, int i8, Bundle bundle) {
        if (i8 != -1) {
            throw new IllegalStateException("Starting activity with a requestCode requires a FragmentActivity host");
        }
        androidx.core.content.a.l(this.f5628b, intent, bundle);
    }

    @Deprecated
    public void n(Fragment fragment, @SuppressLint({"UnknownNullness"}) IntentSender intentSender, int i8, Intent intent, int i9, int i10, int i11, Bundle bundle) {
        if (i8 != -1) {
            throw new IllegalStateException("Starting intent sender with a requestCode requires a FragmentActivity host");
        }
        androidx.core.app.b.y(this.f5627a, intentSender, i8, intent, i9, i10, i11, bundle);
    }

    public void o() {
    }
}
