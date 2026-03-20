package androidx.media;

import android.media.AudioAttributes;
import android.os.Build;
import android.util.SparseIntArray;
import androidx.media.AudioAttributesImpl;
import androidx.media.AudioAttributesImplApi21;
import androidx.media.AudioAttributesImplApi26;
import androidx.media.AudioAttributesImplBase;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesCompat implements y1.b {

    /* renamed from: b  reason: collision with root package name */
    private static final SparseIntArray f5952b;

    /* renamed from: c  reason: collision with root package name */
    static boolean f5953c;

    /* renamed from: d  reason: collision with root package name */
    private static final int[] f5954d;

    /* renamed from: a  reason: collision with root package name */
    public AudioAttributesImpl f5955a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a {

        /* renamed from: a  reason: collision with root package name */
        final AudioAttributesImpl.a f5956a;

        public a() {
            AudioAttributesImpl.a aVar;
            if (AudioAttributesCompat.f5953c) {
                aVar = new AudioAttributesImplBase.a();
            } else {
                int i8 = Build.VERSION.SDK_INT;
                aVar = i8 >= 26 ? new AudioAttributesImplApi26.a() : i8 >= 21 ? new AudioAttributesImplApi21.a() : new AudioAttributesImplBase.a();
            }
            this.f5956a = aVar;
        }

        public AudioAttributesCompat a() {
            return new AudioAttributesCompat(this.f5956a.a());
        }

        public a b(int i8) {
            this.f5956a.d(i8);
            return this;
        }

        public a c(int i8) {
            this.f5956a.b(i8);
            return this;
        }

        public a d(int i8) {
            this.f5956a.c(i8);
            return this;
        }

        public a e(int i8) {
            this.f5956a.e(i8);
            return this;
        }
    }

    static {
        SparseIntArray sparseIntArray = new SparseIntArray();
        f5952b = sparseIntArray;
        sparseIntArray.put(5, 1);
        sparseIntArray.put(6, 2);
        sparseIntArray.put(7, 2);
        sparseIntArray.put(8, 1);
        sparseIntArray.put(9, 1);
        sparseIntArray.put(10, 1);
        f5954d = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 16};
    }

    public AudioAttributesCompat() {
    }

    AudioAttributesCompat(AudioAttributesImpl audioAttributesImpl) {
        this.f5955a = audioAttributesImpl;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int c(boolean z4, int i8, int i9) {
        if ((i8 & 1) == 1) {
            return z4 ? 1 : 7;
        } else if ((i8 & 4) == 4) {
            return z4 ? 0 : 6;
        } else {
            switch (i9) {
                case 0:
                case 1:
                case 12:
                case 14:
                case 16:
                    return 3;
                case 2:
                    return 0;
                case 3:
                    return z4 ? 0 : 8;
                case 4:
                    return 4;
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                    return 5;
                case 6:
                    return 2;
                case 11:
                    return 10;
                case 13:
                    return 1;
                case 15:
                default:
                    if (z4) {
                        throw new IllegalArgumentException("Unknown usage value " + i9 + " in audio attributes");
                    }
                    return 3;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String e(int i8) {
        switch (i8) {
            case 0:
                return "USAGE_UNKNOWN";
            case 1:
                return "USAGE_MEDIA";
            case 2:
                return "USAGE_VOICE_COMMUNICATION";
            case 3:
                return "USAGE_VOICE_COMMUNICATION_SIGNALLING";
            case 4:
                return "USAGE_ALARM";
            case 5:
                return "USAGE_NOTIFICATION";
            case 6:
                return "USAGE_NOTIFICATION_RINGTONE";
            case 7:
                return "USAGE_NOTIFICATION_COMMUNICATION_REQUEST";
            case 8:
                return "USAGE_NOTIFICATION_COMMUNICATION_INSTANT";
            case 9:
                return "USAGE_NOTIFICATION_COMMUNICATION_DELAYED";
            case 10:
                return "USAGE_NOTIFICATION_EVENT";
            case 11:
                return "USAGE_ASSISTANCE_ACCESSIBILITY";
            case 12:
                return "USAGE_ASSISTANCE_NAVIGATION_GUIDANCE";
            case 13:
                return "USAGE_ASSISTANCE_SONIFICATION";
            case 14:
                return "USAGE_GAME";
            case 15:
            default:
                return "unknown usage " + i8;
            case 16:
                return "USAGE_ASSISTANT";
        }
    }

    public static AudioAttributesCompat f(Object obj) {
        if (f5953c) {
            return null;
        }
        int i8 = Build.VERSION.SDK_INT;
        if (i8 >= 26) {
            return new AudioAttributesCompat(new AudioAttributesImplApi26((AudioAttributes) obj));
        }
        if (i8 >= 21) {
            return new AudioAttributesCompat(new AudioAttributesImplApi21((AudioAttributes) obj));
        }
        return null;
    }

    public int a() {
        return this.f5955a.a();
    }

    public Object d() {
        return this.f5955a.b();
    }

    public boolean equals(Object obj) {
        if (obj instanceof AudioAttributesCompat) {
            AudioAttributesImpl audioAttributesImpl = this.f5955a;
            AudioAttributesImpl audioAttributesImpl2 = ((AudioAttributesCompat) obj).f5955a;
            return audioAttributesImpl == null ? audioAttributesImpl2 == null : audioAttributesImpl.equals(audioAttributesImpl2);
        }
        return false;
    }

    public int hashCode() {
        return this.f5955a.hashCode();
    }

    public String toString() {
        return this.f5955a.toString();
    }
}
