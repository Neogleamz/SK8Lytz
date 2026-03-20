package androidx.media2.common;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import androidx.versionedparcelable.CustomVersionedParcelable;
import androidx.versionedparcelable.ParcelImpl;
import java.util.ArrayList;
import java.util.Objects;
import y1.b;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class MediaMetadata extends CustomVersionedParcelable {

    /* renamed from: d  reason: collision with root package name */
    static final k0.a<String, Integer> f6119d;

    /* renamed from: a  reason: collision with root package name */
    Bundle f6120a;

    /* renamed from: b  reason: collision with root package name */
    Bundle f6121b;

    /* renamed from: c  reason: collision with root package name */
    ParcelImplListSlice f6122c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class BitmapEntry implements b {

        /* renamed from: a  reason: collision with root package name */
        String f6123a;

        /* renamed from: b  reason: collision with root package name */
        Bitmap f6124b;

        /* JADX INFO: Access modifiers changed from: package-private */
        public BitmapEntry() {
        }

        BitmapEntry(String str, Bitmap bitmap) {
            this.f6123a = str;
            this.f6124b = bitmap;
            int d8 = d(bitmap);
            if (d8 > 262144) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                double sqrt = Math.sqrt(262144.0d / d8);
                int i8 = (int) (width * sqrt);
                int i9 = (int) (height * sqrt);
                Log.i("MediaMetadata", "Scaling large bitmap of " + width + "x" + height + " into " + i8 + "x" + i9);
                this.f6124b = Bitmap.createScaledBitmap(bitmap, i8, i9, true);
            }
        }

        private int d(Bitmap bitmap) {
            return androidx.core.graphics.a.a(bitmap);
        }

        Bitmap c() {
            return this.f6124b;
        }

        String e() {
            return this.f6123a;
        }
    }

    static {
        k0.a<String, Integer> aVar = new k0.a<>();
        f6119d = aVar;
        aVar.put("android.media.metadata.TITLE", 1);
        aVar.put("android.media.metadata.ARTIST", 1);
        aVar.put("android.media.metadata.DURATION", 0);
        aVar.put("android.media.metadata.ALBUM", 1);
        aVar.put("android.media.metadata.AUTHOR", 1);
        aVar.put("android.media.metadata.WRITER", 1);
        aVar.put("android.media.metadata.COMPOSER", 1);
        aVar.put("android.media.metadata.COMPILATION", 1);
        aVar.put("android.media.metadata.DATE", 1);
        aVar.put("android.media.metadata.YEAR", 0);
        aVar.put("android.media.metadata.GENRE", 1);
        aVar.put("android.media.metadata.TRACK_NUMBER", 0);
        aVar.put("android.media.metadata.NUM_TRACKS", 0);
        aVar.put("android.media.metadata.DISC_NUMBER", 0);
        aVar.put("android.media.metadata.ALBUM_ARTIST", 1);
        aVar.put("android.media.metadata.ART", 2);
        aVar.put("android.media.metadata.ART_URI", 1);
        aVar.put("android.media.metadata.ALBUM_ART", 2);
        aVar.put("android.media.metadata.ALBUM_ART_URI", 1);
        aVar.put("android.media.metadata.USER_RATING", 3);
        aVar.put("android.media.metadata.RATING", 3);
        aVar.put("android.media.metadata.DISPLAY_TITLE", 1);
        aVar.put("android.media.metadata.DISPLAY_SUBTITLE", 1);
        aVar.put("android.media.metadata.DISPLAY_DESCRIPTION", 1);
        aVar.put("android.media.metadata.DISPLAY_ICON", 2);
        aVar.put("android.media.metadata.DISPLAY_ICON_URI", 1);
        aVar.put("android.media.metadata.MEDIA_ID", 1);
        aVar.put("android.media.metadata.MEDIA_URI", 1);
        aVar.put("androidx.media2.metadata.RADIO_FREQUENCY", 4);
        aVar.put("androidx.media2.metadata.RADIO_PROGRAM_NAME", 1);
        aVar.put("androidx.media2.metadata.BROWSABLE", 0);
        aVar.put("androidx.media2.metadata.PLAYABLE", 0);
        aVar.put("androidx.media2.metadata.ADVERTISEMENT", 0);
        aVar.put("androidx.media2.metadata.DOWNLOAD_STATUS", 0);
        aVar.put("androidx.media2.metadata.EXTRAS", 5);
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void c() {
        Bundle bundle = this.f6121b;
        if (bundle == null) {
            bundle = new Bundle();
        }
        this.f6120a = bundle;
        ParcelImplListSlice parcelImplListSlice = this.f6122c;
        if (parcelImplListSlice != null) {
            for (ParcelImpl parcelImpl : parcelImplListSlice.a()) {
                BitmapEntry bitmapEntry = (BitmapEntry) MediaParcelUtils.a(parcelImpl);
                this.f6120a.putParcelable(bitmapEntry.e(), bitmapEntry.c());
            }
        }
    }

    @Override // androidx.versionedparcelable.CustomVersionedParcelable
    public void d(boolean z4) {
        synchronized (this.f6120a) {
            if (this.f6121b == null) {
                this.f6121b = new Bundle(this.f6120a);
                ArrayList arrayList = new ArrayList();
                for (String str : this.f6120a.keySet()) {
                    Object obj = this.f6120a.get(str);
                    if (obj instanceof Bitmap) {
                        arrayList.add(MediaParcelUtils.b(new BitmapEntry(str, (Bitmap) obj)));
                        this.f6121b.remove(str);
                    }
                }
                this.f6122c = new ParcelImplListSlice(arrayList);
            }
        }
    }

    public boolean e(String str) {
        Objects.requireNonNull(str, "key shouldn't be null");
        return this.f6120a.containsKey(str);
    }

    public long f(String str) {
        Objects.requireNonNull(str, "key shouldn't be null");
        return this.f6120a.getLong(str, 0L);
    }

    public String g(String str) {
        Objects.requireNonNull(str, "key shouldn't be null");
        CharSequence charSequence = this.f6120a.getCharSequence(str);
        if (charSequence != null) {
            return charSequence.toString();
        }
        return null;
    }

    public String toString() {
        return this.f6120a.toString();
    }
}
