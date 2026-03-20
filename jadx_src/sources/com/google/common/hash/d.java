package com.google.common.hash;

import com.google.common.hash.e;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class d extends e implements b {
    private static final long serialVersionUID = 7249069246863182397L;

    private void readObject(ObjectInputStream objectInputStream) {
        objectInputStream.defaultReadObject();
        this.f19567c = 0;
        this.f19565a = null;
        this.f19566b = objectInputStream.readLong();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeLong(h());
    }

    @Override // com.google.common.hash.b
    public void a(long j8) {
        int length;
        e.b bVar;
        e.b[] bVarArr = this.f19565a;
        if (bVarArr == null) {
            long j9 = this.f19566b;
            if (c(j9, j9 + j8)) {
                return;
            }
        }
        int[] iArr = e.f19559d.get();
        boolean z4 = true;
        if (iArr != null && bVarArr != null && (length = bVarArr.length) >= 1 && (bVar = bVarArr[(length - 1) & iArr[0]]) != null) {
            long j10 = bVar.f19570a;
            z4 = bVar.a(j10, j10 + j8);
            if (z4) {
                return;
            }
        }
        g(j8, iArr, z4);
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return h();
    }

    @Override // com.google.common.hash.e
    final long e(long j8, long j9) {
        return j8 + j9;
    }

    @Override // java.lang.Number
    public float floatValue() {
        return (float) h();
    }

    public long h() {
        long j8 = this.f19566b;
        e.b[] bVarArr = this.f19565a;
        if (bVarArr != null) {
            for (e.b bVar : bVarArr) {
                if (bVar != null) {
                    j8 += bVar.f19570a;
                }
            }
        }
        return j8;
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) h();
    }

    @Override // java.lang.Number
    public long longValue() {
        return h();
    }

    public String toString() {
        return Long.toString(h());
    }
}
