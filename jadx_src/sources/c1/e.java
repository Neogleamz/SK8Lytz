package c1;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.util.Log;
import java.nio.Buffer;
import java.nio.FloatBuffer;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class e {

    /* renamed from: h  reason: collision with root package name */
    public static final float[] f8285h;

    /* renamed from: i  reason: collision with root package name */
    public static final float[] f8286i;

    /* renamed from: a  reason: collision with root package name */
    private int f8287a;

    /* renamed from: b  reason: collision with root package name */
    private int f8288b;

    /* renamed from: c  reason: collision with root package name */
    private int f8289c;

    /* renamed from: d  reason: collision with root package name */
    private int f8290d;

    /* renamed from: e  reason: collision with root package name */
    private int f8291e;

    /* renamed from: f  reason: collision with root package name */
    private int f8292f;

    /* renamed from: g  reason: collision with root package name */
    private int f8293g;

    static {
        float[] fArr = new float[16];
        f8285h = fArr;
        Matrix.setIdentityM(fArr, 0);
        float[] fArr2 = new float[16];
        f8286i = fArr2;
        Matrix.setIdentityM(fArr2, 0);
        Matrix.translateM(fArr2, 0, 0.0f, 1.0f, 0.0f);
        Matrix.scaleM(fArr2, 0, 1.0f, -1.0f, 1.0f);
    }

    public e(int i8) {
        String str;
        this.f8287a = i8;
        if (i8 == 0) {
            this.f8293g = 3553;
            str = "precision mediump float;\nvarying vec2 vTextureCoord;\nuniform sampler2D sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
        } else if (i8 != 1) {
            throw new RuntimeException("Unhandled type " + i8);
        } else {
            this.f8293g = 36197;
            str = "#extension GL_OES_EGL_image_external : require\nprecision mediump float;\nvarying vec2 vTextureCoord;\nuniform samplerExternalOES sTexture;\nvoid main() {\n    gl_FragColor = texture2D(sTexture, vTextureCoord);\n}\n";
        }
        this.f8288b = c("uniform mat4 uMVPMatrix;\nuniform mat4 uTexMatrix;\nattribute vec4 aPosition;\nattribute vec4 aTextureCoord;\nvarying vec2 vTextureCoord;\nvoid main() {\n    gl_Position = uMVPMatrix * aPosition;\n    vTextureCoord = (uTexMatrix * aTextureCoord).xy;\n}\n", str);
        int i9 = this.f8288b;
        if (i9 == 0) {
            throw new RuntimeException("Unable to create program");
        }
        int glGetAttribLocation = GLES20.glGetAttribLocation(i9, "aPosition");
        this.f8291e = glGetAttribLocation;
        b(glGetAttribLocation, "aPosition");
        int glGetAttribLocation2 = GLES20.glGetAttribLocation(this.f8288b, "aTextureCoord");
        this.f8292f = glGetAttribLocation2;
        b(glGetAttribLocation2, "aTextureCoord");
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.f8288b, "uMVPMatrix");
        this.f8289c = glGetUniformLocation;
        b(glGetUniformLocation, "uMVPMatrix");
        int glGetUniformLocation2 = GLES20.glGetUniformLocation(this.f8288b, "uTexMatrix");
        this.f8290d = glGetUniformLocation2;
        b(glGetUniformLocation2, "uTexMatrix");
    }

    public static void a(String str) {
        int glGetError;
        if (GLES20.glGetError() == 0) {
            return;
        }
        String str2 = str + ": glError 0x" + Integer.toHexString(glGetError);
        Log.e("Texture2dProgram", str2);
        throw new RuntimeException(str2);
    }

    public static void b(int i8, String str) {
        if (i8 >= 0) {
            return;
        }
        throw new RuntimeException("Unable to locate '" + str + "' in program");
    }

    public static int c(String str, String str2) {
        int f5;
        int f8 = f(35633, str);
        if (f8 == 0 || (f5 = f(35632, str2)) == 0) {
            return 0;
        }
        int glCreateProgram = GLES20.glCreateProgram();
        a("glCreateProgram");
        if (glCreateProgram == 0) {
            Log.e("Texture2dProgram", "Could not create program");
        }
        GLES20.glAttachShader(glCreateProgram, f8);
        a("glAttachShader");
        GLES20.glAttachShader(glCreateProgram, f5);
        a("glAttachShader");
        GLES20.glLinkProgram(glCreateProgram);
        int[] iArr = new int[1];
        GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
        if (iArr[0] != 1) {
            Log.e("Texture2dProgram", "Could not link program: ");
            Log.e("Texture2dProgram", GLES20.glGetProgramInfoLog(glCreateProgram));
            GLES20.glDeleteProgram(glCreateProgram);
            return 0;
        }
        return glCreateProgram;
    }

    public static int f(int i8, String str) {
        int glCreateShader = GLES20.glCreateShader(i8);
        a("glCreateShader type=" + i8);
        GLES20.glShaderSource(glCreateShader, str);
        GLES20.glCompileShader(glCreateShader);
        int[] iArr = new int[1];
        GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
        if (iArr[0] == 0) {
            Log.e("Texture2dProgram", "Could not compile shader " + i8 + ":");
            StringBuilder sb = new StringBuilder();
            sb.append(" ");
            sb.append(GLES20.glGetShaderInfoLog(glCreateShader));
            Log.e("Texture2dProgram", sb.toString());
            GLES20.glDeleteShader(glCreateShader);
            return 0;
        }
        return glCreateShader;
    }

    public int d() {
        int[] iArr = new int[1];
        GLES20.glGenTextures(1, iArr, 0);
        a("glGenTextures");
        int i8 = iArr[0];
        GLES20.glBindTexture(this.f8293g, i8);
        a("glBindTexture " + i8);
        GLES20.glTexParameterf(this.f8293g, 10241, 9728.0f);
        int i9 = this.f8293g;
        GLES20.glTexParameterf(i9, 10240, i9 != 3553 ? 9729.0f : 9728.0f);
        GLES20.glTexParameteri(this.f8293g, 10242, 33071);
        GLES20.glTexParameteri(this.f8293g, 10243, 33071);
        a("glTexParameter");
        return i8;
    }

    public void e(float[] fArr, FloatBuffer floatBuffer, int i8, int i9, int i10, int i11, float[] fArr2, FloatBuffer floatBuffer2, int i12, int i13) {
        a("draw start");
        GLES20.glUseProgram(this.f8288b);
        a("glUseProgram");
        GLES20.glActiveTexture(33984);
        GLES20.glBindTexture(this.f8293g, i12);
        GLES20.glUniformMatrix4fv(this.f8289c, 1, false, fArr, 0);
        a("glUniformMatrix4fv");
        GLES20.glUniformMatrix4fv(this.f8290d, 1, false, fArr2, 0);
        a("glUniformMatrix4fv");
        GLES20.glEnableVertexAttribArray(this.f8291e);
        a("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(this.f8291e, i10, 5126, false, i11, (Buffer) floatBuffer);
        a("glVertexAttribPointer");
        GLES20.glEnableVertexAttribArray(this.f8292f);
        a("glEnableVertexAttribArray");
        GLES20.glVertexAttribPointer(this.f8292f, 2, 5126, false, i13, (Buffer) floatBuffer2);
        a("glVertexAttribPointer");
        GLES20.glDrawArrays(5, i8, i9);
        a("glDrawArrays");
        GLES20.glDisableVertexAttribArray(this.f8291e);
        GLES20.glDisableVertexAttribArray(this.f8292f);
        GLES20.glBindTexture(this.f8293g, 0);
        GLES20.glUseProgram(0);
    }

    public void g(int i8, Bitmap bitmap) {
        GLES20.glBindTexture(this.f8293g, i8);
        GLUtils.texImage2D(this.f8293g, 0, bitmap, 0);
    }

    public void h() {
        Log.d("Texture2dProgram", "deleting program " + this.f8288b);
        GLES20.glDeleteProgram(this.f8288b);
        this.f8288b = -1;
    }
}
