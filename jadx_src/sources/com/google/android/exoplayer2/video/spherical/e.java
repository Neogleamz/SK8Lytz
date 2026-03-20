package com.google.android.exoplayer2.video.spherical;

import android.opengl.GLES20;
import android.util.Log;
import com.google.android.exoplayer2.util.GlUtil;
import com.google.android.exoplayer2.video.spherical.c;
import java.nio.Buffer;
import java.nio.FloatBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
final class e {

    /* renamed from: j  reason: collision with root package name */
    private static final float[] f11131j = {1.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f};

    /* renamed from: k  reason: collision with root package name */
    private static final float[] f11132k = {1.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, 0.5f, 1.0f};

    /* renamed from: l  reason: collision with root package name */
    private static final float[] f11133l = {1.0f, 0.0f, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, 1.0f, 1.0f};

    /* renamed from: m  reason: collision with root package name */
    private static final float[] f11134m = {0.5f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f};

    /* renamed from: n  reason: collision with root package name */
    private static final float[] f11135n = {0.5f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.5f, 1.0f, 1.0f};

    /* renamed from: a  reason: collision with root package name */
    private int f11136a;

    /* renamed from: b  reason: collision with root package name */
    private a f11137b;

    /* renamed from: c  reason: collision with root package name */
    private a f11138c;

    /* renamed from: d  reason: collision with root package name */
    private com.google.android.exoplayer2.util.b f11139d;

    /* renamed from: e  reason: collision with root package name */
    private int f11140e;

    /* renamed from: f  reason: collision with root package name */
    private int f11141f;

    /* renamed from: g  reason: collision with root package name */
    private int f11142g;

    /* renamed from: h  reason: collision with root package name */
    private int f11143h;

    /* renamed from: i  reason: collision with root package name */
    private int f11144i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class a {

        /* renamed from: a  reason: collision with root package name */
        private final int f11145a;

        /* renamed from: b  reason: collision with root package name */
        private final FloatBuffer f11146b;

        /* renamed from: c  reason: collision with root package name */
        private final FloatBuffer f11147c;

        /* renamed from: d  reason: collision with root package name */
        private final int f11148d;

        public a(c.b bVar) {
            this.f11145a = bVar.a();
            this.f11146b = GlUtil.e(bVar.f11129c);
            this.f11147c = GlUtil.e(bVar.f11130d);
            int i8 = bVar.f11128b;
            this.f11148d = i8 != 1 ? i8 != 2 ? 4 : 6 : 5;
        }
    }

    public static boolean c(c cVar) {
        c.a aVar = cVar.f11122a;
        c.a aVar2 = cVar.f11123b;
        return aVar.b() == 1 && aVar.a(0).f11127a == 0 && aVar2.b() == 1 && aVar2.a(0).f11127a == 0;
    }

    public void a(int i8, float[] fArr, boolean z4) {
        a aVar = z4 ? this.f11138c : this.f11137b;
        if (aVar == null) {
            return;
        }
        int i9 = this.f11136a;
        GLES20.glUniformMatrix3fv(this.f11141f, 1, false, i9 == 1 ? z4 ? f11133l : f11132k : i9 == 2 ? z4 ? f11135n : f11134m : f11131j, 0);
        GLES20.glUniformMatrix4fv(this.f11140e, 1, false, fArr, 0);
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(36197, i8);
        GLES20.glUniform1i(this.f11144i, 0);
        try {
            GlUtil.b();
        } catch (GlUtil.GlException e8) {
            Log.e("ProjectionRenderer", "Failed to bind uniforms", e8);
        }
        GLES20.glVertexAttribPointer(this.f11142g, 3, 5126, false, 12, (Buffer) aVar.f11146b);
        try {
            GlUtil.b();
        } catch (GlUtil.GlException e9) {
            Log.e("ProjectionRenderer", "Failed to load position data", e9);
        }
        GLES20.glVertexAttribPointer(this.f11143h, 2, 5126, false, 8, (Buffer) aVar.f11147c);
        try {
            GlUtil.b();
        } catch (GlUtil.GlException e10) {
            Log.e("ProjectionRenderer", "Failed to load texture data", e10);
        }
        GLES20.glDrawArrays(aVar.f11148d, 0, aVar.f11145a);
        try {
            GlUtil.b();
        } catch (GlUtil.GlException e11) {
            Log.e("ProjectionRenderer", "Failed to render", e11);
        }
    }

    public void b() {
        try {
            com.google.android.exoplayer2.util.b bVar = new com.google.android.exoplayer2.util.b("uniform mat4 uMvpMatrix;\nuniform mat3 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec2 aTexCoords;\nvarying vec2 vTexCoords;\n// Standard transformation.\nvoid main() {\n  gl_Position = uMvpMatrix * aPosition;\n  vTexCoords = (uTexMatrix * vec3(aTexCoords, 1)).xy;\n}\n", "// This is required since the texture data is GL_TEXTURE_EXTERNAL_OES.\n#extension GL_OES_EGL_image_external : require\nprecision mediump float;\n// Standard texture rendering shader.\nuniform samplerExternalOES uTexture;\nvarying vec2 vTexCoords;\nvoid main() {\n  gl_FragColor = texture2D(uTexture, vTexCoords);\n}\n");
            this.f11139d = bVar;
            this.f11140e = bVar.j("uMvpMatrix");
            this.f11141f = this.f11139d.j("uTexMatrix");
            this.f11142g = this.f11139d.e("aPosition");
            this.f11143h = this.f11139d.e("aTexCoords");
            this.f11144i = this.f11139d.j("uTexture");
        } catch (GlUtil.GlException e8) {
            Log.e("ProjectionRenderer", "Failed to initialize the program", e8);
        }
    }

    public void d(c cVar) {
        if (c(cVar)) {
            this.f11136a = cVar.f11124c;
            a aVar = new a(cVar.f11122a.a(0));
            this.f11137b = aVar;
            if (!cVar.f11125d) {
                aVar = new a(cVar.f11123b.a(0));
            }
            this.f11138c = aVar;
        }
    }
}
