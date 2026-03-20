package com.google.android.gms.common.api.internal;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Keep;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class LifecycleCallback {

    /* renamed from: a  reason: collision with root package name */
    protected final l6.f f11595a;

    /* JADX INFO: Access modifiers changed from: protected */
    public LifecycleCallback(l6.f fVar) {
        this.f11595a = fVar;
    }

    public static l6.f c(Activity activity) {
        return d(new l6.e(activity));
    }

    protected static l6.f d(l6.e eVar) {
        if (eVar.d()) {
            return l6.i0.L1(eVar.b());
        }
        if (eVar.c()) {
            return l6.g0.f(eVar.a());
        }
        throw new IllegalArgumentException("Can't get fragment for unexpected activity.");
    }

    @Keep
    private static l6.f getChimeraLifecycleFragmentImpl(l6.e eVar) {
        throw new IllegalStateException("Method not available in SDK.");
    }

    public void a(String str, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    public Activity b() {
        Activity c9 = this.f11595a.c();
        n6.j.l(c9);
        return c9;
    }

    public void e(int i8, int i9, Intent intent) {
    }

    public void f(Bundle bundle) {
    }

    public void g() {
    }

    public void h() {
    }

    public void i(Bundle bundle) {
    }

    public void j() {
    }

    public void k() {
    }
}
