package androidx.camera.core.impl.utils;

import androidx.camera.core.impl.utils.ExifData;
import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h extends FilterOutputStream {

    /* renamed from: g  reason: collision with root package name */
    private static final byte[] f2643g = "Exif\u0000\u0000".getBytes(g.f2635e);

    /* renamed from: a  reason: collision with root package name */
    private final ExifData f2644a;

    /* renamed from: b  reason: collision with root package name */
    private final byte[] f2645b;

    /* renamed from: c  reason: collision with root package name */
    private final ByteBuffer f2646c;

    /* renamed from: d  reason: collision with root package name */
    private int f2647d;

    /* renamed from: e  reason: collision with root package name */
    private int f2648e;

    /* renamed from: f  reason: collision with root package name */
    private int f2649f;

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class a {
        public static boolean a(short s8) {
            return (s8 < -64 || s8 > -49 || s8 == -60 || s8 == -56 || s8 == -52) ? false : true;
        }
    }

    public h(OutputStream outputStream, ExifData exifData) {
        super(new BufferedOutputStream(outputStream, 65536));
        this.f2645b = new byte[1];
        this.f2646c = ByteBuffer.allocate(4);
        this.f2647d = 0;
        this.f2644a = exifData;
    }

    private int a(int i8, byte[] bArr, int i9, int i10) {
        int min = Math.min(i10, i8 - this.f2646c.position());
        this.f2646c.put(bArr, i9, min);
        return min;
    }

    private void b(b bVar) {
        i[] iVarArr;
        i[][] iVarArr2 = ExifData.f2598i;
        int[] iArr = new int[iVarArr2.length];
        int[] iArr2 = new int[iVarArr2.length];
        for (i iVar : ExifData.f2596g) {
            for (int i8 = 0; i8 < ExifData.f2598i.length; i8++) {
                this.f2644a.c(i8).remove(iVar.f2651b);
            }
        }
        if (!this.f2644a.c(1).isEmpty()) {
            this.f2644a.c(0).put(ExifData.f2596g[1].f2651b, g.f(0L, this.f2644a.d()));
        }
        if (!this.f2644a.c(2).isEmpty()) {
            this.f2644a.c(0).put(ExifData.f2596g[2].f2651b, g.f(0L, this.f2644a.d()));
        }
        if (!this.f2644a.c(3).isEmpty()) {
            this.f2644a.c(1).put(ExifData.f2596g[3].f2651b, g.f(0L, this.f2644a.d()));
        }
        for (int i9 = 0; i9 < ExifData.f2598i.length; i9++) {
            int i10 = 0;
            for (Map.Entry<String, g> entry : this.f2644a.c(i9).entrySet()) {
                int j8 = entry.getValue().j();
                if (j8 > 4) {
                    i10 += j8;
                }
            }
            iArr2[i9] = iArr2[i9] + i10;
        }
        int i11 = 8;
        for (int i12 = 0; i12 < ExifData.f2598i.length; i12++) {
            if (!this.f2644a.c(i12).isEmpty()) {
                iArr[i12] = i11;
                i11 += (this.f2644a.c(i12).size() * 12) + 2 + 4 + iArr2[i12];
            }
        }
        int i13 = i11 + 8;
        if (!this.f2644a.c(1).isEmpty()) {
            this.f2644a.c(0).put(ExifData.f2596g[1].f2651b, g.f(iArr[1], this.f2644a.d()));
        }
        if (!this.f2644a.c(2).isEmpty()) {
            this.f2644a.c(0).put(ExifData.f2596g[2].f2651b, g.f(iArr[2], this.f2644a.d()));
        }
        if (!this.f2644a.c(3).isEmpty()) {
            this.f2644a.c(1).put(ExifData.f2596g[3].f2651b, g.f(iArr[3], this.f2644a.d()));
        }
        bVar.h(i13);
        bVar.write(f2643g);
        bVar.d(this.f2644a.d() == ByteOrder.BIG_ENDIAN ? (short) 19789 : (short) 18761);
        bVar.a(this.f2644a.d());
        bVar.h(42);
        bVar.f(8L);
        for (int i14 = 0; i14 < ExifData.f2598i.length; i14++) {
            if (!this.f2644a.c(i14).isEmpty()) {
                bVar.h(this.f2644a.c(i14).size());
                int size = iArr[i14] + 2 + (this.f2644a.c(i14).size() * 12) + 4;
                for (Map.Entry<String, g> entry2 : this.f2644a.c(i14).entrySet()) {
                    int i15 = ((i) androidx.core.util.h.i(ExifData.b.f2610f.get(i14).get(entry2.getKey()), "Tag not supported: " + entry2.getKey() + ". Tag needs to be ported from ExifInterface to ExifData.")).f2650a;
                    g value = entry2.getValue();
                    int j9 = value.j();
                    bVar.h(i15);
                    bVar.h(value.f2639a);
                    bVar.c(value.f2640b);
                    if (j9 > 4) {
                        bVar.f(size);
                        size += j9;
                    } else {
                        bVar.write(value.f2642d);
                        if (j9 < 4) {
                            while (j9 < 4) {
                                bVar.b(0);
                                j9++;
                            }
                        }
                    }
                }
                bVar.f(0L);
                for (Map.Entry<String, g> entry3 : this.f2644a.c(i14).entrySet()) {
                    byte[] bArr = entry3.getValue().f2642d;
                    if (bArr.length > 4) {
                        bVar.write(bArr, 0, bArr.length);
                    }
                }
            }
        }
        bVar.a(ByteOrder.BIG_ENDIAN);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i8) {
        byte[] bArr = this.f2645b;
        bArr[0] = (byte) (i8 & 255);
        write(bArr);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) {
        write(bArr, 0, bArr.length);
    }

    /* JADX WARN: Code restructure failed: missing block: B:45:0x0105, code lost:
        if (r9 <= 0) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x0107, code lost:
        ((java.io.FilterOutputStream) r6).out.write(r7, r8, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x010c, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:?, code lost:
        return;
     */
    @Override // java.io.FilterOutputStream, java.io.OutputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void write(byte[] r7, int r8, int r9) {
        /*
            Method dump skipped, instructions count: 269
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.camera.core.impl.utils.h.write(byte[], int, int):void");
    }
}
