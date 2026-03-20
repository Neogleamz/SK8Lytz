package com.google.android.gms.internal.mlkit_vision_barcode;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class z1 {
    /* JADX INFO: Access modifiers changed from: package-private */
    public static int a(Set set) {
        Iterator it = set.iterator();
        int i8 = 0;
        while (it.hasNext()) {
            Object next = it.next();
            i8 += next != null ? next.hashCode() : 0;
        }
        return i8;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean b(Set set, Object obj) {
        if (set == obj) {
            return true;
        }
        if (obj instanceof Set) {
            Set set2 = (Set) obj;
            try {
                if (set.size() == set2.size()) {
                    if (set.containsAll(set2)) {
                        return true;
                    }
                }
            } catch (ClassCastException | NullPointerException unused) {
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(Set set, Collection collection) {
        Objects.requireNonNull(collection);
        if (collection instanceof q1) {
            collection = ((q1) collection).zza();
        }
        if (!(collection instanceof Set) || collection.size() <= set.size()) {
            return d(set, collection.iterator());
        }
        Iterator it = set.iterator();
        boolean z4 = false;
        while (it.hasNext()) {
            if (collection.contains(it.next())) {
                it.remove();
                z4 = true;
            }
        }
        return z4;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean d(Set set, Iterator it) {
        boolean z4 = false;
        while (it.hasNext()) {
            z4 |= set.remove(it.next());
        }
        return z4;
    }
}
