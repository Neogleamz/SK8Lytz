package com.google.android.exoplayer2.util;

import android.opengl.GLES20;
import java.util.HashMap;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class b {

    /* renamed from: a  reason: collision with root package name */
    private final int f10994a;

    /* renamed from: b  reason: collision with root package name */
    private final a[] f10995b;

    /* renamed from: c  reason: collision with root package name */
    private final C0119b[] f10996c;

    /* renamed from: d  reason: collision with root package name */
    private final Map<String, a> f10997d;

    /* renamed from: e  reason: collision with root package name */
    private final Map<String, C0119b> f10998e;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a {

        /* renamed from: a  reason: collision with root package name */
        public final String f10999a;

        /* renamed from: b  reason: collision with root package name */
        private final int f11000b;

        /* renamed from: c  reason: collision with root package name */
        private final int f11001c;

        private a(String str, int i8, int i9) {
            this.f10999a = str;
            this.f11000b = i8;
            this.f11001c = i9;
        }

        public static a a(int i8, int i9) {
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(i8, 35722, iArr, 0);
            byte[] bArr = new byte[iArr[0]];
            GLES20.glGetActiveAttrib(i8, i9, iArr[0], new int[1], 0, new int[1], 0, new int[1], 0, bArr, 0);
            String str = new String(bArr, 0, b.h(bArr));
            return new a(str, i9, b.f(i8, str));
        }
    }

    /* renamed from: com.google.android.exoplayer2.util.b$b  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class C0119b {

        /* renamed from: a  reason: collision with root package name */
        public final String f11002a;

        /* renamed from: b  reason: collision with root package name */
        private final int f11003b;

        /* renamed from: c  reason: collision with root package name */
        private final int f11004c;

        /* renamed from: d  reason: collision with root package name */
        private final float[] f11005d = new float[16];

        private C0119b(String str, int i8, int i9) {
            this.f11002a = str;
            this.f11003b = i8;
            this.f11004c = i9;
        }

        public static C0119b a(int i8, int i9) {
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(i8, 35719, iArr, 0);
            int[] iArr2 = new int[1];
            byte[] bArr = new byte[iArr[0]];
            GLES20.glGetActiveUniform(i8, i9, iArr[0], new int[1], 0, new int[1], 0, iArr2, 0, bArr, 0);
            String str = new String(bArr, 0, b.h(bArr));
            return new C0119b(str, b.i(i8, str), iArr2[0]);
        }
    }

    public b(String str, String str2) {
        int glCreateProgram = GLES20.glCreateProgram();
        this.f10994a = glCreateProgram;
        GlUtil.b();
        d(glCreateProgram, 35633, str);
        d(glCreateProgram, 35632, str2);
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = {0};
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        GlUtil.c(iArr[0] == 1, "Unable to link shader program: \n" + GLES20.glGetProgramInfoLog(glCreateProgram));
        GLES20.glUseProgram(glCreateProgram);
        this.f10997d = new HashMap();
        int[] iArr2 = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35721, iArr2, 0);
        this.f10995b = new a[iArr2[0]];
        for (int i8 = 0; i8 < iArr2[0]; i8++) {
            a a9 = a.a(this.f10994a, i8);
            this.f10995b[i8] = a9;
            this.f10997d.put(a9.f10999a, a9);
        }
        this.f10998e = new HashMap();
        int[] iArr3 = new int[1];
        GLES20.glGetProgramiv(this.f10994a, 35718, iArr3, 0);
        this.f10996c = new C0119b[iArr3[0]];
        for (int i9 = 0; i9 < iArr3[0]; i9++) {
            C0119b a10 = C0119b.a(this.f10994a, i9);
            this.f10996c[i9] = a10;
            this.f10998e.put(a10.f11002a, a10);
        }
        GlUtil.b();
    }

    private static void d(int i8, int i9, String str) {
        int glCreateShader = GLES20.glCreateShader(i9);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = {0};
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        boolean z4 = iArr[0] == 1;
        GlUtil.c(z4, GLES20.glGetShaderInfoLog(glCreateShader) + ", source: " + str);
        GLES20.glAttachShader(i8, glCreateShader);
        GLES20.glDeleteShader(glCreateShader);
        GlUtil.b();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int f(int i8, String str) {
        return GLES20.glGetAttribLocation(i8, str);
    }

    private int g(String str) {
        return f(this.f10994a, str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int h(byte[] bArr) {
        for (int i8 = 0; i8 < bArr.length; i8++) {
            if (bArr[i8] == 0) {
                return i8;
            }
        }
        return bArr.length;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int i(int i8, String str) {
        return GLES20.glGetUniformLocation(i8, str);
    }

    public int e(String str) {
        int g8 = g(str);
        GLES20.glEnableVertexAttribArray(g8);
        GlUtil.b();
        return g8;
    }

    public int j(String str) {
        return i(this.f10994a, str);
    }
}
