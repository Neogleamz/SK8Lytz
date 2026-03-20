package w7;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {

    /* renamed from: i  reason: collision with root package name */
    private static final int[] f23674i = new int[3];

    /* renamed from: j  reason: collision with root package name */
    private static final float[] f23675j = {0.0f, 0.5f, 1.0f};

    /* renamed from: k  reason: collision with root package name */
    private static final int[] f23676k = new int[4];

    /* renamed from: l  reason: collision with root package name */
    private static final float[] f23677l = {0.0f, 0.0f, 0.5f, 1.0f};

    /* renamed from: a  reason: collision with root package name */
    private final Paint f23678a;

    /* renamed from: b  reason: collision with root package name */
    private final Paint f23679b;

    /* renamed from: c  reason: collision with root package name */
    private final Paint f23680c;

    /* renamed from: d  reason: collision with root package name */
    private int f23681d;

    /* renamed from: e  reason: collision with root package name */
    private int f23682e;

    /* renamed from: f  reason: collision with root package name */
    private int f23683f;

    /* renamed from: g  reason: collision with root package name */
    private final Path f23684g;

    /* renamed from: h  reason: collision with root package name */
    private Paint f23685h;

    public a() {
        this(-16777216);
    }

    public a(int i8) {
        this.f23684g = new Path();
        this.f23685h = new Paint();
        this.f23678a = new Paint();
        d(i8);
        this.f23685h.setColor(0);
        Paint paint = new Paint(4);
        this.f23679b = paint;
        paint.setStyle(Paint.Style.FILL);
        this.f23680c = new Paint(paint);
    }

    public void a(Canvas canvas, Matrix matrix, RectF rectF, int i8, float f5, float f8) {
        boolean z4 = f8 < 0.0f;
        Path path = this.f23684g;
        if (z4) {
            int[] iArr = f23676k;
            iArr[0] = 0;
            iArr[1] = this.f23683f;
            iArr[2] = this.f23682e;
            iArr[3] = this.f23681d;
        } else {
            path.rewind();
            path.moveTo(rectF.centerX(), rectF.centerY());
            path.arcTo(rectF, f5, f8);
            path.close();
            float f9 = -i8;
            rectF.inset(f9, f9);
            int[] iArr2 = f23676k;
            iArr2[0] = 0;
            iArr2[1] = this.f23681d;
            iArr2[2] = this.f23682e;
            iArr2[3] = this.f23683f;
        }
        float width = rectF.width() / 2.0f;
        if (width <= 0.0f) {
            return;
        }
        float f10 = 1.0f - (i8 / width);
        float[] fArr = f23677l;
        fArr[1] = f10;
        fArr[2] = ((1.0f - f10) / 2.0f) + f10;
        this.f23679b.setShader(new RadialGradient(rectF.centerX(), rectF.centerY(), width, f23676k, fArr, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        canvas.scale(1.0f, rectF.height() / rectF.width());
        if (!z4) {
            canvas.clipPath(path, Region.Op.DIFFERENCE);
            canvas.drawPath(path, this.f23685h);
        }
        canvas.drawArc(rectF, f5, f8, true, this.f23679b);
        canvas.restore();
    }

    public void b(Canvas canvas, Matrix matrix, RectF rectF, int i8) {
        rectF.bottom += i8;
        rectF.offset(0.0f, -i8);
        int[] iArr = f23674i;
        iArr[0] = this.f23683f;
        iArr[1] = this.f23682e;
        iArr[2] = this.f23681d;
        Paint paint = this.f23680c;
        float f5 = rectF.left;
        paint.setShader(new LinearGradient(f5, rectF.top, f5, rectF.bottom, iArr, f23675j, Shader.TileMode.CLAMP));
        canvas.save();
        canvas.concat(matrix);
        canvas.drawRect(rectF, this.f23680c);
        canvas.restore();
    }

    public Paint c() {
        return this.f23678a;
    }

    public void d(int i8) {
        this.f23681d = androidx.core.graphics.b.p(i8, 68);
        this.f23682e = androidx.core.graphics.b.p(i8, 20);
        this.f23683f = androidx.core.graphics.b.p(i8, 0);
        this.f23678a.setColor(this.f23681d);
    }
}
