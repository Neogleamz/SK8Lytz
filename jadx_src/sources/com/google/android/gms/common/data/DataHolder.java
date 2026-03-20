package com.google.android.gms.common.data;

import android.database.CursorWindow;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.android.gms.common.annotation.KeepName;
import com.google.android.gms.common.internal.safeparcel.AbstractSafeParcelable;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.HashMap;
@KeepName
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class DataHolder extends AbstractSafeParcelable implements Closeable {
    public static final Parcelable.Creator<DataHolder> CREATOR = new c();

    /* renamed from: l  reason: collision with root package name */
    private static final a f11732l = new b(new String[0], null);

    /* renamed from: a  reason: collision with root package name */
    final int f11733a;

    /* renamed from: b  reason: collision with root package name */
    private final String[] f11734b;

    /* renamed from: c  reason: collision with root package name */
    Bundle f11735c;

    /* renamed from: d  reason: collision with root package name */
    private final CursorWindow[] f11736d;

    /* renamed from: e  reason: collision with root package name */
    private final int f11737e;

    /* renamed from: f  reason: collision with root package name */
    private final Bundle f11738f;

    /* renamed from: g  reason: collision with root package name */
    int[] f11739g;

    /* renamed from: h  reason: collision with root package name */
    int f11740h;

    /* renamed from: j  reason: collision with root package name */
    boolean f11741j = false;

    /* renamed from: k  reason: collision with root package name */
    private boolean f11742k = true;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        private final String[] f11743a;

        /* renamed from: b  reason: collision with root package name */
        private final ArrayList f11744b = new ArrayList();

        /* renamed from: c  reason: collision with root package name */
        private final HashMap f11745c = new HashMap();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public DataHolder(int i8, String[] strArr, CursorWindow[] cursorWindowArr, int i9, Bundle bundle) {
        this.f11733a = i8;
        this.f11734b = strArr;
        this.f11736d = cursorWindowArr;
        this.f11737e = i9;
        this.f11738f = bundle;
    }

    public final void Z() {
        this.f11735c = new Bundle();
        int i8 = 0;
        int i9 = 0;
        while (true) {
            String[] strArr = this.f11734b;
            if (i9 >= strArr.length) {
                break;
            }
            this.f11735c.putInt(strArr[i9], i9);
            i9++;
        }
        this.f11739g = new int[this.f11736d.length];
        int i10 = 0;
        while (true) {
            CursorWindow[] cursorWindowArr = this.f11736d;
            if (i8 >= cursorWindowArr.length) {
                this.f11740h = i10;
                return;
            }
            this.f11739g[i8] = i10;
            i10 += this.f11736d[i8].getNumRows() - (i10 - cursorWindowArr[i8].getStartPosition());
            i8++;
        }
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        synchronized (this) {
            if (!this.f11741j) {
                this.f11741j = true;
                int i8 = 0;
                while (true) {
                    CursorWindow[] cursorWindowArr = this.f11736d;
                    if (i8 >= cursorWindowArr.length) {
                        break;
                    }
                    cursorWindowArr[i8].close();
                    i8++;
                }
            }
        }
    }

    protected final void finalize() {
        try {
            if (this.f11742k && this.f11736d.length > 0 && !isClosed()) {
                close();
                String obj = toString();
                Log.e("DataBuffer", "Internal data leak within a DataBuffer object detected!  Be sure to explicitly call release() on all DataBuffer extending objects when you are done with them. (internal object: " + obj + ")");
            }
        } finally {
            super.finalize();
        }
    }

    public boolean isClosed() {
        boolean z4;
        synchronized (this) {
            z4 = this.f11741j;
        }
        return z4;
    }

    public Bundle t() {
        return this.f11738f;
    }

    public int u() {
        return this.f11737e;
    }

    @Override // android.os.Parcelable
    public final void writeToParcel(Parcel parcel, int i8) {
        int a9 = o6.a.a(parcel);
        o6.a.s(parcel, 1, this.f11734b, false);
        o6.a.u(parcel, 2, this.f11736d, i8, false);
        o6.a.l(parcel, 3, u());
        o6.a.e(parcel, 4, t(), false);
        o6.a.l(parcel, 1000, this.f11733a);
        o6.a.b(parcel, a9);
        if ((i8 & 1) != 0) {
            close();
        }
    }
}
