package androidx.media;

import android.media.AudioAttributes;
import androidx.media.AudioAttributesImpl;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AudioAttributesImplApi21 implements AudioAttributesImpl {

    /* renamed from: a  reason: collision with root package name */
    public AudioAttributes f5957a;

    /* renamed from: b  reason: collision with root package name */
    public int f5958b;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class a implements AudioAttributesImpl.a {

        /* renamed from: a  reason: collision with root package name */
        final AudioAttributes.Builder f5959a = new AudioAttributes.Builder();

        @Override // androidx.media.AudioAttributesImpl.a
        public AudioAttributesImpl a() {
            return new AudioAttributesImplApi21(this.f5959a.build());
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: f */
        public a d(int i8) {
            this.f5959a.setContentType(i8);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: g */
        public a b(int i8) {
            this.f5959a.setFlags(i8);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: h */
        public a c(int i8) {
            this.f5959a.setLegacyStreamType(i8);
            return this;
        }

        @Override // androidx.media.AudioAttributesImpl.a
        /* renamed from: i */
        public a e(int i8) {
            if (i8 == 16) {
                i8 = 12;
            }
            this.f5959a.setUsage(i8);
            return this;
        }
    }

    public AudioAttributesImplApi21() {
        this.f5958b = -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioAttributesImplApi21(AudioAttributes audioAttributes) {
        this(audioAttributes, -1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioAttributesImplApi21(AudioAttributes audioAttributes, int i8) {
        this.f5958b = -1;
        this.f5957a = audioAttributes;
        this.f5958b = i8;
    }

    @Override // androidx.media.AudioAttributesImpl
    public int a() {
        int i8 = this.f5958b;
        return i8 != -1 ? i8 : AudioAttributesCompat.c(false, c(), d());
    }

    @Override // androidx.media.AudioAttributesImpl
    public Object b() {
        return this.f5957a;
    }

    public int c() {
        return this.f5957a.getFlags();
    }

    public int d() {
        return this.f5957a.getUsage();
    }

    public boolean equals(Object obj) {
        if (obj instanceof AudioAttributesImplApi21) {
            return this.f5957a.equals(((AudioAttributesImplApi21) obj).f5957a);
        }
        return false;
    }

    public int hashCode() {
        return this.f5957a.hashCode();
    }

    public String toString() {
        return "AudioAttributesCompat: audioattributes=" + this.f5957a;
    }
}
