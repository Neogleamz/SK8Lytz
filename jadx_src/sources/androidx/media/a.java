package androidx.media;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import androidx.media.AudioAttributesCompat;
import java.util.Objects;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: g  reason: collision with root package name */
    static final AudioAttributesCompat f6067g = new AudioAttributesCompat.a().e(1).a();

    /* renamed from: a  reason: collision with root package name */
    private final int f6068a;

    /* renamed from: b  reason: collision with root package name */
    private final AudioManager.OnAudioFocusChangeListener f6069b;

    /* renamed from: c  reason: collision with root package name */
    private final Handler f6070c;

    /* renamed from: d  reason: collision with root package name */
    private final AudioAttributesCompat f6071d;

    /* renamed from: e  reason: collision with root package name */
    private final boolean f6072e;

    /* renamed from: f  reason: collision with root package name */
    private final Object f6073f;

    /* renamed from: androidx.media.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class C0063a {
        static AudioFocusRequest a(int i8, AudioAttributes audioAttributes, boolean z4, AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener, Handler handler) {
            return new AudioFocusRequest.Builder(i8).setAudioAttributes(audioAttributes).setWillPauseWhenDucked(z4).setOnAudioFocusChangeListener(onAudioFocusChangeListener, handler).build();
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class b {

        /* renamed from: a  reason: collision with root package name */
        private int f6074a;

        /* renamed from: b  reason: collision with root package name */
        private AudioManager.OnAudioFocusChangeListener f6075b;

        /* renamed from: c  reason: collision with root package name */
        private Handler f6076c;

        /* renamed from: d  reason: collision with root package name */
        private AudioAttributesCompat f6077d = a.f6067g;

        /* renamed from: e  reason: collision with root package name */
        private boolean f6078e;

        public b(int i8) {
            d(i8);
        }

        private static boolean b(int i8) {
            return i8 == 1 || i8 == 2 || i8 == 3 || i8 == 4;
        }

        public a a() {
            if (this.f6075b != null) {
                return new a(this.f6074a, this.f6075b, this.f6076c, this.f6077d, this.f6078e);
            }
            throw new IllegalStateException("Can't build an AudioFocusRequestCompat instance without a listener");
        }

        public b c(AudioAttributesCompat audioAttributesCompat) {
            Objects.requireNonNull(audioAttributesCompat, "Illegal null AudioAttributes");
            this.f6077d = audioAttributesCompat;
            return this;
        }

        public b d(int i8) {
            if (!b(i8)) {
                throw new IllegalArgumentException("Illegal audio focus gain type " + i8);
            }
            if (Build.VERSION.SDK_INT < 19 && i8 == 4) {
                i8 = 2;
            }
            this.f6074a = i8;
            return this;
        }

        public b e(AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener) {
            return f(onAudioFocusChangeListener, new Handler(Looper.getMainLooper()));
        }

        public b f(AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener, Handler handler) {
            if (onAudioFocusChangeListener != null) {
                if (handler != null) {
                    this.f6075b = onAudioFocusChangeListener;
                    this.f6076c = handler;
                    return this;
                }
                throw new IllegalArgumentException("Handler must not be null");
            }
            throw new IllegalArgumentException("OnAudioFocusChangeListener must not be null");
        }

        public b g(boolean z4) {
            this.f6078e = z4;
            return this;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c implements Handler.Callback, AudioManager.OnAudioFocusChangeListener {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f6079a;

        /* renamed from: b  reason: collision with root package name */
        private final AudioManager.OnAudioFocusChangeListener f6080b;

        c(AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener, Handler handler) {
            this.f6080b = onAudioFocusChangeListener;
            this.f6079a = new Handler(handler.getLooper(), this);
        }

        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            if (message.what == 2782386) {
                this.f6080b.onAudioFocusChange(message.arg1);
                return true;
            }
            return false;
        }

        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(int i8) {
            Handler handler = this.f6079a;
            handler.sendMessage(Message.obtain(handler, 2782386, i8, 0));
        }
    }

    a(int i8, AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener, Handler handler, AudioAttributesCompat audioAttributesCompat, boolean z4) {
        this.f6068a = i8;
        this.f6070c = handler;
        this.f6071d = audioAttributesCompat;
        this.f6072e = z4;
        int i9 = Build.VERSION.SDK_INT;
        if (i9 >= 26 || handler.getLooper() == Looper.getMainLooper()) {
            this.f6069b = onAudioFocusChangeListener;
        } else {
            this.f6069b = new c(onAudioFocusChangeListener, handler);
        }
        this.f6073f = i9 >= 26 ? C0063a.a(i8, a(), z4, this.f6069b, handler) : null;
    }

    AudioAttributes a() {
        AudioAttributesCompat audioAttributesCompat = this.f6071d;
        if (audioAttributesCompat != null) {
            return (AudioAttributes) audioAttributesCompat.d();
        }
        return null;
    }

    public AudioAttributesCompat b() {
        return this.f6071d;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public AudioFocusRequest c() {
        return (AudioFocusRequest) this.f6073f;
    }

    public int d() {
        return this.f6068a;
    }

    public AudioManager.OnAudioFocusChangeListener e() {
        return this.f6069b;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof a) {
            a aVar = (a) obj;
            return this.f6068a == aVar.f6068a && this.f6072e == aVar.f6072e && androidx.core.util.c.a(this.f6069b, aVar.f6069b) && androidx.core.util.c.a(this.f6070c, aVar.f6070c) && androidx.core.util.c.a(this.f6071d, aVar.f6071d);
        }
        return false;
    }

    public int hashCode() {
        return androidx.core.util.c.b(Integer.valueOf(this.f6068a), this.f6069b, this.f6070c, this.f6071d, Boolean.valueOf(this.f6072e));
    }
}
