package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AbiDetect {

    /* renamed from: a  reason: collision with root package name */
    private static boolean f8732a = false;

    static {
        r.h();
    }

    private AbiDetect() {
    }

    public static String a() {
        return f8732a ? "arm-v7a-neon" : getNativeAbi();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static void b() {
        f8732a = true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native String getNativeAbi();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native String getNativeBuildConf();

    static native String getNativeCpuAbi();

    /* JADX INFO: Access modifiers changed from: package-private */
    public static native boolean isNativeLTSBuild();
}
