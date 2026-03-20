package com.google.android.exoplayer2.upstream;

import a6.h;
import a6.i;
import a6.x;
import android.net.Uri;
import b6.l0;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.exoplayer2.upstream.a;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d<T> implements Loader.e {

    /* renamed from: a  reason: collision with root package name */
    public final long f10974a;

    /* renamed from: b  reason: collision with root package name */
    public final com.google.android.exoplayer2.upstream.a f10975b;

    /* renamed from: c  reason: collision with root package name */
    public final int f10976c;

    /* renamed from: d  reason: collision with root package name */
    private final x f10977d;

    /* renamed from: e  reason: collision with root package name */
    private final a<? extends T> f10978e;

    /* renamed from: f  reason: collision with root package name */
    private volatile T f10979f;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a<T> {
        T a(Uri uri, InputStream inputStream);
    }

    public d(h hVar, Uri uri, int i8, a<? extends T> aVar) {
        this(hVar, new a.b().i(uri).b(1).a(), i8, aVar);
    }

    public d(h hVar, com.google.android.exoplayer2.upstream.a aVar, int i8, a<? extends T> aVar2) {
        this.f10977d = new x(hVar);
        this.f10975b = aVar;
        this.f10976c = i8;
        this.f10978e = aVar2;
        this.f10974a = h5.h.a();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void a() {
        this.f10977d.o();
        i iVar = new i(this.f10977d, this.f10975b);
        try {
            iVar.b();
            this.f10979f = this.f10978e.a((Uri) b6.a.e(this.f10977d.v()), iVar);
        } finally {
            l0.n(iVar);
        }
    }

    public long b() {
        return this.f10977d.l();
    }

    @Override // com.google.android.exoplayer2.upstream.Loader.e
    public final void c() {
    }

    public Map<String, List<String>> d() {
        return this.f10977d.n();
    }

    public final T e() {
        return this.f10979f;
    }

    public Uri f() {
        return this.f10977d.m();
    }
}
