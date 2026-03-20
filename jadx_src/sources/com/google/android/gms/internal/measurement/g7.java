package com.google.android.gms.internal.measurement;

import com.google.android.gms.internal.measurement.f7;
import com.google.android.gms.internal.measurement.g7;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class g7<MessageType extends g7<MessageType, BuilderType>, BuilderType extends f7<MessageType, BuilderType>> implements ia {
    protected int zza = 0;

    /* JADX INFO: Access modifiers changed from: protected */
    public static <T> void h(Iterable<T> iterable, List<? super T> list) {
        a9.e(iterable);
        if (iterable instanceof o9) {
            List<?> d8 = ((o9) iterable).d();
            o9 o9Var = (o9) list;
            int size = list.size();
            for (Object obj : d8) {
                if (obj == null) {
                    String str = "Element at index " + (o9Var.size() - size) + " is null.";
                    for (int size2 = o9Var.size() - 1; size2 >= size; size2--) {
                        o9Var.remove(size2);
                    }
                    throw new NullPointerException(str);
                } else if (obj instanceof zzij) {
                    o9Var.r0((zzij) obj);
                } else {
                    o9Var.add((String) obj);
                }
            }
        } else if (iterable instanceof sa) {
            list.addAll((Collection) iterable);
        } else {
            if ((list instanceof ArrayList) && (iterable instanceof Collection)) {
                ((ArrayList) list).ensureCapacity(list.size() + ((Collection) iterable).size());
            }
            int size3 = list.size();
            for (T t8 : iterable) {
                if (t8 == null) {
                    String str2 = "Element at index " + (list.size() - size3) + " is null.";
                    for (int size4 = list.size() - 1; size4 >= size3; size4--) {
                        list.remove(size4);
                    }
                    throw new NullPointerException(str2);
                }
                list.add(t8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int a(xa xaVar) {
        int i8 = i();
        if (i8 == -1) {
            int a9 = xaVar.a(this);
            l(a9);
            return a9;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public int i() {
        throw new UnsupportedOperationException();
    }

    @Override // com.google.android.gms.internal.measurement.ia
    public final zzij j() {
        try {
            v7 y8 = zzij.y(f());
            b(y8.b());
            return y8.a();
        } catch (IOException e8) {
            String name = getClass().getName();
            throw new RuntimeException("Serializing " + name + " to a ByteString threw an IOException (should never happen).", e8);
        }
    }

    public final byte[] k() {
        try {
            byte[] bArr = new byte[f()];
            zzja H = zzja.H(bArr);
            b(H);
            H.I();
            return bArr;
        } catch (IOException e8) {
            String name = getClass().getName();
            throw new RuntimeException("Serializing " + name + " to a byte array threw an IOException (should never happen).", e8);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void l(int i8) {
        throw new UnsupportedOperationException();
    }
}
