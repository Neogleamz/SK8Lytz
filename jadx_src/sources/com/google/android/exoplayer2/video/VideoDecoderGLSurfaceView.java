package com.google.android.exoplayer2.video;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import c6.h;
import com.google.android.exoplayer2.util.GlUtil;
import com.google.android.exoplayer2.util.b;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.atomic.AtomicReference;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import l4.i;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class VideoDecoderGLSurfaceView extends GLSurfaceView implements h {

    /* renamed from: a  reason: collision with root package name */
    private final a f11073a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class a implements GLSurfaceView.Renderer {

        /* renamed from: l  reason: collision with root package name */
        private static final float[] f11074l = {1.164f, 1.164f, 1.164f, 0.0f, -0.392f, 2.017f, 1.596f, -0.813f, 0.0f};

        /* renamed from: m  reason: collision with root package name */
        private static final float[] f11075m = {1.164f, 1.164f, 1.164f, 0.0f, -0.213f, 2.112f, 1.793f, -0.533f, 0.0f};

        /* renamed from: n  reason: collision with root package name */
        private static final float[] f11076n = {1.168f, 1.168f, 1.168f, 0.0f, -0.188f, 2.148f, 1.683f, -0.652f, 0.0f};

        /* renamed from: p  reason: collision with root package name */
        private static final String[] f11077p = {"y_tex", "u_tex", "v_tex"};
        private static final FloatBuffer q = GlUtil.e(new float[]{-1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f});

        /* renamed from: a  reason: collision with root package name */
        private final GLSurfaceView f11078a;

        /* renamed from: b  reason: collision with root package name */
        private final int[] f11079b = new int[3];

        /* renamed from: c  reason: collision with root package name */
        private final int[] f11080c = new int[3];

        /* renamed from: d  reason: collision with root package name */
        private final int[] f11081d = new int[3];

        /* renamed from: e  reason: collision with root package name */
        private final int[] f11082e = new int[3];

        /* renamed from: f  reason: collision with root package name */
        private final AtomicReference<i> f11083f = new AtomicReference<>();

        /* renamed from: g  reason: collision with root package name */
        private final FloatBuffer[] f11084g = new FloatBuffer[3];

        /* renamed from: h  reason: collision with root package name */
        private b f11085h;

        /* renamed from: j  reason: collision with root package name */
        private int f11086j;

        /* renamed from: k  reason: collision with root package name */
        private i f11087k;

        public a(GLSurfaceView gLSurfaceView) {
            this.f11078a = gLSurfaceView;
            for (int i8 = 0; i8 < 3; i8++) {
                int[] iArr = this.f11081d;
                this.f11082e[i8] = -1;
                iArr[i8] = -1;
            }
        }

        private void b() {
            try {
                GLES20.glGenTextures(3, this.f11079b, 0);
                for (int i8 = 0; i8 < 3; i8++) {
                    GLES20.glUniform1i(this.f11085h.j(f11077p[i8]), i8);
                    GLES20.glActiveTexture(33984 + i8);
                    GlUtil.a(3553, this.f11079b[i8]);
                }
                GlUtil.b();
            } catch (GlUtil.GlException e8) {
                Log.e("VideoDecoderGLSV", "Failed to set up the textures", e8);
            }
        }

        public void a(i iVar) {
            i andSet = this.f11083f.getAndSet(iVar);
            if (andSet != null) {
                andSet.y();
            }
            this.f11078a.requestRender();
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onDrawFrame(GL10 gl10) {
            i andSet = this.f11083f.getAndSet(null);
            if (andSet == null && this.f11087k == null) {
                return;
            }
            if (andSet != null) {
                i iVar = this.f11087k;
                if (iVar != null) {
                    iVar.y();
                }
                this.f11087k = andSet;
            }
            i iVar2 = (i) b6.a.e(this.f11087k);
            float[] fArr = f11075m;
            int i8 = iVar2.f21622h;
            if (i8 == 1) {
                fArr = f11074l;
            } else if (i8 == 3) {
                fArr = f11076n;
            }
            GLES20.glUniformMatrix3fv(this.f11086j, 1, false, fArr, 0);
            int[] iArr = (int[]) b6.a.e(iVar2.f21621g);
            ByteBuffer[] byteBufferArr = (ByteBuffer[]) b6.a.e(iVar2.f21620f);
            int i9 = 0;
            while (i9 < 3) {
                int i10 = i9 == 0 ? iVar2.f21619e : (iVar2.f21619e + 1) / 2;
                GLES20.glActiveTexture(33984 + i9);
                GLES20.glBindTexture(3553, this.f11079b[i9]);
                GLES20.glPixelStorei(3317, 1);
                GLES20.glTexImage2D(3553, 0, 6409, iArr[i9], i10, 0, 6409, 5121, byteBufferArr[i9]);
                i9++;
            }
            int i11 = (r3[0] + 1) / 2;
            int[] iArr2 = {iVar2.f21618d, i11, i11};
            for (int i12 = 0; i12 < 3; i12++) {
                if (this.f11081d[i12] != iArr2[i12] || this.f11082e[i12] != iArr[i12]) {
                    b6.a.f(iArr[i12] != 0);
                    float f5 = iArr2[i12] / iArr[i12];
                    this.f11084g[i12] = GlUtil.e(new float[]{0.0f, 0.0f, 0.0f, 1.0f, f5, 0.0f, f5, 1.0f});
                    GLES20.glVertexAttribPointer(this.f11080c[i12], 2, 5126, false, 0, (Buffer) this.f11084g[i12]);
                    this.f11081d[i12] = iArr2[i12];
                    this.f11082e[i12] = iArr[i12];
                }
            }
            GLES20.glClear(16384);
            GLES20.glDrawArrays(5, 0, 4);
            try {
                GlUtil.b();
            } catch (GlUtil.GlException e8) {
                Log.e("VideoDecoderGLSV", "Failed to draw a frame", e8);
            }
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceChanged(GL10 gl10, int i8, int i9) {
            GLES20.glViewport(0, 0, i8, i9);
        }

        @Override // android.opengl.GLSurfaceView.Renderer
        public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            try {
                b bVar = new b("varying vec2 interp_tc_y;\nvarying vec2 interp_tc_u;\nvarying vec2 interp_tc_v;\nattribute vec4 in_pos;\nattribute vec2 in_tc_y;\nattribute vec2 in_tc_u;\nattribute vec2 in_tc_v;\nvoid main() {\n  gl_Position = in_pos;\n  interp_tc_y = in_tc_y;\n  interp_tc_u = in_tc_u;\n  interp_tc_v = in_tc_v;\n}\n", "precision mediump float;\nvarying vec2 interp_tc_y;\nvarying vec2 interp_tc_u;\nvarying vec2 interp_tc_v;\nuniform sampler2D y_tex;\nuniform sampler2D u_tex;\nuniform sampler2D v_tex;\nuniform mat3 mColorConversion;\nvoid main() {\n  vec3 yuv;\n  yuv.x = texture2D(y_tex, interp_tc_y).r - 0.0625;\n  yuv.y = texture2D(u_tex, interp_tc_u).r - 0.5;\n  yuv.z = texture2D(v_tex, interp_tc_v).r - 0.5;\n  gl_FragColor = vec4(mColorConversion * yuv, 1.0);\n}\n");
                this.f11085h = bVar;
                GLES20.glVertexAttribPointer(bVar.e("in_pos"), 2, 5126, false, 0, (Buffer) q);
                this.f11080c[0] = this.f11085h.e("in_tc_y");
                this.f11080c[1] = this.f11085h.e("in_tc_u");
                this.f11080c[2] = this.f11085h.e("in_tc_v");
                this.f11086j = this.f11085h.j("mColorConversion");
                GlUtil.b();
                b();
                GlUtil.b();
            } catch (GlUtil.GlException e8) {
                Log.e("VideoDecoderGLSV", "Failed to set up the textures and program", e8);
            }
        }
    }

    public VideoDecoderGLSurfaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        a aVar = new a(this);
        this.f11073a = aVar;
        setPreserveEGLContextOnPause(true);
        setEGLContextClientVersion(2);
        setRenderer(aVar);
        setRenderMode(0);
    }

    @Deprecated
    public h getVideoDecoderOutputBufferRenderer() {
        return this;
    }

    public void setOutputBuffer(i iVar) {
        this.f11073a.a(iVar);
    }
}
