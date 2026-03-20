package com.arthenica.ffmpegkit;

import android.os.Build;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class r {

    /* renamed from: a  reason: collision with root package name */
    static final String[] f8820a = {"avutil", "swscale", "swresample", "avcodec", "avformat", "avfilter", "avdevice"};

    /* renamed from: b  reason: collision with root package name */
    static final String[] f8821b = {"chromaprint", "openh264", "rubberband", "snappy", "srt", "tesseract", "x265", "zimg", "libilbc"};

    static String a() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("brand: ");
        sb.append(Build.BRAND);
        sb.append(", model: ");
        sb.append(Build.MODEL);
        sb.append(", device: ");
        sb.append(Build.DEVICE);
        sb.append(", api level: ");
        int i8 = Build.VERSION.SDK_INT;
        sb.append(i8);
        if (i8 >= 21) {
            sb.append(", abis: ");
            sb.append(FFmpegKitConfig.c(Build.SUPPORTED_ABIS));
            sb.append(", 32bit abis: ");
            sb.append(FFmpegKitConfig.c(Build.SUPPORTED_32_BIT_ABIS));
            sb.append(", 64bit abis: ");
            str = FFmpegKitConfig.c(Build.SUPPORTED_64_BIT_ABIS);
        } else {
            sb.append(", cpu abis: ");
            sb.append(Build.CPU_ABI);
            sb.append(", cpu abi2s: ");
            str = Build.CPU_ABI2;
        }
        sb.append(str);
        return sb.toString();
    }

    static boolean b() {
        return System.getProperty("enable.ffmpeg.kit.test.mode") == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String c() {
        return b() ? AbiDetect.a() : Abi.ABI_X86_64.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String d() {
        return b() ? FFmpegKitConfig.t() : new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
    }

    private static List<String> e() {
        return b() ? s.a() : Collections.emptyList();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static boolean f() {
        boolean z4;
        if (Build.VERSION.SDK_INT < 21) {
            List<String> e8 = e();
            String[] strArr = f8821b;
            int length = strArr.length;
            int i8 = 0;
            while (true) {
                if (i8 >= length) {
                    break;
                } else if (e8.contains(strArr[i8])) {
                    j("c++_shared");
                    break;
                } else {
                    i8++;
                }
            }
            boolean z8 = true;
            if ("arm-v7a".equals(l())) {
                try {
                    for (String str : f8820a) {
                        j(str + "_neon");
                    }
                    z4 = false;
                } catch (Error e9) {
                    Log.i("ffmpeg-kit", String.format("NEON supported armeabi-v7a ffmpeg library not found. Loading default armeabi-v7a library.%s", j2.a.a(e9)));
                    z4 = true;
                    z8 = false;
                }
            } else {
                z4 = false;
                z8 = false;
            }
            if (!z8) {
                for (String str2 : f8820a) {
                    j(str2);
                }
            }
            return z4;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0036  */
    /* JADX WARN: Removed duplicated region for block: B:22:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void g(boolean r3) {
        /*
            r0 = 1
            r1 = 0
            if (r3 != 0) goto L33
            java.lang.String r3 = l()
            java.lang.String r2 = "arm-v7a"
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L33
            java.lang.String r3 = "ffmpegkit_armv7a_neon"
            j(r3)     // Catch: java.lang.Error -> L1c
            com.arthenica.ffmpegkit.AbiDetect.b()     // Catch: java.lang.Error -> L19
            goto L34
        L19:
            r3 = move-exception
            r2 = r0
            goto L1e
        L1c:
            r3 = move-exception
            r2 = r1
        L1e:
            java.lang.Object[] r0 = new java.lang.Object[r0]
            java.lang.String r3 = j2.a.a(r3)
            r0[r1] = r3
            java.lang.String r3 = "NEON supported armeabi-v7a ffmpegkit library not found. Loading default armeabi-v7a library.%s"
            java.lang.String r3 = java.lang.String.format(r3, r0)
            java.lang.String r0 = "ffmpeg-kit"
            android.util.Log.i(r0, r3)
            r0 = r2
            goto L34
        L33:
            r0 = r1
        L34:
            if (r0 != 0) goto L3b
            java.lang.String r3 = "ffmpegkit"
            j(r3)
        L3b:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.arthenica.ffmpegkit.r.g(boolean):void");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void h() {
        j("ffmpegkit_abidetect");
    }

    static boolean i() {
        if (b()) {
            return AbiDetect.isNativeLTSBuild();
        }
        return true;
    }

    private static void j(String str) {
        if (b()) {
            try {
                System.loadLibrary(str);
            } catch (UnsatisfiedLinkError e8) {
                throw new Error(String.format("FFmpegKit failed to start on %s.", a()), e8);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static int k() {
        return b() ? FFmpegKitConfig.getNativeLogLevel() : Level.AV_LOG_DEBUG.h();
    }

    private static String l() {
        return b() ? AbiDetect.getNativeAbi() : Abi.ABI_X86_64.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String m() {
        return b() ? s.b() : "test";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static String n() {
        return b() ? FFmpegKitConfig.L() : i() ? String.format("%s-lts", "6.0") : "6.0";
    }
}
