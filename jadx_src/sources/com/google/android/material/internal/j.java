package com.google.android.material.internal;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextPaint;
import java.lang.ref.WeakReference;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class j {

    /* renamed from: c  reason: collision with root package name */
    private float f18142c;

    /* renamed from: f  reason: collision with root package name */
    private u7.d f18145f;

    /* renamed from: a  reason: collision with root package name */
    private final TextPaint f18140a = new TextPaint(1);

    /* renamed from: b  reason: collision with root package name */
    private final u7.f f18141b = new a();

    /* renamed from: d  reason: collision with root package name */
    private boolean f18143d = true;

    /* renamed from: e  reason: collision with root package name */
    private WeakReference<b> f18144e = new WeakReference<>(null);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a extends u7.f {
        a() {
        }

        @Override // u7.f
        public void a(int i8) {
            j.this.f18143d = true;
            b bVar = (b) j.this.f18144e.get();
            if (bVar != null) {
                bVar.a();
            }
        }

        @Override // u7.f
        public void b(Typeface typeface, boolean z4) {
            if (z4) {
                return;
            }
            j.this.f18143d = true;
            b bVar = (b) j.this.f18144e.get();
            if (bVar != null) {
                bVar.a();
            }
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a();

        int[] getState();

        boolean onStateChange(int[] iArr);
    }

    public j(b bVar) {
        g(bVar);
    }

    private float c(CharSequence charSequence) {
        if (charSequence == null) {
            return 0.0f;
        }
        return this.f18140a.measureText(charSequence, 0, charSequence.length());
    }

    public u7.d d() {
        return this.f18145f;
    }

    public TextPaint e() {
        return this.f18140a;
    }

    public float f(String str) {
        if (this.f18143d) {
            float c9 = c(str);
            this.f18142c = c9;
            this.f18143d = false;
            return c9;
        }
        return this.f18142c;
    }

    public void g(b bVar) {
        this.f18144e = new WeakReference<>(bVar);
    }

    public void h(u7.d dVar, Context context) {
        if (this.f18145f != dVar) {
            this.f18145f = dVar;
            if (dVar != null) {
                dVar.k(context, this.f18140a, this.f18141b);
                b bVar = this.f18144e.get();
                if (bVar != null) {
                    this.f18140a.drawableState = bVar.getState();
                }
                dVar.j(context, this.f18140a, this.f18141b);
                this.f18143d = true;
            }
            b bVar2 = this.f18144e.get();
            if (bVar2 != null) {
                bVar2.a();
                bVar2.onStateChange(bVar2.getState());
            }
        }
    }

    public void i(boolean z4) {
        this.f18143d = z4;
    }

    public void j(Context context) {
        this.f18145f.j(context, this.f18140a, this.f18141b);
    }
}
