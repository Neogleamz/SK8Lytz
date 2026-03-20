package com.ajinasokan.flutter_fgbg;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.i;
import androidx.lifecycle.r;
import androidx.lifecycle.t;
import io.flutter.plugin.common.d;
import yf.a;
import zf.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class FlutterFGBGPlugin implements a, zf.a, i, d.d {

    /* renamed from: a  reason: collision with root package name */
    d.b f8641a;

    public void b(Object obj, d.b bVar) {
        this.f8641a = bVar;
    }

    public void d(Object obj) {
        this.f8641a = null;
    }

    @r(Lifecycle.Event.ON_STOP)
    void onAppBackgrounded() {
        d.b bVar = this.f8641a;
        if (bVar != null) {
            bVar.success("background");
        }
    }

    @r(Lifecycle.Event.ON_START)
    void onAppForegrounded() {
        d.b bVar = this.f8641a;
        if (bVar != null) {
            bVar.success("foreground");
        }
    }

    public void onAttachedToActivity(c cVar) {
        t.l().getLifecycle().a(this);
    }

    public void onAttachedToEngine(a.b bVar) {
        new d(bVar.b(), "com.ajinasokan.flutter_fgbg/events").d(this);
    }

    public void onDetachedFromActivity() {
        t.l().getLifecycle().c(this);
    }

    public void onDetachedFromActivityForConfigChanges() {
    }

    public void onDetachedFromEngine(a.b bVar) {
    }

    public void onReattachedToActivityForConfigChanges(c cVar) {
    }
}
