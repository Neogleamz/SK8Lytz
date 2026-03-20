package androidx.media2.common;

import android.media.MediaFormat;
import android.os.Bundle;
import androidx.versionedparcelable.CustomVersionedParcelable;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.Closeable;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public abstract class SessionPlayer implements Closeable {

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class TrackInfo extends CustomVersionedParcelable {

        /* renamed from: a  reason: collision with root package name */
        int f6129a;

        /* renamed from: b  reason: collision with root package name */
        int f6130b;

        /* renamed from: c  reason: collision with root package name */
        MediaFormat f6131c;

        /* renamed from: d  reason: collision with root package name */
        boolean f6132d;

        /* renamed from: e  reason: collision with root package name */
        Bundle f6133e;

        /* renamed from: f  reason: collision with root package name */
        private final Object f6134f = new Object();

        private static void e(String str, MediaFormat mediaFormat, Bundle bundle) {
            if (mediaFormat.containsKey(str)) {
                bundle.putInt(str, mediaFormat.getInteger(str));
            }
        }

        private static void f(String str, MediaFormat mediaFormat, Bundle bundle) {
            if (mediaFormat.containsKey(str)) {
                bundle.putString(str, mediaFormat.getString(str));
            }
        }

        private static void g(String str, MediaFormat mediaFormat, Bundle bundle) {
            if (bundle.containsKey(str)) {
                mediaFormat.setInteger(str, bundle.getInt(str));
            }
        }

        private static void h(String str, MediaFormat mediaFormat, Bundle bundle) {
            if (bundle.containsKey(str)) {
                mediaFormat.setString(str, bundle.getString(str));
            }
        }

        @Override // androidx.versionedparcelable.CustomVersionedParcelable
        public void c() {
            Bundle bundle = this.f6133e;
            if (bundle != null && !bundle.getBoolean("androidx.media2.common.SessionPlayer.TrackInfo.KEY_IS_FORMAT_NULL")) {
                MediaFormat mediaFormat = new MediaFormat();
                this.f6131c = mediaFormat;
                h("language", mediaFormat, this.f6133e);
                h("mime", this.f6131c, this.f6133e);
                g("is-forced-subtitle", this.f6131c, this.f6133e);
                g("is-autoselect", this.f6131c, this.f6133e);
                g("is-default", this.f6131c, this.f6133e);
            }
            Bundle bundle2 = this.f6133e;
            if (bundle2 == null || !bundle2.containsKey("androidx.media2.common.SessionPlayer.TrackInfo.KEY_IS_SELECTABLE")) {
                this.f6132d = this.f6130b != 1;
            } else {
                this.f6132d = this.f6133e.getBoolean("androidx.media2.common.SessionPlayer.TrackInfo.KEY_IS_SELECTABLE");
            }
        }

        @Override // androidx.versionedparcelable.CustomVersionedParcelable
        public void d(boolean z4) {
            synchronized (this.f6134f) {
                Bundle bundle = new Bundle();
                this.f6133e = bundle;
                bundle.putBoolean("androidx.media2.common.SessionPlayer.TrackInfo.KEY_IS_FORMAT_NULL", this.f6131c == null);
                MediaFormat mediaFormat = this.f6131c;
                if (mediaFormat != null) {
                    f("language", mediaFormat, this.f6133e);
                    f("mime", this.f6131c, this.f6133e);
                    e("is-forced-subtitle", this.f6131c, this.f6133e);
                    e("is-autoselect", this.f6131c, this.f6133e);
                    e("is-default", this.f6131c, this.f6133e);
                }
                this.f6133e.putBoolean("androidx.media2.common.SessionPlayer.TrackInfo.KEY_IS_SELECTABLE", this.f6132d);
            }
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof TrackInfo) && this.f6129a == ((TrackInfo) obj).f6129a;
        }

        public int hashCode() {
            return this.f6129a;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder((int) RecognitionOptions.ITF);
            sb.append(getClass().getName());
            sb.append('#');
            sb.append(this.f6129a);
            sb.append('{');
            int i8 = this.f6130b;
            sb.append(i8 != 1 ? i8 != 2 ? i8 != 4 ? i8 != 5 ? "UNKNOWN" : "METADATA" : "SUBTITLE" : "AUDIO" : "VIDEO");
            sb.append(", ");
            sb.append(this.f6131c);
            sb.append(", isSelectable=");
            sb.append(this.f6132d);
            sb.append("}");
            return sb.toString();
        }
    }
}
