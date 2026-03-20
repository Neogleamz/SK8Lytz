package androidx.core.provider;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.CancellationSignal;
import androidx.core.provider.g;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class d {

    /* renamed from: a  reason: collision with root package name */
    private static final Comparator<byte[]> f4797a = new Comparator() { // from class: androidx.core.provider.c
        @Override // java.util.Comparator
        public final int compare(Object obj, Object obj2) {
            int g8;
            g8 = d.g((byte[]) obj, (byte[]) obj2);
            return g8;
        }
    };

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {
        static Cursor a(ContentResolver contentResolver, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Object obj) {
            return contentResolver.query(uri, strArr, str, strArr2, str2, (CancellationSignal) obj);
        }
    }

    private static List<byte[]> b(Signature[] signatureArr) {
        ArrayList arrayList = new ArrayList();
        for (Signature signature : signatureArr) {
            arrayList.add(signature.toByteArray());
        }
        return arrayList;
    }

    private static boolean c(List<byte[]> list, List<byte[]> list2) {
        if (list.size() != list2.size()) {
            return false;
        }
        for (int i8 = 0; i8 < list.size(); i8++) {
            if (!Arrays.equals(list.get(i8), list2.get(i8))) {
                return false;
            }
        }
        return true;
    }

    private static List<List<byte[]>> d(e eVar, Resources resources) {
        return eVar.b() != null ? eVar.b() : androidx.core.content.res.e.c(resources, eVar.c());
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static g.a e(Context context, e eVar, CancellationSignal cancellationSignal) {
        ProviderInfo f5 = f(context.getPackageManager(), eVar, context.getResources());
        return f5 == null ? g.a.a(1, null) : g.a.a(0, h(context, eVar, f5.authority, cancellationSignal));
    }

    static ProviderInfo f(PackageManager packageManager, e eVar, Resources resources) {
        String e8 = eVar.e();
        ProviderInfo resolveContentProvider = packageManager.resolveContentProvider(e8, 0);
        if (resolveContentProvider == null) {
            throw new PackageManager.NameNotFoundException("No package found for authority: " + e8);
        } else if (!resolveContentProvider.packageName.equals(eVar.f())) {
            throw new PackageManager.NameNotFoundException("Found content provider " + e8 + ", but package was not " + eVar.f());
        } else {
            List<byte[]> b9 = b(packageManager.getPackageInfo(resolveContentProvider.packageName, 64).signatures);
            Collections.sort(b9, f4797a);
            List<List<byte[]>> d8 = d(eVar, resources);
            for (int i8 = 0; i8 < d8.size(); i8++) {
                ArrayList arrayList = new ArrayList(d8.get(i8));
                Collections.sort(arrayList, f4797a);
                if (c(b9, arrayList)) {
                    return resolveContentProvider;
                }
            }
            return null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static /* synthetic */ int g(byte[] bArr, byte[] bArr2) {
        int i8;
        int i9;
        if (bArr.length == bArr2.length) {
            for (int i10 = 0; i10 < bArr.length; i10++) {
                if (bArr[i10] != bArr2[i10]) {
                    i8 = bArr[i10];
                    i9 = bArr2[i10];
                }
            }
            return 0;
        }
        i8 = bArr.length;
        i9 = bArr2.length;
        return i8 - i9;
    }

    static g.b[] h(Context context, e eVar, String str, CancellationSignal cancellationSignal) {
        int i8;
        boolean z4;
        ArrayList arrayList = new ArrayList();
        Uri build = new Uri.Builder().scheme("content").authority(str).build();
        Uri build2 = new Uri.Builder().scheme("content").authority(str).appendPath("file").build();
        Cursor cursor = null;
        try {
            String[] strArr = {"_id", "file_id", "font_ttc_index", "font_variation_settings", "font_weight", "font_italic", "result_code"};
            ContentResolver contentResolver = context.getContentResolver();
            int i9 = 0;
            cursor = Build.VERSION.SDK_INT > 16 ? a.a(contentResolver, build, strArr, "query = ?", new String[]{eVar.g()}, null, cancellationSignal) : contentResolver.query(build, strArr, "query = ?", new String[]{eVar.g()}, null);
            if (cursor != null && cursor.getCount() > 0) {
                int columnIndex = cursor.getColumnIndex("result_code");
                ArrayList arrayList2 = new ArrayList();
                int columnIndex2 = cursor.getColumnIndex("_id");
                int columnIndex3 = cursor.getColumnIndex("file_id");
                int columnIndex4 = cursor.getColumnIndex("font_ttc_index");
                int columnIndex5 = cursor.getColumnIndex("font_weight");
                int columnIndex6 = cursor.getColumnIndex("font_italic");
                while (cursor.moveToNext()) {
                    int i10 = columnIndex != -1 ? cursor.getInt(columnIndex) : i9;
                    int i11 = columnIndex4 != -1 ? cursor.getInt(columnIndex4) : i9;
                    int i12 = i10;
                    Uri withAppendedId = columnIndex3 == -1 ? ContentUris.withAppendedId(build, cursor.getLong(columnIndex2)) : ContentUris.withAppendedId(build2, cursor.getLong(columnIndex3));
                    int i13 = columnIndex5 != -1 ? cursor.getInt(columnIndex5) : 400;
                    if (columnIndex6 == -1 || cursor.getInt(columnIndex6) != 1) {
                        i8 = i12;
                        z4 = false;
                    } else {
                        i8 = i12;
                        z4 = true;
                    }
                    arrayList2.add(g.b.a(withAppendedId, i11, i13, z4, i8));
                    i9 = 0;
                }
                arrayList = arrayList2;
            }
            return (g.b[]) arrayList.toArray(new g.b[0]);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
