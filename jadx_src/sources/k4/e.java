package k4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioTrack;
import android.provider.Settings;
import android.util.Pair;
import b6.l0;
import com.google.android.exoplayer2.w0;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.d3;
import com.google.common.primitives.g;
import java.util.Arrays;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: c  reason: collision with root package name */
    public static final e f21003c = new e(new int[]{2}, 8);

    /* renamed from: d  reason: collision with root package name */
    private static final e f21004d = new e(new int[]{2, 5, 6}, 8);

    /* renamed from: e  reason: collision with root package name */
    private static final ImmutableMap<Integer, Integer> f21005e = new ImmutableMap.b().g(5, 6).g(17, 6).g(7, 6).g(18, 6).g(6, 8).g(8, 8).g(14, 8).d();

    /* renamed from: a  reason: collision with root package name */
    private final int[] f21006a;

    /* renamed from: b  reason: collision with root package name */
    private final int f21007b;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {

        /* renamed from: a  reason: collision with root package name */
        private static final AudioAttributes f21008a = new AudioAttributes.Builder().setUsage(1).setContentType(3).setFlags(0).build();

        public static int[] a() {
            ImmutableList.a u8 = ImmutableList.u();
            d3 it = e.f21005e.keySet().iterator();
            while (it.hasNext()) {
                int intValue = ((Integer) it.next()).intValue();
                if (AudioTrack.isDirectPlaybackSupported(new AudioFormat.Builder().setChannelMask(12).setEncoding(intValue).setSampleRate(48000).build(), f21008a)) {
                    u8.a(Integer.valueOf(intValue));
                }
            }
            u8.a(2);
            return g.l(u8.k());
        }

        public static int b(int i8, int i9) {
            for (int i10 = 8; i10 > 0; i10--) {
                if (AudioTrack.isDirectPlaybackSupported(new AudioFormat.Builder().setEncoding(i8).setSampleRate(i9).setChannelMask(l0.G(i10)).build(), f21008a)) {
                    return i10;
                }
            }
            return 0;
        }
    }

    public e(int[] iArr, int i8) {
        if (iArr != null) {
            int[] copyOf = Arrays.copyOf(iArr, iArr.length);
            this.f21006a = copyOf;
            Arrays.sort(copyOf);
        } else {
            this.f21006a = new int[0];
        }
        this.f21007b = i8;
    }

    private static boolean b() {
        if (l0.f8063a >= 17) {
            String str = l0.f8065c;
            if ("Amazon".equals(str) || "Xiaomi".equals(str)) {
                return true;
            }
        }
        return false;
    }

    public static e c(Context context) {
        return d(context, context.registerReceiver(null, new IntentFilter("android.media.action.HDMI_AUDIO_PLUG")));
    }

    @SuppressLint({"InlinedApi"})
    static e d(Context context, Intent intent) {
        return (b() && Settings.Global.getInt(context.getContentResolver(), "external_surround_sound_enabled", 0) == 1) ? f21004d : (l0.f8063a < 29 || !(l0.x0(context) || l0.s0(context))) ? (intent == null || intent.getIntExtra("android.media.extra.AUDIO_PLUG_STATE", 0) == 0) ? f21003c : new e(intent.getIntArrayExtra("android.media.extra.ENCODINGS"), intent.getIntExtra("android.media.extra.MAX_CHANNEL_COUNT", 8)) : new e(a.a(), 8);
    }

    private static int e(int i8) {
        int i9 = l0.f8063a;
        if (i9 <= 28) {
            if (i8 == 7) {
                i8 = 8;
            } else if (i8 == 3 || i8 == 4 || i8 == 5) {
                i8 = 6;
            }
        }
        if (i9 <= 26 && "fugu".equals(l0.f8064b) && i8 == 1) {
            i8 = 2;
        }
        return l0.G(i8);
    }

    private static int g(int i8, int i9) {
        return l0.f8063a >= 29 ? a.b(i8, i9) : ((Integer) b6.a.e(f21005e.getOrDefault(Integer.valueOf(i8), 0))).intValue();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof e) {
            e eVar = (e) obj;
            return Arrays.equals(this.f21006a, eVar.f21006a) && this.f21007b == eVar.f21007b;
        }
        return false;
    }

    public Pair<Integer, Integer> f(w0 w0Var) {
        int f5 = b6.t.f((String) b6.a.e(w0Var.f11207m), w0Var.f11204j);
        if (f21005e.containsKey(Integer.valueOf(f5))) {
            if (f5 == 18 && !i(18)) {
                f5 = 6;
            } else if (f5 == 8 && !i(8)) {
                f5 = 7;
            }
            if (i(f5)) {
                int i8 = w0Var.F;
                if (i8 == -1 || f5 == 18) {
                    int i9 = w0Var.G;
                    if (i9 == -1) {
                        i9 = 48000;
                    }
                    i8 = g(f5, i9);
                } else if (i8 > this.f21007b) {
                    return null;
                }
                int e8 = e(i8);
                if (e8 == 0) {
                    return null;
                }
                return Pair.create(Integer.valueOf(f5), Integer.valueOf(e8));
            }
            return null;
        }
        return null;
    }

    public boolean h(w0 w0Var) {
        return f(w0Var) != null;
    }

    public int hashCode() {
        return this.f21007b + (Arrays.hashCode(this.f21006a) * 31);
    }

    public boolean i(int i8) {
        return Arrays.binarySearch(this.f21006a, i8) >= 0;
    }

    public String toString() {
        return "AudioCapabilities[maxChannelCount=" + this.f21007b + ", supportedEncodings=" + Arrays.toString(this.f21006a) + "]";
    }
}
