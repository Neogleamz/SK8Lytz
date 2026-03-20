package h4;

import android.util.SparseArray;
import com.google.android.datatransport.Priority;
import java.util.HashMap;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class a {

    /* renamed from: a  reason: collision with root package name */
    private static SparseArray<Priority> f20265a = new SparseArray<>();

    /* renamed from: b  reason: collision with root package name */
    private static HashMap<Priority, Integer> f20266b;

    static {
        HashMap<Priority, Integer> hashMap = new HashMap<>();
        f20266b = hashMap;
        hashMap.put(Priority.DEFAULT, 0);
        f20266b.put(Priority.VERY_LOW, 1);
        f20266b.put(Priority.HIGHEST, 2);
        for (Priority priority : f20266b.keySet()) {
            f20265a.append(f20266b.get(priority).intValue(), priority);
        }
    }

    public static int a(Priority priority) {
        Integer num = f20266b.get(priority);
        if (num != null) {
            return num.intValue();
        }
        throw new IllegalStateException("PriorityMapping is missing known Priority value " + priority);
    }

    public static Priority b(int i8) {
        Priority priority = f20265a.get(i8);
        if (priority != null) {
            return priority;
        }
        throw new IllegalArgumentException("Unknown Priority for value " + i8);
    }
}
