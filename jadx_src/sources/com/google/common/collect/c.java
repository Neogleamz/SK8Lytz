package com.google.common.collect;

import java.util.NoSuchElementException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class c<T> extends d3<T> {

    /* renamed from: a  reason: collision with root package name */
    private b f19179a = b.NOT_READY;

    /* renamed from: b  reason: collision with root package name */
    private T f19180b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static /* synthetic */ class a {

        /* renamed from: a  reason: collision with root package name */
        static final /* synthetic */ int[] f19181a;

        static {
            int[] iArr = new int[b.values().length];
            f19181a = iArr;
            try {
                iArr[b.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f19181a[b.READY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public enum b {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    private boolean c() {
        this.f19179a = b.FAILED;
        this.f19180b = a();
        if (this.f19179a != b.DONE) {
            this.f19179a = b.READY;
            return true;
        }
        return false;
    }

    protected abstract T a();

    /* JADX INFO: Access modifiers changed from: protected */
    public final T b() {
        this.f19179a = b.DONE;
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        com.google.common.base.l.s(this.f19179a != b.FAILED);
        int i8 = a.f19181a[this.f19179a.ordinal()];
        if (i8 != 1) {
            if (i8 != 2) {
                return c();
            }
            return true;
        }
        return false;
    }

    @Override // java.util.Iterator
    public final T next() {
        if (hasNext()) {
            this.f19179a = b.NOT_READY;
            T t8 = (T) u1.a(this.f19180b);
            this.f19180b = null;
            return t8;
        }
        throw new NoSuchElementException();
    }
}
