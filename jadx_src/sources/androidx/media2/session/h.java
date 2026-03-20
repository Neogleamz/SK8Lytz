package androidx.media2.session;

import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import androidx.media.MediaBrowserServiceCompat;
import androidx.media2.common.MediaItem;
import androidx.media2.common.MediaParcelUtils;
import androidx.media2.common.ParcelImplListSlice;
import androidx.versionedparcelable.ParcelImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class h {

    /* renamed from: a  reason: collision with root package name */
    public static final MediaBrowserServiceCompat.e f6264a = new MediaBrowserServiceCompat.e("androidx.media2.session.MediaLibraryService", null);

    /* renamed from: b  reason: collision with root package name */
    public static final Executor f6265b = new a();

    /* renamed from: c  reason: collision with root package name */
    private static final Map<String, String> f6266c;

    /* renamed from: d  reason: collision with root package name */
    private static final Map<String, String> f6267d;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Executor {
        a() {
        }

        @Override // java.util.concurrent.Executor
        public void execute(Runnable runnable) {
            runnable.run();
        }
    }

    static {
        HashMap hashMap = new HashMap();
        f6266c = hashMap;
        f6267d = new HashMap();
        hashMap.put("android.media.metadata.ADVERTISEMENT", "androidx.media2.metadata.ADVERTISEMENT");
        hashMap.put("android.media.metadata.BT_FOLDER_TYPE", "androidx.media2.metadata.BROWSABLE");
        hashMap.put("android.media.metadata.DOWNLOAD_STATUS", "androidx.media2.metadata.DOWNLOAD_STATUS");
        for (Map.Entry entry : hashMap.entrySet()) {
            Map<String, String> map = f6267d;
            if (map.containsKey(entry.getValue())) {
                throw new RuntimeException("Shouldn't map to the same value");
            }
            map.put((String) entry.getValue(), (String) entry.getKey());
        }
    }

    public static ParcelImplListSlice a(List<MediaItem> list) {
        if (list == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < list.size(); i8++) {
            MediaItem mediaItem = list.get(i8);
            if (mediaItem != null) {
                arrayList.add(MediaParcelUtils.b(mediaItem));
            }
        }
        return new ParcelImplListSlice(arrayList);
    }

    public static List<MediaItem> b(ParcelImplListSlice parcelImplListSlice) {
        if (parcelImplListSlice == null) {
            return null;
        }
        List<ParcelImpl> a9 = parcelImplListSlice.a();
        ArrayList arrayList = new ArrayList();
        for (int i8 = 0; i8 < a9.size(); i8++) {
            ParcelImpl parcelImpl = a9.get(i8);
            if (parcelImpl != null) {
                arrayList.add((MediaItem) MediaParcelUtils.a(parcelImpl));
            }
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean c(Bundle bundle) {
        Parcel obtain = Parcel.obtain();
        try {
            obtain.writeBundle(bundle);
            obtain.setDataPosition(0);
            Bundle readBundle = obtain.readBundle(null);
            for (String str : readBundle.keySet()) {
                readBundle.get(str);
            }
            return false;
        } catch (BadParcelableException e8) {
            Log.d("MediaUtils", "Custom parcelables are not allowed", e8);
            return true;
        } finally {
            obtain.recycle();
        }
    }

    public static MediaItem d(MediaItem mediaItem) {
        return (mediaItem == null || mediaItem.getClass() == MediaItem.class) ? mediaItem : new MediaItem.a().d(mediaItem.h()).b(mediaItem.e()).c(mediaItem.g()).a();
    }
}
