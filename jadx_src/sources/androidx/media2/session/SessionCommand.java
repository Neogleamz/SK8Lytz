package androidx.media2.session;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.SparseArray;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class SessionCommand implements y1.b {

    /* renamed from: d  reason: collision with root package name */
    static final SparseArray<List<Integer>> f6203d;

    /* renamed from: e  reason: collision with root package name */
    static final SparseArray<List<Integer>> f6204e;

    /* renamed from: f  reason: collision with root package name */
    static final SparseArray<List<Integer>> f6205f;

    /* renamed from: g  reason: collision with root package name */
    static final SparseArray<List<Integer>> f6206g;

    /* renamed from: h  reason: collision with root package name */
    static final SparseArray<List<Integer>> f6207h;

    /* renamed from: a  reason: collision with root package name */
    int f6208a;

    /* renamed from: b  reason: collision with root package name */
    String f6209b;

    /* renamed from: c  reason: collision with root package name */
    Bundle f6210c;

    static {
        SparseArray<List<Integer>> sparseArray = new SparseArray<>();
        f6203d = sparseArray;
        SparseArray<List<Integer>> sparseArray2 = new SparseArray<>();
        f6204e = sparseArray2;
        sparseArray.put(1, Arrays.asList(Integer.valueOf((int) ModuleDescriptor.MODULE_VERSION), 10001, 10002, 10003, 10004, 11000, 11001, 11002));
        sparseArray2.put(1, Arrays.asList(10005, 10006, 10007, 10008, 10009, 10010, 10011, 10012, 10013, 10014, 10015, 10016, 10017, 10018));
        sparseArray2.put(2, Collections.singletonList(10019));
        SparseArray<List<Integer>> sparseArray3 = new SparseArray<>();
        f6205f = sparseArray3;
        sparseArray3.put(1, Arrays.asList(30000, 30001));
        SparseArray<List<Integer>> sparseArray4 = new SparseArray<>();
        f6206g = sparseArray4;
        sparseArray4.put(1, Arrays.asList(40000, 40001, 40002, 40003, 40010));
        sparseArray4.put(2, Collections.singletonList(40011));
        SparseArray<List<Integer>> sparseArray5 = new SparseArray<>();
        f6207h = sparseArray5;
        sparseArray5.put(1, Arrays.asList(50000, 50001, 50002, 50003, 50004, 50005, 50006));
    }

    public boolean equals(Object obj) {
        if (obj instanceof SessionCommand) {
            SessionCommand sessionCommand = (SessionCommand) obj;
            return this.f6208a == sessionCommand.f6208a && TextUtils.equals(this.f6209b, sessionCommand.f6209b);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(this.f6209b, Integer.valueOf(this.f6208a));
    }
}
