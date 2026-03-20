package com.google.android.material.circularreveal;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import com.google.android.material.circularreveal.c;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class b {

    /* renamed from: j  reason: collision with root package name */
    public static final int f17740j;

    /* renamed from: a  reason: collision with root package name */
    private final a f17741a;

    /* renamed from: b  reason: collision with root package name */
    private final View f17742b;

    /* renamed from: c  reason: collision with root package name */
    private final Path f17743c;

    /* renamed from: d  reason: collision with root package name */
    private final Paint f17744d;

    /* renamed from: e  reason: collision with root package name */
    private final Paint f17745e;

    /* renamed from: f  reason: collision with root package name */
    private c.e f17746f;

    /* renamed from: g  reason: collision with root package name */
    private Drawable f17747g;

    /* renamed from: h  reason: collision with root package name */
    private boolean f17748h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f17749i;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface a {
        void c(Canvas canvas);

        boolean d();
    }

    static {
        int i8 = Build.VERSION.SDK_INT;
        f17740j = i8 >= 21 ? 2 : i8 >= 18 ? 1 : 0;
    }

    public b(a aVar) {
        this.f17741a = aVar;
        View view = (View) aVar;
        this.f17742b = view;
        view.setWillNotDraw(false);
        this.f17743c = new Path();
        this.f17744d = new Paint(7);
        Paint paint = new Paint(1);
        this.f17745e = paint;
        paint.setColor(0);
    }

    private void d(Canvas canvas) {
        if (o()) {
            Rect bounds = this.f17747g.getBounds();
            float width = this.f17746f.f17754a - (bounds.width() / 2.0f);
            float height = this.f17746f.f17755b - (bounds.height() / 2.0f);
            canvas.translate(width, height);
            this.f17747g.draw(canvas);
            canvas.translate(-width, -height);
        }
    }

    private float g(c.e eVar) {
        return s7.a.b(eVar.f17754a, eVar.f17755b, 0.0f, 0.0f, this.f17742b.getWidth(), this.f17742b.getHeight());
    }

    private void i() {
        if (f17740j == 1) {
            this.f17743c.rewind();
            c.e eVar = this.f17746f;
            if (eVar != null) {
                this.f17743c.addCircle(eVar.f17754a, eVar.f17755b, eVar.f17756c, Path.Direction.CW);
            }
        }
        this.f17742b.invalidate();
    }

    private boolean n() {
        c.e eVar = this.f17746f;
        boolean z4 = eVar == null || eVar.a();
        return f17740j == 0 ? !z4 && this.f17749i : !z4;
    }

    private boolean o() {
        return (this.f17748h || this.f17747g == null || this.f17746f == null) ? false : true;
    }

    private boolean p() {
        return (this.f17748h || Color.alpha(this.f17745e.getColor()) == 0) ? false : true;
    }

    public void a() {
        if (f17740j == 0) {
            this.f17748h = true;
            this.f17749i = false;
            this.f17742b.buildDrawingCache();
            Bitmap drawingCache = this.f17742b.getDrawingCache();
            if (drawingCache == null && this.f17742b.getWidth() != 0 && this.f17742b.getHeight() != 0) {
                drawingCache = Bitmap.createBitmap(this.f17742b.getWidth(), this.f17742b.getHeight(), Bitmap.Config.ARGB_8888);
                this.f17742b.draw(new Canvas(drawingCache));
            }
            if (drawingCache != null) {
                Paint paint = this.f17744d;
                Shader.TileMode tileMode = Shader.TileMode.CLAMP;
                paint.setShader(new BitmapShader(drawingCache, tileMode, tileMode));
            }
            this.f17748h = false;
            this.f17749i = true;
        }
    }

    public void b() {
        if (f17740j == 0) {
            this.f17749i = false;
            this.f17742b.destroyDrawingCache();
            this.f17744d.setShader(null);
            this.f17742b.invalidate();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0019, code lost:
        if (p() != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x008b, code lost:
        if (p() != false) goto L14;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x008d, code lost:
        r9.drawRect(0.0f, 0.0f, r8.f17742b.getWidth(), r8.f17742b.getHeight(), r8.f17745e);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void c(android.graphics.Canvas r9) {
        /*
            r8 = this;
            boolean r0 = r8.n()
            if (r0 == 0) goto L82
            int r0 = com.google.android.material.circularreveal.b.f17740j
            if (r0 == 0) goto L61
            r1 = 1
            if (r0 == r1) goto L33
            r1 = 2
            if (r0 != r1) goto L1c
            com.google.android.material.circularreveal.b$a r0 = r8.f17741a
            r0.c(r9)
            boolean r0 = r8.p()
            if (r0 == 0) goto La3
            goto L8d
        L1c:
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "Unsupported strategy "
            r1.append(r2)
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            r9.<init>(r0)
            throw r9
        L33:
            int r0 = r9.save()
            android.graphics.Path r1 = r8.f17743c
            r9.clipPath(r1)
            com.google.android.material.circularreveal.b$a r1 = r8.f17741a
            r1.c(r9)
            boolean r1 = r8.p()
            if (r1 == 0) goto L5d
            r3 = 0
            r4 = 0
            android.view.View r1 = r8.f17742b
            int r1 = r1.getWidth()
            float r5 = (float) r1
            android.view.View r1 = r8.f17742b
            int r1 = r1.getHeight()
            float r6 = (float) r1
            android.graphics.Paint r7 = r8.f17745e
            r2 = r9
            r2.drawRect(r3, r4, r5, r6, r7)
        L5d:
            r9.restoreToCount(r0)
            goto La3
        L61:
            com.google.android.material.circularreveal.c$e r0 = r8.f17746f
            float r1 = r0.f17754a
            float r2 = r0.f17755b
            float r0 = r0.f17756c
            android.graphics.Paint r3 = r8.f17744d
            r9.drawCircle(r1, r2, r0, r3)
            boolean r0 = r8.p()
            if (r0 == 0) goto La3
            com.google.android.material.circularreveal.c$e r0 = r8.f17746f
            float r1 = r0.f17754a
            float r2 = r0.f17755b
            float r0 = r0.f17756c
            android.graphics.Paint r3 = r8.f17745e
            r9.drawCircle(r1, r2, r0, r3)
            goto La3
        L82:
            com.google.android.material.circularreveal.b$a r0 = r8.f17741a
            r0.c(r9)
            boolean r0 = r8.p()
            if (r0 == 0) goto La3
        L8d:
            r2 = 0
            r3 = 0
            android.view.View r0 = r8.f17742b
            int r0 = r0.getWidth()
            float r4 = (float) r0
            android.view.View r0 = r8.f17742b
            int r0 = r0.getHeight()
            float r5 = (float) r0
            android.graphics.Paint r6 = r8.f17745e
            r1 = r9
            r1.drawRect(r2, r3, r4, r5, r6)
        La3:
            r8.d(r9)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.circularreveal.b.c(android.graphics.Canvas):void");
    }

    public Drawable e() {
        return this.f17747g;
    }

    public int f() {
        return this.f17745e.getColor();
    }

    public c.e h() {
        c.e eVar = this.f17746f;
        if (eVar == null) {
            return null;
        }
        c.e eVar2 = new c.e(eVar);
        if (eVar2.a()) {
            eVar2.f17756c = g(eVar2);
        }
        return eVar2;
    }

    public boolean j() {
        return this.f17741a.d() && !n();
    }

    public void k(Drawable drawable) {
        this.f17747g = drawable;
        this.f17742b.invalidate();
    }

    public void l(int i8) {
        this.f17745e.setColor(i8);
        this.f17742b.invalidate();
    }

    public void m(c.e eVar) {
        if (eVar == null) {
            this.f17746f = null;
        } else {
            c.e eVar2 = this.f17746f;
            if (eVar2 == null) {
                this.f17746f = new c.e(eVar);
            } else {
                eVar2.c(eVar);
            }
            if (s7.a.c(eVar.f17756c, g(eVar), 1.0E-4f)) {
                this.f17746f.f17756c = Float.MAX_VALUE;
            }
        }
        i();
    }
}
