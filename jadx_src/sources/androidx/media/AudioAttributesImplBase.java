package androidx.media;

import android.util.Log;
import androidx.media.AudioAttributesImpl;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesImplBase implements AudioAttributesImpl {

    /* renamed from: a  reason: collision with root package name */
    public int f5960a;

    /* renamed from: b  reason: collision with root package name */
    public int f5961b;

    /* renamed from: c  reason: collision with root package name */
    public int f5962c;

    /* renamed from: d  reason: collision with root package name */
    public int f5963d;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements AudioAttributesImpl.a {

        /* renamed from: a  reason: collision with root package name */
        private int f5964a = 0;

        /* renamed from: b  reason: collision with root package name */
        private int f5965b = 0;

        /* renamed from: c  reason: collision with root package name */
        private int f5966c = 0;

        /* renamed from: d  reason: collision with root package name */
        private int f5967d = -1;

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        private a h(int i8) {
            int i9 = 1;
            switch (i8) {
                case 0:
                case 10:
                    this.f5965b = i9;
                    break;
                case 1:
                case 2:
                case 4:
                case 5:
                case 8:
                case 9:
                    this.f5965b = 4;
                    break;
                case 3:
                    i9 = 2;
                    this.f5965b = i9;
                    break;
                case 6:
                    this.f5965b = 1;
                    this.f5966c |= 4;
                    break;
                case 7:
                    this.f5966c = 1 | this.f5966c;
                    this.f5965b = 4;
                    break;
                default:
                    Log.e("AudioAttributesCompat", "Invalid stream type " + i8 + " for AudioAttributesCompat");
                    break;
            }
            this.f5964a = AudioAttributesImplBase.f(i8);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        public AudioAttributesImpl a() {
            return new AudioAttributesImplBase(this.f5965b, this.f5966c, this.f5964a, this.f5967d);
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: f */
        public a d(int i8) {
            if (i8 != 0 && i8 != 1 && i8 != 2 && i8 != 3 && i8 != 4) {
                i8 = 0;
            }
            this.f5965b = i8;
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: g */
        public a b(int i8) {
            this.f5966c = (i8 & 1023) | this.f5966c;
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: i */
        public a c(int i8) {
            if (i8 != 10) {
                this.f5967d = i8;
                return h(i8);
            }
            throw new IllegalArgumentException("STREAM_ACCESSIBILITY is not a legacy stream type that was used for audio playback");
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: j */
        public a e(int i8) {
            switch (i8) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                    break;
                case 16:
                    i8 = 12;
                    break;
                default:
                    i8 = 0;
                    break;
            }
            this.f5964a = i8;
            return this;
        }
    }

    public AudioAttributesImplBase() {
        this.f5960a = 0;
        this.f5961b = 0;
        this.f5962c = 0;
        this.f5963d = -1;
    }

    AudioAttributesImplBase(int i8, int i9, int i10, int i11) {
        this.f5960a = 0;
        this.f5961b = 0;
        this.f5962c = 0;
        this.f5963d = -1;
        this.f5961b = i8;
        this.f5962c = i9;
        this.f5960a = i10;
        this.f5963d = i11;
    }

    static int f(int i8) {
        switch (i8) {
            case 0:
                return 2;
            case 1:
            case 7:
                return 13;
            case 2:
                return 6;
            case 3:
                return 1;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 2;
            case 8:
                return 3;
            case 9:
            default:
                return 0;
            case 10:
                return 11;
        }
    }

    @Override // androidx.media.AudioAttributesImpl
    public int a() {
        int i8 = this.f5963d;
        return i8 != -1 ? i8 : AudioAttributesCompat.c(false, this.f5962c, this.f5960a);
    }

    @Override // androidx.media.AudioAttributesImpl
    public Object b() {
        return null;
    }

    public int c() {
        return this.f5961b;
    }

    public int d() {
        int i8 = this.f5962c;
        int a9 = a();
        if (a9 == 6) {
            i8 |= 4;
        } else if (a9 == 7) {
            i8 |= 1;
        }
        return i8 & 273;
    }

    public int e() {
        return this.f5960a;
    }

    public boolean equals(Object obj) {
        if (obj instanceof AudioAttributesImplBase) {
            AudioAttributesImplBase audioAttributesImplBase = (AudioAttributesImplBase) obj;
            return this.f5961b == audioAttributesImplBase.c() && this.f5962c == audioAttributesImplBase.d() && this.f5960a == audioAttributesImplBase.e() && this.f5963d == audioAttributesImplBase.f5963d;
        }
        return false;
    }

    public int hashCode() {
        return Arrays.hashCode(new Object[]{Integer.valueOf(this.f5961b), Integer.valueOf(this.f5962c), Integer.valueOf(this.f5960a), Integer.valueOf(this.f5963d)});
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("AudioAttributesCompat:");
        if (this.f5963d != -1) {
            sb.append(" stream=");
            sb.append(this.f5963d);
            sb.append(" derived");
        }
        sb.append(" usage=");
        sb.append(AudioAttributesCompat.e(this.f5960a));
        sb.append(" content=");
        sb.append(this.f5961b);
        sb.append(" flags=0x");
        sb.append(Integer.toHexString(this.f5962c).toUpperCase());
        return sb.toString();
    }
}
