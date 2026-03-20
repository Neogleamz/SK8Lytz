package com.google.android.exoplayer2;

import android.content.Context;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.os.Handler;
import com.google.android.exoplayer2.d;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class d {

    /* renamed from: a  reason: collision with root package name */
    private final AudioManager f9489a;

    /* renamed from: b  reason: collision with root package name */
    private final a f9490b;

    /* renamed from: c  reason: collision with root package name */
    private b f9491c;

    /* renamed from: d  reason: collision with root package name */
    private com.google.android.exoplayer2.audio.a f9492d;

    /* renamed from: f  reason: collision with root package name */
    private int f9494f;

    /* renamed from: h  reason: collision with root package name */
    private AudioFocusRequest f9496h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f9497i;

    /* renamed from: g  reason: collision with root package name */
    private float f9495g = 1.0f;

    /* renamed from: e  reason: collision with root package name */
    private int f9493e = 0;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class a implements AudioManager.OnAudioFocusChangeListener {

        /* renamed from: a  reason: collision with root package name */
        private final Handler f9498a;

        public a(Handler handler) {
            this.f9498a = handler;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void b(int i8) {
            d.this.h(i8);
        }

        @Override // android.media.AudioManager.OnAudioFocusChangeListener
        public void onAudioFocusChange(final int i8) {
            this.f9498a.post(new Runnable() { // from class: com.google.android.exoplayer2.c
                @Override // java.lang.Runnable
                public final void run() {
                    d.a.this.b(i8);
                }
            });
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void A(float f5);

        void B(int i8);
    }

    public d(Context context, Handler handler, b bVar) {
        this.f9489a = (AudioManager) b6.a.e((AudioManager) context.getApplicationContext().getSystemService("audio"));
        this.f9491c = bVar;
        this.f9490b = new a(handler);
    }

    private void a() {
        this.f9489a.abandonAudioFocus(this.f9490b);
    }

    private void b() {
        if (this.f9493e == 0) {
            return;
        }
        if (b6.l0.f8063a >= 26) {
            c();
        } else {
            a();
        }
        n(0);
    }

    private void c() {
        AudioFocusRequest audioFocusRequest = this.f9496h;
        if (audioFocusRequest != null) {
            this.f9489a.abandonAudioFocusRequest(audioFocusRequest);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private static int e(com.google.android.exoplayer2.audio.a aVar) {
        if (aVar == null) {
            return 0;
        }
        switch (aVar.f9322c) {
            case 0:
                b6.p.i("AudioFocusManager", "Specify a proper usage in the audio attributes for audio focus handling. Using AUDIOFOCUS_GAIN by default.");
                return 1;
            case 1:
            case 14:
                return 1;
            case 2:
            case 4:
                return 2;
            case 3:
                return 0;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 12:
            case 13:
                break;
            case 11:
                if (aVar.f9320a == 1) {
                    return 2;
                }
                break;
            case 15:
            default:
                b6.p.i("AudioFocusManager", "Unidentified audio usage: " + aVar.f9322c);
                return 0;
            case 16:
                return b6.l0.f8063a >= 19 ? 4 : 2;
        }
        return 3;
    }

    private void f(int i8) {
        b bVar = this.f9491c;
        if (bVar != null) {
            bVar.B(i8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void h(int i8) {
        int i9;
        if (i8 == -3 || i8 == -2) {
            if (i8 == -2 || q()) {
                f(0);
                i9 = 2;
            } else {
                i9 = 3;
            }
            n(i9);
        } else if (i8 == -1) {
            f(-1);
            b();
        } else if (i8 == 1) {
            n(1);
            f(1);
        } else {
            b6.p.i("AudioFocusManager", "Unknown focus change type: " + i8);
        }
    }

    private int j() {
        if (this.f9493e == 1) {
            return 1;
        }
        if ((b6.l0.f8063a >= 26 ? l() : k()) == 1) {
            n(1);
            return 1;
        }
        n(0);
        return -1;
    }

    private int k() {
        return this.f9489a.requestAudioFocus(this.f9490b, b6.l0.f0(((com.google.android.exoplayer2.audio.a) b6.a.e(this.f9492d)).f9322c), this.f9494f);
    }

    private int l() {
        AudioFocusRequest audioFocusRequest = this.f9496h;
        if (audioFocusRequest == null || this.f9497i) {
            this.f9496h = (audioFocusRequest == null ? new AudioFocusRequest.Builder(this.f9494f) : new AudioFocusRequest.Builder(this.f9496h)).setAudioAttributes(((com.google.android.exoplayer2.audio.a) b6.a.e(this.f9492d)).b().f9326a).setWillPauseWhenDucked(q()).setOnAudioFocusChangeListener(this.f9490b).build();
            this.f9497i = false;
        }
        return this.f9489a.requestAudioFocus(this.f9496h);
    }

    private void n(int i8) {
        if (this.f9493e == i8) {
            return;
        }
        this.f9493e = i8;
        float f5 = i8 == 3 ? 0.2f : 1.0f;
        if (this.f9495g == f5) {
            return;
        }
        this.f9495g = f5;
        b bVar = this.f9491c;
        if (bVar != null) {
            bVar.A(f5);
        }
    }

    private boolean o(int i8) {
        return i8 == 1 || this.f9494f != 1;
    }

    private boolean q() {
        com.google.android.exoplayer2.audio.a aVar = this.f9492d;
        return aVar != null && aVar.f9320a == 1;
    }

    public float g() {
        return this.f9495g;
    }

    public void i() {
        this.f9491c = null;
        b();
    }

    public void m(com.google.android.exoplayer2.audio.a aVar) {
        if (b6.l0.c(this.f9492d, aVar)) {
            return;
        }
        this.f9492d = aVar;
        int e8 = e(aVar);
        this.f9494f = e8;
        boolean z4 = true;
        if (e8 != 1 && e8 != 0) {
            z4 = false;
        }
        b6.a.b(z4, "Automatic handling of audio focus is only available for USAGE_MEDIA and USAGE_GAME.");
    }

    public int p(boolean z4, int i8) {
        if (o(i8)) {
            b();
            return z4 ? 1 : -1;
        } else if (z4) {
            return j();
        } else {
            return -1;
        }
    }
}
