package androidx.core.app;

import android.app.Notification;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import androidx.core.app.k;
import androidx.core.graphics.drawable.IconCompat;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class m {

    /* renamed from: b  reason: collision with root package name */
    private static Field f4558b;

    /* renamed from: c  reason: collision with root package name */
    private static boolean f4559c;

    /* renamed from: a  reason: collision with root package name */
    private static final Object f4557a = new Object();

    /* renamed from: d  reason: collision with root package name */
    private static final Object f4560d = new Object();

    public static SparseArray<Bundle> a(List<Bundle> list) {
        int size = list.size();
        SparseArray<Bundle> sparseArray = null;
        for (int i8 = 0; i8 < size; i8++) {
            Bundle bundle = list.get(i8);
            if (bundle != null) {
                if (sparseArray == null) {
                    sparseArray = new SparseArray<>();
                }
                sparseArray.put(i8, bundle);
            }
        }
        return sparseArray;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Bundle b(k.a aVar) {
        Bundle bundle = new Bundle();
        IconCompat f5 = aVar.f();
        bundle.putInt("icon", f5 != null ? f5.r() : 0);
        bundle.putCharSequence("title", aVar.j());
        bundle.putParcelable("actionIntent", aVar.a());
        Bundle bundle2 = aVar.d() != null ? new Bundle(aVar.d()) : new Bundle();
        bundle2.putBoolean("android.support.allowGeneratedReplies", aVar.b());
        bundle.putBundle("extras", bundle2);
        bundle.putParcelableArray("remoteInputs", e(aVar.g()));
        bundle.putBoolean("showsUserInterface", aVar.i());
        bundle.putInt("semanticAction", aVar.h());
        return bundle;
    }

    public static Bundle c(Notification notification) {
        String str;
        String str2;
        synchronized (f4557a) {
            if (f4559c) {
                return null;
            }
            try {
                if (f4558b == null) {
                    Field declaredField = Notification.class.getDeclaredField("extras");
                    if (!Bundle.class.isAssignableFrom(declaredField.getType())) {
                        Log.e("NotificationCompat", "Notification.extras field is not of type Bundle");
                        f4559c = true;
                        return null;
                    }
                    declaredField.setAccessible(true);
                    f4558b = declaredField;
                }
                Bundle bundle = (Bundle) f4558b.get(notification);
                if (bundle == null) {
                    bundle = new Bundle();
                    f4558b.set(notification, bundle);
                }
                return bundle;
            } catch (IllegalAccessException e8) {
                e = e8;
                str = "NotificationCompat";
                str2 = "Unable to access notification extras";
                Log.e(str, str2, e);
                f4559c = true;
                return null;
            } catch (NoSuchFieldException e9) {
                e = e9;
                str = "NotificationCompat";
                str2 = "Unable to access notification extras";
                Log.e(str, str2, e);
                f4559c = true;
                return null;
            }
        }
    }

    private static Bundle d(q qVar) {
        Bundle bundle = new Bundle();
        bundle.putString("resultKey", qVar.j());
        bundle.putCharSequence("label", qVar.i());
        bundle.putCharSequenceArray("choices", qVar.e());
        bundle.putBoolean("allowFreeFormInput", qVar.c());
        bundle.putBundle("extras", qVar.h());
        Set<String> d8 = qVar.d();
        if (d8 != null && !d8.isEmpty()) {
            ArrayList<String> arrayList = new ArrayList<>(d8.size());
            for (String str : d8) {
                arrayList.add(str);
            }
            bundle.putStringArrayList("allowedDataTypes", arrayList);
        }
        return bundle;
    }

    private static Bundle[] e(q[] qVarArr) {
        if (qVarArr == null) {
            return null;
        }
        Bundle[] bundleArr = new Bundle[qVarArr.length];
        for (int i8 = 0; i8 < qVarArr.length; i8++) {
            bundleArr[i8] = d(qVarArr[i8]);
        }
        return bundleArr;
    }

    public static Bundle f(Notification.Builder builder, k.a aVar) {
        IconCompat f5 = aVar.f();
        builder.addAction(f5 != null ? f5.r() : 0, aVar.j(), aVar.a());
        Bundle bundle = new Bundle(aVar.d());
        if (aVar.g() != null) {
            bundle.putParcelableArray("android.support.remoteInputs", e(aVar.g()));
        }
        if (aVar.c() != null) {
            bundle.putParcelableArray("android.support.dataRemoteInputs", e(aVar.c()));
        }
        bundle.putBoolean("android.support.allowGeneratedReplies", aVar.b());
        return bundle;
    }
}
