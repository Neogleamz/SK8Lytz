package com.google.android.exoplayer2.util;

import android.content.Context;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.opengl.GLU;
import android.opengl.Matrix;
import b6.l0;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class GlUtil {

    /* renamed from: a  reason: collision with root package name */
    public static final int[] f10980a = {12352, 4, 12324, 8, 12323, 8, 12322, 8, 12321, 8, 12325, 0, 12326, 0, 12344};

    /* renamed from: b  reason: collision with root package name */
    public static final int[] f10981b = {12352, 4, 12324, 10, 12323, 10, 12322, 10, 12321, 2, 12325, 0, 12326, 0, 12344};

    /* renamed from: c  reason: collision with root package name */
    private static final int[] f10982c = {12344};

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class GlException extends Exception {
        public GlException(String str) {
            super(str);
        }
    }

    public static void a(int i8, int i9) {
        GLES20.glBindTexture(i8, i9);
        b();
        GLES20.glTexParameteri(i8, 10240, 9729);
        b();
        GLES20.glTexParameteri(i8, 10241, 9729);
        b();
        GLES20.glTexParameteri(i8, 10242, 33071);
        b();
        GLES20.glTexParameteri(i8, 10243, 33071);
        b();
    }

    public static void b() {
        StringBuilder sb = new StringBuilder();
        boolean z4 = false;
        while (true) {
            int glGetError = GLES20.glGetError();
            if (glGetError == 0) {
                break;
            }
            if (z4) {
                sb.append('\n');
            }
            sb.append("glError: ");
            sb.append(GLU.gluErrorString(glGetError));
            z4 = true;
        }
        if (z4) {
            throw new GlException(sb.toString());
        }
    }

    public static void c(boolean z4, String str) {
        if (!z4) {
            throw new GlException(str);
        }
    }

    private static FloatBuffer d(int i8) {
        return ByteBuffer.allocateDirect(i8 * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    public static FloatBuffer e(float[] fArr) {
        return (FloatBuffer) d(fArr.length).put(fArr).flip();
    }

    public static int f() {
        int g8 = g();
        a(36197, g8);
        return g8;
    }

    private static int g() {
        c(!l0.c(EGL14.eglGetCurrentContext(), EGL14.EGL_NO_CONTEXT), "No current context");
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        b();
        return iArr[0];
    }

    public static boolean h(Context context) {
        String eglQueryString;
        int i8 = l0.f8063a;
        if (i8 < 24) {
            return false;
        }
        if (i8 >= 26 || !("samsung".equals(l0.f8065c) || "XT1650".equals(l0.f8066d))) {
            return (i8 >= 26 || context.getPackageManager().hasSystemFeature("android.hardware.vr.high_performance")) && (eglQueryString = EGL14.eglQueryString(EGL14.eglGetDisplay(0), 12373)) != null && eglQueryString.contains("EGL_EXT_protected_content");
        }
        return false;
    }

    public static boolean i() {
        String eglQueryString;
        return l0.f8063a >= 17 && (eglQueryString = EGL14.eglQueryString(EGL14.eglGetDisplay(0), 12373)) != null && eglQueryString.contains("EGL_KHR_surfaceless_context");
    }

    public static void j(float[] fArr) {
        Matrix.setIdentityM(fArr, 0);
    }
}
