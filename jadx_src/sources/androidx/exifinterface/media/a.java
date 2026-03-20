package androidx.exifinterface.media;

import android.content.res.AssetManager;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import androidx.exifinterface.media.b;
import com.daimajia.numberprogressbar.BuildConfig;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class a {
    private static SimpleDateFormat U;
    private static SimpleDateFormat V;
    private static final e[] Z;

    /* renamed from: a0  reason: collision with root package name */
    private static final e[] f5307a0;

    /* renamed from: b0  reason: collision with root package name */
    private static final e[] f5308b0;

    /* renamed from: c0  reason: collision with root package name */
    private static final e[] f5309c0;

    /* renamed from: d0  reason: collision with root package name */
    private static final e[] f5310d0;

    /* renamed from: e0  reason: collision with root package name */
    private static final e f5311e0;

    /* renamed from: f0  reason: collision with root package name */
    private static final e[] f5312f0;

    /* renamed from: g0  reason: collision with root package name */
    private static final e[] f5313g0;

    /* renamed from: h0  reason: collision with root package name */
    private static final e[] f5314h0;

    /* renamed from: i0  reason: collision with root package name */
    private static final e[] f5315i0;

    /* renamed from: j0  reason: collision with root package name */
    static final e[][] f5316j0;

    /* renamed from: k0  reason: collision with root package name */
    private static final e[] f5317k0;

    /* renamed from: l0  reason: collision with root package name */
    private static final HashMap<Integer, e>[] f5318l0;

    /* renamed from: m0  reason: collision with root package name */
    private static final HashMap<String, e>[] f5319m0;

    /* renamed from: n0  reason: collision with root package name */
    private static final HashSet<String> f5320n0;

    /* renamed from: o0  reason: collision with root package name */
    private static final HashMap<Integer, Integer> f5321o0;

    /* renamed from: p0  reason: collision with root package name */
    static final Charset f5322p0;

    /* renamed from: q0  reason: collision with root package name */
    static final byte[] f5323q0;

    /* renamed from: r0  reason: collision with root package name */
    private static final byte[] f5324r0;

    /* renamed from: s0  reason: collision with root package name */
    private static final Pattern f5325s0;

    /* renamed from: t0  reason: collision with root package name */
    private static final Pattern f5326t0;

    /* renamed from: u0  reason: collision with root package name */
    private static final Pattern f5327u0;

    /* renamed from: v0  reason: collision with root package name */
    private static final Pattern f5329v0;

    /* renamed from: a  reason: collision with root package name */
    private String f5334a;

    /* renamed from: b  reason: collision with root package name */
    private FileDescriptor f5335b;

    /* renamed from: c  reason: collision with root package name */
    private AssetManager.AssetInputStream f5336c;

    /* renamed from: d  reason: collision with root package name */
    private int f5337d;

    /* renamed from: e  reason: collision with root package name */
    private boolean f5338e;

    /* renamed from: f  reason: collision with root package name */
    private final HashMap<String, d>[] f5339f;

    /* renamed from: g  reason: collision with root package name */
    private Set<Integer> f5340g;

    /* renamed from: h  reason: collision with root package name */
    private ByteOrder f5341h;

    /* renamed from: i  reason: collision with root package name */
    private boolean f5342i;

    /* renamed from: j  reason: collision with root package name */
    private boolean f5343j;

    /* renamed from: k  reason: collision with root package name */
    private boolean f5344k;

    /* renamed from: l  reason: collision with root package name */
    private int f5345l;

    /* renamed from: m  reason: collision with root package name */
    private int f5346m;

    /* renamed from: n  reason: collision with root package name */
    private byte[] f5347n;

    /* renamed from: o  reason: collision with root package name */
    private int f5348o;

    /* renamed from: p  reason: collision with root package name */
    private int f5349p;
    private int q;

    /* renamed from: r  reason: collision with root package name */
    private int f5350r;

    /* renamed from: s  reason: collision with root package name */
    private int f5351s;

    /* renamed from: t  reason: collision with root package name */
    private boolean f5352t;

    /* renamed from: u  reason: collision with root package name */
    private boolean f5353u;

    /* renamed from: v  reason: collision with root package name */
    private static final boolean f5328v = Log.isLoggable("ExifInterface", 3);

    /* renamed from: w  reason: collision with root package name */
    private static final List<Integer> f5330w = Arrays.asList(1, 6, 3, 8);

    /* renamed from: x  reason: collision with root package name */
    private static final List<Integer> f5331x = Arrays.asList(2, 7, 4, 5);

    /* renamed from: y  reason: collision with root package name */
    public static final int[] f5332y = {8, 8, 8};

    /* renamed from: z  reason: collision with root package name */
    public static final int[] f5333z = {4};
    public static final int[] A = {8};
    static final byte[] B = {-1, -40, -1};
    private static final byte[] C = {102, 116, 121, 112};
    private static final byte[] D = {109, 105, 102, 49};
    private static final byte[] E = {104, 101, 105, 99};
    private static final byte[] F = {79, 76, 89, 77, 80, 0};
    private static final byte[] G = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    private static final byte[] H = {-119, 80, 78, 71, 13, 10, 26, 10};
    private static final byte[] I = {101, 88, 73, 102};
    private static final byte[] J = {73, 72, 68, 82};
    private static final byte[] K = {73, 69, 78, 68};
    private static final byte[] L = {82, 73, 70, 70};
    private static final byte[] M = {87, 69, 66, 80};
    private static final byte[] N = {69, 88, 73, 70};
    private static final byte[] O = {-99, 1, 42};
    private static final byte[] P = "VP8X".getBytes(Charset.defaultCharset());
    private static final byte[] Q = "VP8L".getBytes(Charset.defaultCharset());
    private static final byte[] R = "VP8 ".getBytes(Charset.defaultCharset());
    private static final byte[] S = "ANIM".getBytes(Charset.defaultCharset());
    private static final byte[] T = "ANMF".getBytes(Charset.defaultCharset());
    static final String[] W = {BuildConfig.FLAVOR, "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};
    static final int[] X = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    static final byte[] Y = {65, 83, 67, 73, 73, 0, 0, 0};

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: androidx.exifinterface.media.a$a  reason: collision with other inner class name */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public class C0052a extends MediaDataSource {

        /* renamed from: a  reason: collision with root package name */
        long f5354a;

        /* renamed from: b  reason: collision with root package name */
        final /* synthetic */ g f5355b;

        C0052a(g gVar) {
            this.f5355b = gVar;
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // android.media.MediaDataSource
        public long getSize() {
            return -1L;
        }

        @Override // android.media.MediaDataSource
        public int readAt(long j8, byte[] bArr, int i8, int i9) {
            if (i9 == 0) {
                return 0;
            }
            if (j8 < 0) {
                return -1;
            }
            try {
                long j9 = this.f5354a;
                if (j9 != j8) {
                    if (j9 >= 0 && j8 >= j9 + this.f5355b.available()) {
                        return -1;
                    }
                    this.f5355b.h(j8);
                    this.f5354a = j8;
                }
                if (i9 > this.f5355b.available()) {
                    i9 = this.f5355b.available();
                }
                int read = this.f5355b.read(bArr, i8, i9);
                if (read >= 0) {
                    this.f5354a += read;
                    return read;
                }
            } catch (IOException unused) {
            }
            this.f5354a = -1L;
            return -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b extends InputStream implements DataInput {

        /* renamed from: a  reason: collision with root package name */
        protected final DataInputStream f5357a;

        /* renamed from: b  reason: collision with root package name */
        protected int f5358b;

        /* renamed from: c  reason: collision with root package name */
        private ByteOrder f5359c;

        /* renamed from: d  reason: collision with root package name */
        private byte[] f5360d;

        /* renamed from: e  reason: collision with root package name */
        private int f5361e;

        b(InputStream inputStream) {
            this(inputStream, ByteOrder.BIG_ENDIAN);
        }

        b(InputStream inputStream, ByteOrder byteOrder) {
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            this.f5357a = dataInputStream;
            dataInputStream.mark(0);
            this.f5358b = 0;
            this.f5359c = byteOrder;
            this.f5361e = inputStream instanceof b ? ((b) inputStream).a() : -1;
        }

        b(byte[] bArr) {
            this(new ByteArrayInputStream(bArr), ByteOrder.BIG_ENDIAN);
            this.f5361e = bArr.length;
        }

        public int a() {
            return this.f5361e;
        }

        @Override // java.io.InputStream
        public int available() {
            return this.f5357a.available();
        }

        public int b() {
            return this.f5358b;
        }

        public long c() {
            return readInt() & 4294967295L;
        }

        public void d(ByteOrder byteOrder) {
            this.f5359c = byteOrder;
        }

        public void f(int i8) {
            int i9 = 0;
            while (i9 < i8) {
                int i10 = i8 - i9;
                int skip = (int) this.f5357a.skip(i10);
                if (skip <= 0) {
                    if (this.f5360d == null) {
                        this.f5360d = new byte[8192];
                    }
                    skip = this.f5357a.read(this.f5360d, 0, Math.min(8192, i10));
                    if (skip == -1) {
                        throw new EOFException("Reached EOF while skipping " + i8 + " bytes.");
                    }
                }
                i9 += skip;
            }
            this.f5358b += i9;
        }

        @Override // java.io.InputStream
        public void mark(int i8) {
            throw new UnsupportedOperationException("Mark is currently unsupported");
        }

        @Override // java.io.InputStream
        public int read() {
            this.f5358b++;
            return this.f5357a.read();
        }

        @Override // java.io.InputStream
        public int read(byte[] bArr, int i8, int i9) {
            int read = this.f5357a.read(bArr, i8, i9);
            this.f5358b += read;
            return read;
        }

        @Override // java.io.DataInput
        public boolean readBoolean() {
            this.f5358b++;
            return this.f5357a.readBoolean();
        }

        @Override // java.io.DataInput
        public byte readByte() {
            this.f5358b++;
            int read = this.f5357a.read();
            if (read >= 0) {
                return (byte) read;
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public char readChar() {
            this.f5358b += 2;
            return this.f5357a.readChar();
        }

        @Override // java.io.DataInput
        public double readDouble() {
            return Double.longBitsToDouble(readLong());
        }

        @Override // java.io.DataInput
        public float readFloat() {
            return Float.intBitsToFloat(readInt());
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr) {
            this.f5358b += bArr.length;
            this.f5357a.readFully(bArr);
        }

        @Override // java.io.DataInput
        public void readFully(byte[] bArr, int i8, int i9) {
            this.f5358b += i9;
            this.f5357a.readFully(bArr, i8, i9);
        }

        @Override // java.io.DataInput
        public int readInt() {
            this.f5358b += 4;
            int read = this.f5357a.read();
            int read2 = this.f5357a.read();
            int read3 = this.f5357a.read();
            int read4 = this.f5357a.read();
            if ((read | read2 | read3 | read4) >= 0) {
                ByteOrder byteOrder = this.f5359c;
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                }
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                }
                throw new IOException("Invalid byte order: " + this.f5359c);
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readLine() {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        @Override // java.io.DataInput
        public long readLong() {
            this.f5358b += 8;
            int read = this.f5357a.read();
            int read2 = this.f5357a.read();
            int read3 = this.f5357a.read();
            int read4 = this.f5357a.read();
            int read5 = this.f5357a.read();
            int read6 = this.f5357a.read();
            int read7 = this.f5357a.read();
            int read8 = this.f5357a.read();
            if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                ByteOrder byteOrder = this.f5359c;
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    return (read8 << 56) + (read7 << 48) + (read6 << 40) + (read5 << 32) + (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                }
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (read << 56) + (read2 << 48) + (read3 << 40) + (read4 << 32) + (read5 << 24) + (read6 << 16) + (read7 << 8) + read8;
                }
                throw new IOException("Invalid byte order: " + this.f5359c);
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public short readShort() {
            this.f5358b += 2;
            int read = this.f5357a.read();
            int read2 = this.f5357a.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.f5359c;
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    return (short) ((read2 << 8) + read);
                }
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (short) ((read << 8) + read2);
                }
                throw new IOException("Invalid byte order: " + this.f5359c);
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public String readUTF() {
            this.f5358b += 2;
            return this.f5357a.readUTF();
        }

        @Override // java.io.DataInput
        public int readUnsignedByte() {
            this.f5358b++;
            return this.f5357a.readUnsignedByte();
        }

        @Override // java.io.DataInput
        public int readUnsignedShort() {
            this.f5358b += 2;
            int read = this.f5357a.read();
            int read2 = this.f5357a.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.f5359c;
                if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                    return (read2 << 8) + read;
                }
                if (byteOrder == ByteOrder.BIG_ENDIAN) {
                    return (read << 8) + read2;
                }
                throw new IOException("Invalid byte order: " + this.f5359c);
            }
            throw new EOFException();
        }

        @Override // java.io.InputStream
        public void reset() {
            throw new UnsupportedOperationException("Reset is currently unsupported");
        }

        @Override // java.io.DataInput
        public int skipBytes(int i8) {
            throw new UnsupportedOperationException("skipBytes is currently unsupported");
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class c extends FilterOutputStream {

        /* renamed from: a  reason: collision with root package name */
        final OutputStream f5362a;

        /* renamed from: b  reason: collision with root package name */
        private ByteOrder f5363b;

        public c(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.f5362a = outputStream;
            this.f5363b = byteOrder;
        }

        public void a(ByteOrder byteOrder) {
            this.f5363b = byteOrder;
        }

        public void b(int i8) {
            this.f5362a.write(i8);
        }

        public void c(int i8) {
            OutputStream outputStream;
            int i9;
            ByteOrder byteOrder = this.f5363b;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.f5362a.write((i8 >>> 0) & 255);
                this.f5362a.write((i8 >>> 8) & 255);
                this.f5362a.write((i8 >>> 16) & 255);
                outputStream = this.f5362a;
                i9 = i8 >>> 24;
            } else if (byteOrder != ByteOrder.BIG_ENDIAN) {
                return;
            } else {
                this.f5362a.write((i8 >>> 24) & 255);
                this.f5362a.write((i8 >>> 16) & 255);
                this.f5362a.write((i8 >>> 8) & 255);
                outputStream = this.f5362a;
                i9 = i8 >>> 0;
            }
            outputStream.write(i9 & 255);
        }

        public void d(short s8) {
            OutputStream outputStream;
            int i8;
            ByteOrder byteOrder = this.f5363b;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.f5362a.write((s8 >>> 0) & 255);
                outputStream = this.f5362a;
                i8 = s8 >>> 8;
            } else if (byteOrder != ByteOrder.BIG_ENDIAN) {
                return;
            } else {
                this.f5362a.write((s8 >>> 8) & 255);
                outputStream = this.f5362a;
                i8 = s8 >>> 0;
            }
            outputStream.write(i8 & 255);
        }

        public void f(long j8) {
            if (j8 > 4294967295L) {
                throw new IllegalArgumentException("val is larger than the maximum value of a 32-bit unsigned integer");
            }
            c((int) j8);
        }

        public void h(int i8) {
            if (i8 > 65535) {
                throw new IllegalArgumentException("val is larger than the maximum value of a 16-bit unsigned integer");
            }
            d((short) i8);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr) {
            this.f5362a.write(bArr);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i8, int i9) {
            this.f5362a.write(bArr, i8, i9);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class d {

        /* renamed from: a  reason: collision with root package name */
        public final int f5364a;

        /* renamed from: b  reason: collision with root package name */
        public final int f5365b;

        /* renamed from: c  reason: collision with root package name */
        public final long f5366c;

        /* renamed from: d  reason: collision with root package name */
        public final byte[] f5367d;

        d(int i8, int i9, long j8, byte[] bArr) {
            this.f5364a = i8;
            this.f5365b = i9;
            this.f5366c = j8;
            this.f5367d = bArr;
        }

        d(int i8, int i9, byte[] bArr) {
            this(i8, i9, -1L, bArr);
        }

        public static d a(String str) {
            if (str.length() != 1 || str.charAt(0) < '0' || str.charAt(0) > '1') {
                byte[] bytes = str.getBytes(a.f5322p0);
                return new d(1, bytes.length, bytes);
            }
            return new d(1, 1, new byte[]{(byte) (str.charAt(0) - '0')});
        }

        public static d b(double[] dArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[12] * dArr.length]);
            wrap.order(byteOrder);
            for (double d8 : dArr) {
                wrap.putDouble(d8);
            }
            return new d(12, dArr.length, wrap.array());
        }

        public static d c(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[9] * iArr.length]);
            wrap.order(byteOrder);
            for (int i8 : iArr) {
                wrap.putInt(i8);
            }
            return new d(9, iArr.length, wrap.array());
        }

        public static d d(f[] fVarArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[10] * fVarArr.length]);
            wrap.order(byteOrder);
            for (f fVar : fVarArr) {
                wrap.putInt((int) fVar.f5372a);
                wrap.putInt((int) fVar.f5373b);
            }
            return new d(10, fVarArr.length, wrap.array());
        }

        public static d e(String str) {
            byte[] bytes = (str + (char) 0).getBytes(a.f5322p0);
            return new d(2, bytes.length, bytes);
        }

        public static d f(long j8, ByteOrder byteOrder) {
            return g(new long[]{j8}, byteOrder);
        }

        public static d g(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[4] * jArr.length]);
            wrap.order(byteOrder);
            for (long j8 : jArr) {
                wrap.putInt((int) j8);
            }
            return new d(4, jArr.length, wrap.array());
        }

        public static d h(f fVar, ByteOrder byteOrder) {
            return i(new f[]{fVar}, byteOrder);
        }

        public static d i(f[] fVarArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[5] * fVarArr.length]);
            wrap.order(byteOrder);
            for (f fVar : fVarArr) {
                wrap.putInt((int) fVar.f5372a);
                wrap.putInt((int) fVar.f5373b);
            }
            return new d(5, fVarArr.length, wrap.array());
        }

        public static d j(int i8, ByteOrder byteOrder) {
            return k(new int[]{i8}, byteOrder);
        }

        public static d k(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[a.X[3] * iArr.length]);
            wrap.order(byteOrder);
            for (int i8 : iArr) {
                wrap.putShort((short) i8);
            }
            return new d(3, iArr.length, wrap.array());
        }

        public double l(ByteOrder byteOrder) {
            Object o5 = o(byteOrder);
            if (o5 != null) {
                if (o5 instanceof String) {
                    return Double.parseDouble((String) o5);
                }
                if (o5 instanceof long[]) {
                    long[] jArr = (long[]) o5;
                    if (jArr.length == 1) {
                        return jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (o5 instanceof int[]) {
                    int[] iArr = (int[]) o5;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (o5 instanceof double[]) {
                    double[] dArr = (double[]) o5;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (o5 instanceof f[]) {
                    f[] fVarArr = (f[]) o5;
                    if (fVarArr.length == 1) {
                        return fVarArr[0].a();
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
            throw new NumberFormatException("NULL can't be converted to a double value");
        }

        public int m(ByteOrder byteOrder) {
            Object o5 = o(byteOrder);
            if (o5 != null) {
                if (o5 instanceof String) {
                    return Integer.parseInt((String) o5);
                }
                if (o5 instanceof long[]) {
                    long[] jArr = (long[]) o5;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (o5 instanceof int[]) {
                    int[] iArr = (int[]) o5;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
            throw new NumberFormatException("NULL can't be converted to a integer value");
        }

        public String n(ByteOrder byteOrder) {
            Object o5 = o(byteOrder);
            if (o5 == null) {
                return null;
            }
            if (o5 instanceof String) {
                return (String) o5;
            }
            StringBuilder sb = new StringBuilder();
            int i8 = 0;
            if (o5 instanceof long[]) {
                long[] jArr = (long[]) o5;
                while (i8 < jArr.length) {
                    sb.append(jArr[i8]);
                    i8++;
                    if (i8 != jArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (o5 instanceof int[]) {
                int[] iArr = (int[]) o5;
                while (i8 < iArr.length) {
                    sb.append(iArr[i8]);
                    i8++;
                    if (i8 != iArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (o5 instanceof double[]) {
                double[] dArr = (double[]) o5;
                while (i8 < dArr.length) {
                    sb.append(dArr[i8]);
                    i8++;
                    if (i8 != dArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (o5 instanceof f[]) {
                f[] fVarArr = (f[]) o5;
                while (i8 < fVarArr.length) {
                    sb.append(fVarArr[i8].f5372a);
                    sb.append('/');
                    sb.append(fVarArr[i8].f5373b);
                    i8++;
                    if (i8 != fVarArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else {
                return null;
            }
        }

        /* JADX WARN: Not initialized variable reg: 3, insn: 0x019c: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:152:0x019c */
        /* JADX WARN: Removed duplicated region for block: B:176:0x019f A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        java.lang.Object o(java.nio.ByteOrder r11) {
            /*
                Method dump skipped, instructions count: 452
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.d.o(java.nio.ByteOrder):java.lang.Object");
        }

        public int p() {
            return a.X[this.f5364a] * this.f5365b;
        }

        public String toString() {
            return "(" + a.W[this.f5364a] + ", data length:" + this.f5367d.length + ")";
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class e {

        /* renamed from: a  reason: collision with root package name */
        public final int f5368a;

        /* renamed from: b  reason: collision with root package name */
        public final String f5369b;

        /* renamed from: c  reason: collision with root package name */
        public final int f5370c;

        /* renamed from: d  reason: collision with root package name */
        public final int f5371d;

        e(String str, int i8, int i9) {
            this.f5369b = str;
            this.f5368a = i8;
            this.f5370c = i9;
            this.f5371d = -1;
        }

        e(String str, int i8, int i9, int i10) {
            this.f5369b = str;
            this.f5368a = i8;
            this.f5370c = i9;
            this.f5371d = i10;
        }

        boolean a(int i8) {
            int i9;
            int i10 = this.f5370c;
            if (i10 == 7 || i8 == 7 || i10 == i8 || (i9 = this.f5371d) == i8) {
                return true;
            }
            if ((i10 == 4 || i9 == 4) && i8 == 3) {
                return true;
            }
            if ((i10 == 9 || i9 == 9) && i8 == 8) {
                return true;
            }
            return (i10 == 12 || i9 == 12) && i8 == 11;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class f {

        /* renamed from: a  reason: collision with root package name */
        public final long f5372a;

        /* renamed from: b  reason: collision with root package name */
        public final long f5373b;

        f(double d8) {
            this((long) (d8 * 10000.0d), 10000L);
        }

        f(long j8, long j9) {
            if (j9 == 0) {
                this.f5372a = 0L;
                this.f5373b = 1L;
                return;
            }
            this.f5372a = j8;
            this.f5373b = j9;
        }

        public double a() {
            return this.f5372a / this.f5373b;
        }

        public String toString() {
            return this.f5372a + "/" + this.f5373b;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class g extends b {
        g(InputStream inputStream) {
            super(inputStream);
            if (!inputStream.markSupported()) {
                throw new IllegalArgumentException("Cannot create SeekableByteOrderedDataInputStream with stream that does not support mark/reset");
            }
            this.f5357a.mark(Integer.MAX_VALUE);
        }

        g(byte[] bArr) {
            super(bArr);
            this.f5357a.mark(Integer.MAX_VALUE);
        }

        public void h(long j8) {
            int i8 = this.f5358b;
            if (i8 > j8) {
                this.f5358b = 0;
                this.f5357a.reset();
            } else {
                j8 -= i8;
            }
            f((int) j8);
        }
    }

    static {
        e[] eVarArr;
        e[] eVarArr2 = {new e("NewSubfileType", 254, 4), new e("SubfileType", 255, 4), new e("ImageWidth", RecognitionOptions.QR_CODE, 3, 4), new e("ImageLength", 257, 3, 4), new e("BitsPerSample", 258, 3), new e("Compression", 259, 3), new e("PhotometricInterpretation", 262, 3), new e("ImageDescription", 270, 2), new e("Make", 271, 2), new e("Model", 272, 2), new e("StripOffsets", 273, 3, 4), new e("Orientation", 274, 3), new e("SamplesPerPixel", 277, 3), new e("RowsPerStrip", 278, 3, 4), new e("StripByteCounts", 279, 3, 4), new e("XResolution", 282, 5), new e("YResolution", 283, 5), new e("PlanarConfiguration", 284, 3), new e("ResolutionUnit", 296, 3), new e("TransferFunction", 301, 3), new e("Software", 305, 2), new e("DateTime", 306, 2), new e("Artist", 315, 2), new e("WhitePoint", 318, 5), new e("PrimaryChromaticities", 319, 5), new e("SubIFDPointer", 330, 4), new e("JPEGInterchangeFormat", 513, 4), new e("JPEGInterchangeFormatLength", 514, 4), new e("YCbCrCoefficients", 529, 5), new e("YCbCrSubSampling", 530, 3), new e("YCbCrPositioning", 531, 3), new e("ReferenceBlackWhite", 532, 5), new e("Copyright", 33432, 2), new e("ExifIFDPointer", 34665, 4), new e("GPSInfoIFDPointer", 34853, 4), new e("SensorTopBorder", 4, 4), new e("SensorLeftBorder", 5, 4), new e("SensorBottomBorder", 6, 4), new e("SensorRightBorder", 7, 4), new e("ISO", 23, 3), new e("JpgFromRaw", 46, 7), new e("Xmp", 700, 1)};
        Z = eVarArr2;
        e[] eVarArr3 = {new e("ExposureTime", 33434, 5), new e("FNumber", 33437, 5), new e("ExposureProgram", 34850, 3), new e("SpectralSensitivity", 34852, 2), new e("PhotographicSensitivity", 34855, 3), new e("OECF", 34856, 7), new e("SensitivityType", 34864, 3), new e("StandardOutputSensitivity", 34865, 4), new e("RecommendedExposureIndex", 34866, 4), new e("ISOSpeed", 34867, 4), new e("ISOSpeedLatitudeyyy", 34868, 4), new e("ISOSpeedLatitudezzz", 34869, 4), new e("ExifVersion", 36864, 2), new e("DateTimeOriginal", 36867, 2), new e("DateTimeDigitized", 36868, 2), new e("OffsetTime", 36880, 2), new e("OffsetTimeOriginal", 36881, 2), new e("OffsetTimeDigitized", 36882, 2), new e("ComponentsConfiguration", 37121, 7), new e("CompressedBitsPerPixel", 37122, 5), new e("ShutterSpeedValue", 37377, 10), new e("ApertureValue", 37378, 5), new e("BrightnessValue", 37379, 10), new e("ExposureBiasValue", 37380, 10), new e("MaxApertureValue", 37381, 5), new e("SubjectDistance", 37382, 5), new e("MeteringMode", 37383, 3), new e("LightSource", 37384, 3), new e("Flash", 37385, 3), new e("FocalLength", 37386, 5), new e("SubjectArea", 37396, 3), new e("MakerNote", 37500, 7), new e("UserComment", 37510, 7), new e("SubSecTime", 37520, 2), new e("SubSecTimeOriginal", 37521, 2), new e("SubSecTimeDigitized", 37522, 2), new e("FlashpixVersion", 40960, 7), new e("ColorSpace", 40961, 3), new e("PixelXDimension", 40962, 3, 4), new e("PixelYDimension", 40963, 3, 4), new e("RelatedSoundFile", 40964, 2), new e("InteroperabilityIFDPointer", 40965, 4), new e("FlashEnergy", 41483, 5), new e("SpatialFrequencyResponse", 41484, 7), new e("FocalPlaneXResolution", 41486, 5), new e("FocalPlaneYResolution", 41487, 5), new e("FocalPlaneResolutionUnit", 41488, 3), new e("SubjectLocation", 41492, 3), new e("ExposureIndex", 41493, 5), new e("SensingMethod", 41495, 3), new e("FileSource", 41728, 7), new e("SceneType", 41729, 7), new e("CFAPattern", 41730, 7), new e("CustomRendered", 41985, 3), new e("ExposureMode", 41986, 3), new e("WhiteBalance", 41987, 3), new e("DigitalZoomRatio", 41988, 5), new e("FocalLengthIn35mmFilm", 41989, 3), new e("SceneCaptureType", 41990, 3), new e("GainControl", 41991, 3), new e("Contrast", 41992, 3), new e("Saturation", 41993, 3), new e("Sharpness", 41994, 3), new e("DeviceSettingDescription", 41995, 7), new e("SubjectDistanceRange", 41996, 3), new e("ImageUniqueID", 42016, 2), new e("CameraOwnerName", 42032, 2), new e("BodySerialNumber", 42033, 2), new e("LensSpecification", 42034, 5), new e("LensMake", 42035, 2), new e("LensModel", 42036, 2), new e("Gamma", 42240, 5), new e("DNGVersion", 50706, 1), new e("DefaultCropSize", 50720, 3, 4)};
        f5307a0 = eVarArr3;
        e[] eVarArr4 = {new e("GPSVersionID", 0, 1), new e("GPSLatitudeRef", 1, 2), new e("GPSLatitude", 2, 5, 10), new e("GPSLongitudeRef", 3, 2), new e("GPSLongitude", 4, 5, 10), new e("GPSAltitudeRef", 5, 1), new e("GPSAltitude", 6, 5), new e("GPSTimeStamp", 7, 5), new e("GPSSatellites", 8, 2), new e("GPSStatus", 9, 2), new e("GPSMeasureMode", 10, 2), new e("GPSDOP", 11, 5), new e("GPSSpeedRef", 12, 2), new e("GPSSpeed", 13, 5), new e("GPSTrackRef", 14, 2), new e("GPSTrack", 15, 5), new e("GPSImgDirectionRef", 16, 2), new e("GPSImgDirection", 17, 5), new e("GPSMapDatum", 18, 2), new e("GPSDestLatitudeRef", 19, 2), new e("GPSDestLatitude", 20, 5), new e("GPSDestLongitudeRef", 21, 2), new e("GPSDestLongitude", 22, 5), new e("GPSDestBearingRef", 23, 2), new e("GPSDestBearing", 24, 5), new e("GPSDestDistanceRef", 25, 2), new e("GPSDestDistance", 26, 5), new e("GPSProcessingMethod", 27, 7), new e("GPSAreaInformation", 28, 7), new e("GPSDateStamp", 29, 2), new e("GPSDifferential", 30, 3), new e("GPSHPositioningError", 31, 5)};
        f5308b0 = eVarArr4;
        e[] eVarArr5 = {new e("InteroperabilityIndex", 1, 2)};
        f5309c0 = eVarArr5;
        e[] eVarArr6 = {new e("NewSubfileType", 254, 4), new e("SubfileType", 255, 4), new e("ThumbnailImageWidth", RecognitionOptions.QR_CODE, 3, 4), new e("ThumbnailImageLength", 257, 3, 4), new e("BitsPerSample", 258, 3), new e("Compression", 259, 3), new e("PhotometricInterpretation", 262, 3), new e("ImageDescription", 270, 2), new e("Make", 271, 2), new e("Model", 272, 2), new e("StripOffsets", 273, 3, 4), new e("ThumbnailOrientation", 274, 3), new e("SamplesPerPixel", 277, 3), new e("RowsPerStrip", 278, 3, 4), new e("StripByteCounts", 279, 3, 4), new e("XResolution", 282, 5), new e("YResolution", 283, 5), new e("PlanarConfiguration", 284, 3), new e("ResolutionUnit", 296, 3), new e("TransferFunction", 301, 3), new e("Software", 305, 2), new e("DateTime", 306, 2), new e("Artist", 315, 2), new e("WhitePoint", 318, 5), new e("PrimaryChromaticities", 319, 5), new e("SubIFDPointer", 330, 4), new e("JPEGInterchangeFormat", 513, 4), new e("JPEGInterchangeFormatLength", 514, 4), new e("YCbCrCoefficients", 529, 5), new e("YCbCrSubSampling", 530, 3), new e("YCbCrPositioning", 531, 3), new e("ReferenceBlackWhite", 532, 5), new e("Copyright", 33432, 2), new e("ExifIFDPointer", 34665, 4), new e("GPSInfoIFDPointer", 34853, 4), new e("DNGVersion", 50706, 1), new e("DefaultCropSize", 50720, 3, 4)};
        f5310d0 = eVarArr6;
        f5311e0 = new e("StripOffsets", 273, 3);
        e[] eVarArr7 = {new e("ThumbnailImage", RecognitionOptions.QR_CODE, 7), new e("CameraSettingsIFDPointer", 8224, 4), new e("ImageProcessingIFDPointer", 8256, 4)};
        f5312f0 = eVarArr7;
        e[] eVarArr8 = {new e("PreviewImageStart", 257, 4), new e("PreviewImageLength", 258, 4)};
        f5313g0 = eVarArr8;
        e[] eVarArr9 = {new e("AspectFrame", 4371, 3)};
        f5314h0 = eVarArr9;
        e[] eVarArr10 = {new e("ColorSpace", 55, 3)};
        f5315i0 = eVarArr10;
        e[][] eVarArr11 = {eVarArr2, eVarArr3, eVarArr4, eVarArr5, eVarArr6, eVarArr2, eVarArr7, eVarArr8, eVarArr9, eVarArr10};
        f5316j0 = eVarArr11;
        f5317k0 = new e[]{new e("SubIFDPointer", 330, 4), new e("ExifIFDPointer", 34665, 4), new e("GPSInfoIFDPointer", 34853, 4), new e("InteroperabilityIFDPointer", 40965, 4), new e("CameraSettingsIFDPointer", 8224, 1), new e("ImageProcessingIFDPointer", 8256, 1)};
        f5318l0 = new HashMap[eVarArr11.length];
        f5319m0 = new HashMap[eVarArr11.length];
        f5320n0 = new HashSet<>(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
        f5321o0 = new HashMap<>();
        Charset forName = Charset.forName("US-ASCII");
        f5322p0 = forName;
        f5323q0 = "Exif\u0000\u0000".getBytes(forName);
        f5324r0 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(forName);
        Locale locale = Locale.US;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", locale);
        U = simpleDateFormat;
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
        V = simpleDateFormat2;
        simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("UTC"));
        int i8 = 0;
        while (true) {
            e[][] eVarArr12 = f5316j0;
            if (i8 >= eVarArr12.length) {
                HashMap<Integer, Integer> hashMap = f5321o0;
                e[] eVarArr13 = f5317k0;
                hashMap.put(Integer.valueOf(eVarArr13[0].f5368a), 5);
                hashMap.put(Integer.valueOf(eVarArr13[1].f5368a), 1);
                hashMap.put(Integer.valueOf(eVarArr13[2].f5368a), 2);
                hashMap.put(Integer.valueOf(eVarArr13[3].f5368a), 3);
                hashMap.put(Integer.valueOf(eVarArr13[4].f5368a), 7);
                hashMap.put(Integer.valueOf(eVarArr13[5].f5368a), 8);
                f5325s0 = Pattern.compile(".*[1-9].*");
                f5326t0 = Pattern.compile("^(\\d{2}):(\\d{2}):(\\d{2})$");
                f5327u0 = Pattern.compile("^(\\d{4}):(\\d{2}):(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
                f5329v0 = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
                return;
            }
            f5318l0[i8] = new HashMap<>();
            f5319m0[i8] = new HashMap<>();
            for (e eVar : eVarArr12[i8]) {
                f5318l0[i8].put(Integer.valueOf(eVar.f5368a), eVar);
                f5319m0[i8].put(eVar.f5369b, eVar);
            }
            i8++;
        }
    }

    public a(InputStream inputStream) {
        this(inputStream, 0);
    }

    public a(InputStream inputStream, int i8) {
        e[][] eVarArr = f5316j0;
        this.f5339f = new HashMap[eVarArr.length];
        this.f5340g = new HashSet(eVarArr.length);
        this.f5341h = ByteOrder.BIG_ENDIAN;
        Objects.requireNonNull(inputStream, "inputStream cannot be null");
        this.f5334a = null;
        if (i8 == 1) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, f5323q0.length);
            if (!B(bufferedInputStream)) {
                Log.w("ExifInterface", "Given data does not follow the structure of an Exif-only data.");
                return;
            }
            this.f5338e = true;
            this.f5336c = null;
            this.f5335b = null;
            inputStream = bufferedInputStream;
        } else {
            if (inputStream instanceof AssetManager.AssetInputStream) {
                this.f5336c = (AssetManager.AssetInputStream) inputStream;
            } else {
                if (inputStream instanceof FileInputStream) {
                    FileInputStream fileInputStream = (FileInputStream) inputStream;
                    if (I(fileInputStream.getFD())) {
                        this.f5336c = null;
                        this.f5335b = fileInputStream.getFD();
                    }
                }
                this.f5336c = null;
            }
            this.f5335b = null;
        }
        N(inputStream);
    }

    public a(String str) {
        e[][] eVarArr = f5316j0;
        this.f5339f = new HashMap[eVarArr.length];
        this.f5340g = new HashSet(eVarArr.length);
        this.f5341h = ByteOrder.BIG_ENDIAN;
        Objects.requireNonNull(str, "filename cannot be null");
        A(str);
    }

    private void A(String str) {
        Objects.requireNonNull(str, "filename cannot be null");
        FileInputStream fileInputStream = null;
        this.f5336c = null;
        this.f5334a = str;
        try {
            FileInputStream fileInputStream2 = new FileInputStream(str);
            try {
                if (I(fileInputStream2.getFD())) {
                    this.f5335b = fileInputStream2.getFD();
                } else {
                    this.f5335b = null;
                }
                N(fileInputStream2);
                androidx.exifinterface.media.b.b(fileInputStream2);
            } catch (Throwable th) {
                th = th;
                fileInputStream = fileInputStream2;
                androidx.exifinterface.media.b.b(fileInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean B(BufferedInputStream bufferedInputStream) {
        byte[] bArr = f5323q0;
        bufferedInputStream.mark(bArr.length);
        byte[] bArr2 = new byte[bArr.length];
        bufferedInputStream.read(bArr2);
        bufferedInputStream.reset();
        int i8 = 0;
        while (true) {
            byte[] bArr3 = f5323q0;
            if (i8 >= bArr3.length) {
                return true;
            }
            if (bArr2[i8] != bArr3[i8]) {
                return false;
            }
            i8++;
        }
    }

    private boolean C(byte[] bArr) {
        b bVar;
        long readInt;
        byte[] bArr2;
        b bVar2 = null;
        try {
            try {
                bVar = new b(bArr);
            } catch (Exception e8) {
                e = e8;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            readInt = bVar.readInt();
            bArr2 = new byte[4];
            bVar.readFully(bArr2);
        } catch (Exception e9) {
            e = e9;
            bVar2 = bVar;
            if (f5328v) {
                Log.d("ExifInterface", "Exception parsing HEIF file type box.", e);
            }
            if (bVar2 != null) {
                bVar2.close();
            }
            return false;
        } catch (Throwable th2) {
            th = th2;
            bVar2 = bVar;
            if (bVar2 != null) {
                bVar2.close();
            }
            throw th;
        }
        if (!Arrays.equals(bArr2, C)) {
            bVar.close();
            return false;
        }
        long j8 = 16;
        if (readInt == 1) {
            readInt = bVar.readLong();
            if (readInt < 16) {
                bVar.close();
                return false;
            }
        } else {
            j8 = 8;
        }
        if (readInt > bArr.length) {
            readInt = bArr.length;
        }
        long j9 = readInt - j8;
        if (j9 < 8) {
            bVar.close();
            return false;
        }
        byte[] bArr3 = new byte[4];
        boolean z4 = false;
        boolean z8 = false;
        for (long j10 = 0; j10 < j9 / 4; j10++) {
            try {
                bVar.readFully(bArr3);
                if (j10 != 1) {
                    if (Arrays.equals(bArr3, D)) {
                        z4 = true;
                    } else if (Arrays.equals(bArr3, E)) {
                        z8 = true;
                    }
                    if (z4 && z8) {
                        bVar.close();
                        return true;
                    }
                }
            } catch (EOFException unused) {
                bVar.close();
                return false;
            }
        }
        bVar.close();
        return false;
    }

    private static boolean D(byte[] bArr) {
        int i8 = 0;
        while (true) {
            byte[] bArr2 = B;
            if (i8 >= bArr2.length) {
                return true;
            }
            if (bArr[i8] != bArr2[i8]) {
                return false;
            }
            i8++;
        }
    }

    private boolean E(byte[] bArr) {
        boolean z4 = false;
        b bVar = null;
        try {
            b bVar2 = new b(bArr);
            try {
                ByteOrder Q2 = Q(bVar2);
                this.f5341h = Q2;
                bVar2.d(Q2);
                short readShort = bVar2.readShort();
                z4 = (readShort == 20306 || readShort == 21330) ? true : true;
                bVar2.close();
                return z4;
            } catch (Exception unused) {
                bVar = bVar2;
                if (bVar != null) {
                    bVar.close();
                }
                return false;
            } catch (Throwable th) {
                th = th;
                bVar = bVar2;
                if (bVar != null) {
                    bVar.close();
                }
                throw th;
            }
        } catch (Exception unused2) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private boolean F(byte[] bArr) {
        int i8 = 0;
        while (true) {
            byte[] bArr2 = H;
            if (i8 >= bArr2.length) {
                return true;
            }
            if (bArr[i8] != bArr2[i8]) {
                return false;
            }
            i8++;
        }
    }

    private boolean G(byte[] bArr) {
        byte[] bytes = "FUJIFILMCCD-RAW".getBytes(Charset.defaultCharset());
        for (int i8 = 0; i8 < bytes.length; i8++) {
            if (bArr[i8] != bytes[i8]) {
                return false;
            }
        }
        return true;
    }

    private boolean H(byte[] bArr) {
        b bVar = null;
        try {
            b bVar2 = new b(bArr);
            try {
                ByteOrder Q2 = Q(bVar2);
                this.f5341h = Q2;
                bVar2.d(Q2);
                boolean z4 = bVar2.readShort() == 85;
                bVar2.close();
                return z4;
            } catch (Exception unused) {
                bVar = bVar2;
                if (bVar != null) {
                    bVar.close();
                }
                return false;
            } catch (Throwable th) {
                th = th;
                bVar = bVar2;
                if (bVar != null) {
                    bVar.close();
                }
                throw th;
            }
        } catch (Exception unused2) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean I(FileDescriptor fileDescriptor) {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                b.a.c(fileDescriptor, 0L, OsConstants.SEEK_CUR);
                return true;
            } catch (Exception unused) {
                if (f5328v) {
                    Log.d("ExifInterface", "The file descriptor for the given input is not seekable");
                }
            }
        }
        return false;
    }

    private boolean J(HashMap hashMap) {
        d dVar;
        int m8;
        d dVar2 = (d) hashMap.get("BitsPerSample");
        if (dVar2 != null) {
            int[] iArr = (int[]) dVar2.o(this.f5341h);
            int[] iArr2 = f5332y;
            if (Arrays.equals(iArr2, iArr)) {
                return true;
            }
            if (this.f5337d == 3 && (dVar = (d) hashMap.get("PhotometricInterpretation")) != null && (((m8 = dVar.m(this.f5341h)) == 1 && Arrays.equals(iArr, A)) || (m8 == 6 && Arrays.equals(iArr, iArr2)))) {
                return true;
            }
        }
        if (f5328v) {
            Log.d("ExifInterface", "Unsupported data type value");
            return false;
        }
        return false;
    }

    private static boolean K(int i8) {
        return i8 == 4 || i8 == 13 || i8 == 14;
    }

    private boolean L(HashMap hashMap) {
        d dVar = (d) hashMap.get("ImageLength");
        d dVar2 = (d) hashMap.get("ImageWidth");
        if (dVar == null || dVar2 == null) {
            return false;
        }
        return dVar.m(this.f5341h) <= 512 && dVar2.m(this.f5341h) <= 512;
    }

    private boolean M(byte[] bArr) {
        int i8 = 0;
        while (true) {
            byte[] bArr2 = L;
            if (i8 >= bArr2.length) {
                int i9 = 0;
                while (true) {
                    byte[] bArr3 = M;
                    if (i9 >= bArr3.length) {
                        return true;
                    }
                    if (bArr[L.length + i9 + 4] != bArr3[i9]) {
                        return false;
                    }
                    i9++;
                }
            } else if (bArr[i8] != bArr2[i8]) {
                return false;
            } else {
                i8++;
            }
        }
    }

    private void N(InputStream inputStream) {
        Objects.requireNonNull(inputStream, "inputstream shouldn't be null");
        for (int i8 = 0; i8 < f5316j0.length; i8++) {
            try {
                try {
                    this.f5339f[i8] = new HashMap<>();
                } finally {
                    a();
                    if (f5328v) {
                        P();
                    }
                }
            } catch (IOException | UnsupportedOperationException e8) {
                boolean z4 = f5328v;
                if (z4) {
                    Log.w("ExifInterface", "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface.", e8);
                }
                a();
                if (!z4) {
                    return;
                }
            }
        }
        if (!this.f5338e) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream, 5000);
            this.f5337d = m(bufferedInputStream);
            inputStream = bufferedInputStream;
        }
        if (c0(this.f5337d)) {
            g gVar = new g(inputStream);
            if (this.f5338e) {
                t(gVar);
            } else {
                int i9 = this.f5337d;
                if (i9 == 12) {
                    j(gVar);
                } else if (i9 == 7) {
                    n(gVar);
                } else if (i9 == 10) {
                    s(gVar);
                } else {
                    q(gVar);
                }
            }
            gVar.h(this.f5349p);
            b0(gVar);
        } else {
            b bVar = new b(inputStream);
            int i10 = this.f5337d;
            if (i10 == 4) {
                k(bVar, 0, 0);
            } else if (i10 == 13) {
                o(bVar);
            } else if (i10 == 9) {
                p(bVar);
            } else if (i10 == 14) {
                w(bVar);
            }
        }
    }

    private void O(b bVar) {
        ByteOrder Q2 = Q(bVar);
        this.f5341h = Q2;
        bVar.d(Q2);
        int readUnsignedShort = bVar.readUnsignedShort();
        int i8 = this.f5337d;
        if (i8 != 7 && i8 != 10 && readUnsignedShort != 42) {
            throw new IOException("Invalid start code: " + Integer.toHexString(readUnsignedShort));
        }
        int readInt = bVar.readInt();
        if (readInt < 8) {
            throw new IOException("Invalid first Ifd offset: " + readInt);
        }
        int i9 = readInt - 8;
        if (i9 > 0) {
            bVar.f(i9);
        }
    }

    private void P() {
        for (int i8 = 0; i8 < this.f5339f.length; i8++) {
            Log.d("ExifInterface", "The size of tag group[" + i8 + "]: " + this.f5339f[i8].size());
            for (Map.Entry<String, d> entry : this.f5339f[i8].entrySet()) {
                d value = entry.getValue();
                Log.d("ExifInterface", "tagName: " + entry.getKey() + ", tagType: " + value.toString() + ", tagValue: '" + value.n(this.f5341h) + "'");
            }
        }
    }

    private ByteOrder Q(b bVar) {
        short readShort = bVar.readShort();
        if (readShort == 18761) {
            if (f5328v) {
                Log.d("ExifInterface", "readExifSegment: Byte Align II");
            }
            return ByteOrder.LITTLE_ENDIAN;
        } else if (readShort == 19789) {
            if (f5328v) {
                Log.d("ExifInterface", "readExifSegment: Byte Align MM");
            }
            return ByteOrder.BIG_ENDIAN;
        } else {
            throw new IOException("Invalid byte order: " + Integer.toHexString(readShort));
        }
    }

    private void R(byte[] bArr, int i8) {
        g gVar = new g(bArr);
        O(gVar);
        S(gVar, i8);
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x028c  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x012d  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x0135  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0223  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void S(androidx.exifinterface.media.a.g r30, int r31) {
        /*
            Method dump skipped, instructions count: 953
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.S(androidx.exifinterface.media.a$g, int):void");
    }

    private void T(String str) {
        for (int i8 = 0; i8 < f5316j0.length; i8++) {
            this.f5339f[i8].remove(str);
        }
    }

    private void U(int i8, String str, String str2) {
        if (this.f5339f[i8].isEmpty() || this.f5339f[i8].get(str) == null) {
            return;
        }
        HashMap<String, d>[] hashMapArr = this.f5339f;
        hashMapArr[i8].put(str2, hashMapArr[i8].get(str));
        this.f5339f[i8].remove(str);
    }

    private void V(g gVar, int i8) {
        d dVar = this.f5339f[i8].get("ImageLength");
        d dVar2 = this.f5339f[i8].get("ImageWidth");
        if (dVar == null || dVar2 == null) {
            d dVar3 = this.f5339f[i8].get("JPEGInterchangeFormat");
            d dVar4 = this.f5339f[i8].get("JPEGInterchangeFormatLength");
            if (dVar3 == null || dVar4 == null) {
                return;
            }
            int m8 = dVar3.m(this.f5341h);
            int m9 = dVar3.m(this.f5341h);
            gVar.h(m8);
            byte[] bArr = new byte[m9];
            gVar.readFully(bArr);
            k(new b(bArr), m8, i8);
        }
    }

    private void X(InputStream inputStream, OutputStream outputStream) {
        if (f5328v) {
            Log.d("ExifInterface", "saveJpegAttributes starting with (inputStream: " + inputStream + ", outputStream: " + outputStream + ")");
        }
        b bVar = new b(inputStream);
        c cVar = new c(outputStream, ByteOrder.BIG_ENDIAN);
        if (bVar.readByte() != -1) {
            throw new IOException("Invalid marker");
        }
        cVar.b(-1);
        if (bVar.readByte() != -40) {
            throw new IOException("Invalid marker");
        }
        cVar.b(-40);
        d dVar = null;
        if (f("Xmp") != null && this.f5353u) {
            dVar = this.f5339f[0].remove("Xmp");
        }
        cVar.b(-1);
        cVar.b(-31);
        g0(cVar);
        if (dVar != null) {
            this.f5339f[0].put("Xmp", dVar);
        }
        byte[] bArr = new byte[RecognitionOptions.AZTEC];
        while (bVar.readByte() == -1) {
            byte readByte = bVar.readByte();
            if (readByte == -39 || readByte == -38) {
                cVar.b(-1);
                cVar.b(readByte);
                androidx.exifinterface.media.b.d(bVar, cVar);
                return;
            } else if (readByte != -31) {
                cVar.b(-1);
                cVar.b(readByte);
                int readUnsignedShort = bVar.readUnsignedShort();
                cVar.h(readUnsignedShort);
                int i8 = readUnsignedShort - 2;
                if (i8 < 0) {
                    throw new IOException("Invalid length");
                }
                while (i8 > 0) {
                    int read = bVar.read(bArr, 0, Math.min(i8, (int) RecognitionOptions.AZTEC));
                    if (read >= 0) {
                        cVar.write(bArr, 0, read);
                        i8 -= read;
                    }
                }
            } else {
                int readUnsignedShort2 = bVar.readUnsignedShort() - 2;
                if (readUnsignedShort2 < 0) {
                    throw new IOException("Invalid length");
                }
                byte[] bArr2 = new byte[6];
                if (readUnsignedShort2 >= 6) {
                    bVar.readFully(bArr2);
                    if (Arrays.equals(bArr2, f5323q0)) {
                        bVar.f(readUnsignedShort2 - 6);
                    }
                }
                cVar.b(-1);
                cVar.b(readByte);
                cVar.h(readUnsignedShort2 + 2);
                if (readUnsignedShort2 >= 6) {
                    readUnsignedShort2 -= 6;
                    cVar.write(bArr2);
                }
                while (readUnsignedShort2 > 0) {
                    int read2 = bVar.read(bArr, 0, Math.min(readUnsignedShort2, (int) RecognitionOptions.AZTEC));
                    if (read2 >= 0) {
                        cVar.write(bArr, 0, read2);
                        readUnsignedShort2 -= read2;
                    }
                }
            }
        }
        throw new IOException("Invalid marker");
    }

    private void Y(InputStream inputStream, OutputStream outputStream) {
        ByteArrayOutputStream byteArrayOutputStream;
        if (f5328v) {
            Log.d("ExifInterface", "savePngAttributes starting with (inputStream: " + inputStream + ", outputStream: " + outputStream + ")");
        }
        b bVar = new b(inputStream);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        c cVar = new c(outputStream, byteOrder);
        byte[] bArr = H;
        androidx.exifinterface.media.b.e(bVar, cVar, bArr.length);
        int i8 = this.f5349p;
        if (i8 == 0) {
            int readInt = bVar.readInt();
            cVar.c(readInt);
            androidx.exifinterface.media.b.e(bVar, cVar, readInt + 4 + 4);
        } else {
            androidx.exifinterface.media.b.e(bVar, cVar, ((i8 - bArr.length) - 4) - 4);
            bVar.f(bVar.readInt() + 4 + 4);
        }
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Throwable th) {
            th = th;
        }
        try {
            c cVar2 = new c(byteArrayOutputStream, byteOrder);
            g0(cVar2);
            byte[] byteArray = ((ByteArrayOutputStream) cVar2.f5362a).toByteArray();
            cVar.write(byteArray);
            CRC32 crc32 = new CRC32();
            crc32.update(byteArray, 4, byteArray.length - 4);
            cVar.c((int) crc32.getValue());
            androidx.exifinterface.media.b.b(byteArrayOutputStream);
            androidx.exifinterface.media.b.d(bVar, cVar);
        } catch (Throwable th2) {
            th = th2;
            byteArrayOutputStream2 = byteArrayOutputStream;
            androidx.exifinterface.media.b.b(byteArrayOutputStream2);
            throw th;
        }
    }

    private void Z(InputStream inputStream, OutputStream outputStream) {
        ByteArrayOutputStream byteArrayOutputStream;
        int i8;
        int i9;
        int i10;
        boolean z4;
        if (f5328v) {
            Log.d("ExifInterface", "saveWebpAttributes starting with (inputStream: " + inputStream + ", outputStream: " + outputStream + ")");
        }
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        b bVar = new b(inputStream, byteOrder);
        c cVar = new c(outputStream, byteOrder);
        byte[] bArr = L;
        androidx.exifinterface.media.b.e(bVar, cVar, bArr.length);
        byte[] bArr2 = M;
        bVar.f(bArr2.length + 4);
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th) {
                th = th;
            }
        } catch (Exception e8) {
            e = e8;
        }
        try {
            c cVar2 = new c(byteArrayOutputStream, byteOrder);
            int i11 = this.f5349p;
            if (i11 != 0) {
                androidx.exifinterface.media.b.e(bVar, cVar2, ((i11 - ((bArr.length + 4) + bArr2.length)) - 4) - 4);
                bVar.f(4);
                int readInt = bVar.readInt();
                if (readInt % 2 != 0) {
                    readInt++;
                }
                bVar.f(readInt);
            } else {
                byte[] bArr3 = new byte[4];
                bVar.readFully(bArr3);
                byte[] bArr4 = P;
                boolean z8 = true;
                if (!Arrays.equals(bArr3, bArr4)) {
                    byte[] bArr5 = R;
                    if (Arrays.equals(bArr3, bArr5) || Arrays.equals(bArr3, Q)) {
                        int readInt2 = bVar.readInt();
                        int i12 = readInt2 % 2 == 1 ? readInt2 + 1 : readInt2;
                        byte[] bArr6 = new byte[3];
                        if (Arrays.equals(bArr3, bArr5)) {
                            bVar.readFully(bArr6);
                            byte[] bArr7 = new byte[3];
                            bVar.readFully(bArr7);
                            if (!Arrays.equals(O, bArr7)) {
                                throw new IOException("Error checking VP8 signature");
                            }
                            int readInt3 = bVar.readInt();
                            i10 = (readInt3 << 2) >> 18;
                            i12 -= 10;
                            i8 = (readInt3 << 18) >> 18;
                            i9 = readInt3;
                            z8 = false;
                        } else if (!Arrays.equals(bArr3, Q)) {
                            i8 = 0;
                            z8 = false;
                            i9 = 0;
                            i10 = 0;
                        } else if (bVar.readByte() != 47) {
                            throw new IOException("Error checking VP8L signature");
                        } else {
                            i9 = bVar.readInt();
                            i8 = (i9 & 16383) + 1;
                            i10 = ((i9 & 268419072) >>> 14) + 1;
                            if ((i9 & 268435456) == 0) {
                                z8 = false;
                            }
                            i12 -= 5;
                        }
                        cVar2.write(bArr4);
                        cVar2.c(10);
                        byte[] bArr8 = new byte[10];
                        if (z8) {
                            bArr8[0] = (byte) (bArr8[0] | 16);
                        }
                        bArr8[0] = (byte) (bArr8[0] | 8);
                        int i13 = i8 - 1;
                        int i14 = i10 - 1;
                        bArr8[4] = (byte) i13;
                        bArr8[5] = (byte) (i13 >> 8);
                        bArr8[6] = (byte) (i13 >> 16);
                        bArr8[7] = (byte) i14;
                        bArr8[8] = (byte) (i14 >> 8);
                        bArr8[9] = (byte) (i14 >> 16);
                        cVar2.write(bArr8);
                        cVar2.write(bArr3);
                        cVar2.c(readInt2);
                        if (Arrays.equals(bArr3, bArr5)) {
                            cVar2.write(bArr6);
                            cVar2.write(O);
                        } else {
                            if (Arrays.equals(bArr3, Q)) {
                                cVar2.write(47);
                            }
                            androidx.exifinterface.media.b.e(bVar, cVar2, i12);
                        }
                        cVar2.c(i9);
                        androidx.exifinterface.media.b.e(bVar, cVar2, i12);
                    }
                    androidx.exifinterface.media.b.d(bVar, cVar2);
                    int size = byteArrayOutputStream.size();
                    byte[] bArr9 = M;
                    cVar.c(size + bArr9.length);
                    cVar.write(bArr9);
                    byteArrayOutputStream.writeTo(cVar);
                    androidx.exifinterface.media.b.b(byteArrayOutputStream);
                }
                int readInt4 = bVar.readInt();
                byte[] bArr10 = new byte[readInt4 % 2 == 1 ? readInt4 + 1 : readInt4];
                bVar.readFully(bArr10);
                bArr10[0] = (byte) (8 | bArr10[0]);
                boolean z9 = ((bArr10[0] >> 1) & 1) == 1;
                cVar2.write(bArr4);
                cVar2.c(readInt4);
                cVar2.write(bArr10);
                if (z9) {
                    c(bVar, cVar2, S, null);
                    while (true) {
                        byte[] bArr11 = new byte[4];
                        try {
                            bVar.readFully(bArr11);
                            z4 = !Arrays.equals(bArr11, T);
                        } catch (EOFException unused) {
                            z4 = true;
                        }
                        if (z4) {
                            break;
                        }
                        d(bVar, cVar2, bArr11);
                    }
                } else {
                    c(bVar, cVar2, R, Q);
                }
            }
            g0(cVar2);
            androidx.exifinterface.media.b.d(bVar, cVar2);
            int size2 = byteArrayOutputStream.size();
            byte[] bArr92 = M;
            cVar.c(size2 + bArr92.length);
            cVar.write(bArr92);
            byteArrayOutputStream.writeTo(cVar);
            androidx.exifinterface.media.b.b(byteArrayOutputStream);
        } catch (Exception e9) {
            e = e9;
            throw new IOException("Failed to save WebP file", e);
        } catch (Throwable th2) {
            th = th2;
            byteArrayOutputStream2 = byteArrayOutputStream;
            androidx.exifinterface.media.b.b(byteArrayOutputStream2);
            throw th;
        }
    }

    private void a() {
        String f5 = f("DateTimeOriginal");
        if (f5 != null && f("DateTime") == null) {
            this.f5339f[0].put("DateTime", d.e(f5));
        }
        if (f("ImageWidth") == null) {
            this.f5339f[0].put("ImageWidth", d.f(0L, this.f5341h));
        }
        if (f("ImageLength") == null) {
            this.f5339f[0].put("ImageLength", d.f(0L, this.f5341h));
        }
        if (f("Orientation") == null) {
            this.f5339f[0].put("Orientation", d.f(0L, this.f5341h));
        }
        if (f("LightSource") == null) {
            this.f5339f[1].put("LightSource", d.f(0L, this.f5341h));
        }
    }

    private static double b(String str, String str2) {
        try {
            String[] split = str.split(",", -1);
            String[] split2 = split[0].split("/", -1);
            String[] split3 = split[1].split("/", -1);
            String[] split4 = split[2].split("/", -1);
            double parseDouble = (Double.parseDouble(split2[0].trim()) / Double.parseDouble(split2[1].trim())) + ((Double.parseDouble(split3[0].trim()) / Double.parseDouble(split3[1].trim())) / 60.0d) + ((Double.parseDouble(split4[0].trim()) / Double.parseDouble(split4[1].trim())) / 3600.0d);
            if (!str2.equals("S") && !str2.equals("W")) {
                if (!str2.equals("N") && !str2.equals("E")) {
                    throw new IllegalArgumentException();
                }
                return parseDouble;
            }
            return -parseDouble;
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException unused) {
            throw new IllegalArgumentException();
        }
    }

    private void b0(b bVar) {
        HashMap<String, d> hashMap = this.f5339f[4];
        d dVar = hashMap.get("Compression");
        if (dVar != null) {
            int m8 = dVar.m(this.f5341h);
            this.f5348o = m8;
            if (m8 != 1) {
                if (m8 != 6) {
                    if (m8 != 7) {
                        return;
                    }
                }
            }
            if (J(hashMap)) {
                z(bVar, hashMap);
                return;
            }
            return;
        }
        this.f5348o = 6;
        y(bVar, hashMap);
    }

    private void c(b bVar, c cVar, byte[] bArr, byte[] bArr2) {
        while (true) {
            byte[] bArr3 = new byte[4];
            bVar.readFully(bArr3);
            d(bVar, cVar, bArr3);
            if (Arrays.equals(bArr3, bArr)) {
                return;
            }
            if (bArr2 != null && Arrays.equals(bArr3, bArr2)) {
                return;
            }
        }
    }

    private static boolean c0(int i8) {
        return (i8 == 4 || i8 == 9 || i8 == 13 || i8 == 14) ? false : true;
    }

    private void d(b bVar, c cVar, byte[] bArr) {
        int readInt = bVar.readInt();
        cVar.write(bArr);
        cVar.c(readInt);
        if (readInt % 2 == 1) {
            readInt++;
        }
        androidx.exifinterface.media.b.e(bVar, cVar, readInt);
    }

    private void d0(int i8, int i9) {
        String str;
        if (this.f5339f[i8].isEmpty() || this.f5339f[i9].isEmpty()) {
            if (f5328v) {
                Log.d("ExifInterface", "Cannot perform swap since only one image data exists");
                return;
            }
            return;
        }
        d dVar = this.f5339f[i8].get("ImageLength");
        d dVar2 = this.f5339f[i8].get("ImageWidth");
        d dVar3 = this.f5339f[i9].get("ImageLength");
        d dVar4 = this.f5339f[i9].get("ImageWidth");
        if (dVar == null || dVar2 == null) {
            if (!f5328v) {
                return;
            }
            str = "First image does not contain valid size information";
        } else if (dVar3 != null && dVar4 != null) {
            int m8 = dVar.m(this.f5341h);
            int m9 = dVar2.m(this.f5341h);
            int m10 = dVar3.m(this.f5341h);
            int m11 = dVar4.m(this.f5341h);
            if (m8 >= m10 || m9 >= m11) {
                return;
            }
            HashMap<String, d>[] hashMapArr = this.f5339f;
            HashMap<String, d> hashMap = hashMapArr[i8];
            hashMapArr[i8] = hashMapArr[i9];
            hashMapArr[i9] = hashMap;
            return;
        } else if (!f5328v) {
            return;
        } else {
            str = "Second image does not contain valid size information";
        }
        Log.d("ExifInterface", str);
    }

    private void e0(g gVar, int i8) {
        StringBuilder sb;
        String arrays;
        d j8;
        d j9;
        d dVar = this.f5339f[i8].get("DefaultCropSize");
        d dVar2 = this.f5339f[i8].get("SensorTopBorder");
        d dVar3 = this.f5339f[i8].get("SensorLeftBorder");
        d dVar4 = this.f5339f[i8].get("SensorBottomBorder");
        d dVar5 = this.f5339f[i8].get("SensorRightBorder");
        if (dVar == null) {
            if (dVar2 == null || dVar3 == null || dVar4 == null || dVar5 == null) {
                V(gVar, i8);
                return;
            }
            int m8 = dVar2.m(this.f5341h);
            int m9 = dVar4.m(this.f5341h);
            int m10 = dVar5.m(this.f5341h);
            int m11 = dVar3.m(this.f5341h);
            if (m9 <= m8 || m10 <= m11) {
                return;
            }
            d j10 = d.j(m9 - m8, this.f5341h);
            d j11 = d.j(m10 - m11, this.f5341h);
            this.f5339f[i8].put("ImageLength", j10);
            this.f5339f[i8].put("ImageWidth", j11);
        } else if (dVar.f5364a == 5) {
            f[] fVarArr = (f[]) dVar.o(this.f5341h);
            if (fVarArr != null && fVarArr.length == 2) {
                j8 = d.h(fVarArr[0], this.f5341h);
                j9 = d.h(fVarArr[1], this.f5341h);
                this.f5339f[i8].put("ImageWidth", j8);
                this.f5339f[i8].put("ImageLength", j9);
                return;
            }
            sb = new StringBuilder();
            sb.append("Invalid crop size values. cropSize=");
            arrays = Arrays.toString(fVarArr);
            sb.append(arrays);
            Log.w("ExifInterface", sb.toString());
        } else {
            int[] iArr = (int[]) dVar.o(this.f5341h);
            if (iArr != null && iArr.length == 2) {
                j8 = d.j(iArr[0], this.f5341h);
                j9 = d.j(iArr[1], this.f5341h);
                this.f5339f[i8].put("ImageWidth", j8);
                this.f5339f[i8].put("ImageLength", j9);
                return;
            }
            sb = new StringBuilder();
            sb.append("Invalid crop size values. cropSize=");
            arrays = Arrays.toString(iArr);
            sb.append(arrays);
            Log.w("ExifInterface", sb.toString());
        }
    }

    private void f0() {
        d0(0, 5);
        d0(0, 4);
        d0(5, 4);
        d dVar = this.f5339f[1].get("PixelXDimension");
        d dVar2 = this.f5339f[1].get("PixelYDimension");
        if (dVar != null && dVar2 != null) {
            this.f5339f[0].put("ImageWidth", dVar);
            this.f5339f[0].put("ImageLength", dVar2);
        }
        if (this.f5339f[4].isEmpty() && L(this.f5339f[5])) {
            HashMap<String, d>[] hashMapArr = this.f5339f;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap<>();
        }
        if (!L(this.f5339f[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
        U(0, "ThumbnailOrientation", "Orientation");
        U(0, "ThumbnailImageLength", "ImageLength");
        U(0, "ThumbnailImageWidth", "ImageWidth");
        U(5, "ThumbnailOrientation", "Orientation");
        U(5, "ThumbnailImageLength", "ImageLength");
        U(5, "ThumbnailImageWidth", "ImageWidth");
        U(4, "Orientation", "ThumbnailOrientation");
        U(4, "ImageLength", "ThumbnailImageLength");
        U(4, "ImageWidth", "ThumbnailImageWidth");
    }

    private int g0(c cVar) {
        e[][] eVarArr = f5316j0;
        int[] iArr = new int[eVarArr.length];
        int[] iArr2 = new int[eVarArr.length];
        for (e eVar : f5317k0) {
            T(eVar.f5369b);
        }
        if (this.f5342i) {
            if (this.f5343j) {
                T("StripOffsets");
                T("StripByteCounts");
            } else {
                T("JPEGInterchangeFormat");
                T("JPEGInterchangeFormatLength");
            }
        }
        for (int i8 = 0; i8 < f5316j0.length; i8++) {
            for (Object obj : this.f5339f[i8].entrySet().toArray()) {
                Map.Entry entry = (Map.Entry) obj;
                if (entry.getValue() == null) {
                    this.f5339f[i8].remove(entry.getKey());
                }
            }
        }
        if (!this.f5339f[1].isEmpty()) {
            this.f5339f[0].put(f5317k0[1].f5369b, d.f(0L, this.f5341h));
        }
        if (!this.f5339f[2].isEmpty()) {
            this.f5339f[0].put(f5317k0[2].f5369b, d.f(0L, this.f5341h));
        }
        if (!this.f5339f[3].isEmpty()) {
            this.f5339f[1].put(f5317k0[3].f5369b, d.f(0L, this.f5341h));
        }
        if (this.f5342i) {
            if (this.f5343j) {
                this.f5339f[4].put("StripOffsets", d.j(0, this.f5341h));
                this.f5339f[4].put("StripByteCounts", d.j(this.f5346m, this.f5341h));
            } else {
                this.f5339f[4].put("JPEGInterchangeFormat", d.f(0L, this.f5341h));
                this.f5339f[4].put("JPEGInterchangeFormatLength", d.f(this.f5346m, this.f5341h));
            }
        }
        for (int i9 = 0; i9 < f5316j0.length; i9++) {
            int i10 = 0;
            for (Map.Entry<String, d> entry2 : this.f5339f[i9].entrySet()) {
                int p8 = entry2.getValue().p();
                if (p8 > 4) {
                    i10 += p8;
                }
            }
            iArr2[i9] = iArr2[i9] + i10;
        }
        int i11 = 8;
        for (int i12 = 0; i12 < f5316j0.length; i12++) {
            if (!this.f5339f[i12].isEmpty()) {
                iArr[i12] = i11;
                i11 += (this.f5339f[i12].size() * 12) + 2 + 4 + iArr2[i12];
            }
        }
        if (this.f5342i) {
            if (this.f5343j) {
                this.f5339f[4].put("StripOffsets", d.j(i11, this.f5341h));
            } else {
                this.f5339f[4].put("JPEGInterchangeFormat", d.f(i11, this.f5341h));
            }
            this.f5345l = i11;
            i11 += this.f5346m;
        }
        if (this.f5337d == 4) {
            i11 += 8;
        }
        if (f5328v) {
            for (int i13 = 0; i13 < f5316j0.length; i13++) {
                Log.d("ExifInterface", String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", Integer.valueOf(i13), Integer.valueOf(iArr[i13]), Integer.valueOf(this.f5339f[i13].size()), Integer.valueOf(iArr2[i13]), Integer.valueOf(i11)));
            }
        }
        if (!this.f5339f[1].isEmpty()) {
            this.f5339f[0].put(f5317k0[1].f5369b, d.f(iArr[1], this.f5341h));
        }
        if (!this.f5339f[2].isEmpty()) {
            this.f5339f[0].put(f5317k0[2].f5369b, d.f(iArr[2], this.f5341h));
        }
        if (!this.f5339f[3].isEmpty()) {
            this.f5339f[1].put(f5317k0[3].f5369b, d.f(iArr[3], this.f5341h));
        }
        int i14 = this.f5337d;
        if (i14 != 4) {
            if (i14 == 13) {
                cVar.c(i11);
                cVar.write(I);
            } else if (i14 == 14) {
                cVar.write(N);
                cVar.c(i11);
            }
        } else if (i11 > 65535) {
            throw new IllegalStateException("Size of exif data (" + i11 + " bytes) exceeds the max size of a JPEG APP1 segment (65536 bytes)");
        } else {
            cVar.h(i11);
            cVar.write(f5323q0);
        }
        cVar.d(this.f5341h == ByteOrder.BIG_ENDIAN ? (short) 19789 : (short) 18761);
        cVar.a(this.f5341h);
        cVar.h(42);
        cVar.f(8L);
        for (int i15 = 0; i15 < f5316j0.length; i15++) {
            if (!this.f5339f[i15].isEmpty()) {
                cVar.h(this.f5339f[i15].size());
                int size = iArr[i15] + 2 + (this.f5339f[i15].size() * 12) + 4;
                for (Map.Entry<String, d> entry3 : this.f5339f[i15].entrySet()) {
                    int i16 = f5319m0[i15].get(entry3.getKey()).f5368a;
                    d value = entry3.getValue();
                    int p9 = value.p();
                    cVar.h(i16);
                    cVar.h(value.f5364a);
                    cVar.c(value.f5365b);
                    if (p9 > 4) {
                        cVar.f(size);
                        size += p9;
                    } else {
                        cVar.write(value.f5367d);
                        if (p9 < 4) {
                            while (p9 < 4) {
                                cVar.b(0);
                                p9++;
                            }
                        }
                    }
                }
                if (i15 != 0 || this.f5339f[4].isEmpty()) {
                    cVar.f(0L);
                } else {
                    cVar.f(iArr[4]);
                }
                for (Map.Entry<String, d> entry4 : this.f5339f[i15].entrySet()) {
                    byte[] bArr = entry4.getValue().f5367d;
                    if (bArr.length > 4) {
                        cVar.write(bArr, 0, bArr.length);
                    }
                }
            }
        }
        if (this.f5342i) {
            cVar.write(v());
        }
        if (this.f5337d == 14 && i11 % 2 == 1) {
            cVar.b(0);
        }
        cVar.a(ByteOrder.BIG_ENDIAN);
        return i11;
    }

    private d i(String str) {
        Objects.requireNonNull(str, "tag shouldn't be null");
        if ("ISOSpeedRatings".equals(str)) {
            if (f5328v) {
                Log.d("ExifInterface", "getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
            }
            str = "PhotographicSensitivity";
        }
        for (int i8 = 0; i8 < f5316j0.length; i8++) {
            d dVar = this.f5339f[i8].get(str);
            if (dVar != null) {
                return dVar;
            }
        }
        return null;
    }

    private void j(g gVar) {
        String str;
        String str2;
        if (Build.VERSION.SDK_INT < 28) {
            throw new UnsupportedOperationException("Reading EXIF from HEIF files is supported from SDK 28 and above");
        }
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            try {
                b.C0053b.a(mediaMetadataRetriever, new C0052a(gVar));
                String extractMetadata = mediaMetadataRetriever.extractMetadata(33);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(34);
                String extractMetadata3 = mediaMetadataRetriever.extractMetadata(26);
                String extractMetadata4 = mediaMetadataRetriever.extractMetadata(17);
                String str3 = null;
                if ("yes".equals(extractMetadata3)) {
                    str3 = mediaMetadataRetriever.extractMetadata(29);
                    str = mediaMetadataRetriever.extractMetadata(30);
                    str2 = mediaMetadataRetriever.extractMetadata(31);
                } else if ("yes".equals(extractMetadata4)) {
                    str3 = mediaMetadataRetriever.extractMetadata(18);
                    str = mediaMetadataRetriever.extractMetadata(19);
                    str2 = mediaMetadataRetriever.extractMetadata(24);
                } else {
                    str = null;
                    str2 = null;
                }
                if (str3 != null) {
                    this.f5339f[0].put("ImageWidth", d.j(Integer.parseInt(str3), this.f5341h));
                }
                if (str != null) {
                    this.f5339f[0].put("ImageLength", d.j(Integer.parseInt(str), this.f5341h));
                }
                if (str2 != null) {
                    int i8 = 1;
                    int parseInt = Integer.parseInt(str2);
                    if (parseInt == 90) {
                        i8 = 6;
                    } else if (parseInt == 180) {
                        i8 = 3;
                    } else if (parseInt == 270) {
                        i8 = 8;
                    }
                    this.f5339f[0].put("Orientation", d.j(i8, this.f5341h));
                }
                if (extractMetadata != null && extractMetadata2 != null) {
                    int parseInt2 = Integer.parseInt(extractMetadata);
                    int parseInt3 = Integer.parseInt(extractMetadata2);
                    if (parseInt3 <= 6) {
                        throw new IOException("Invalid exif length");
                    }
                    gVar.h(parseInt2);
                    byte[] bArr = new byte[6];
                    gVar.readFully(bArr);
                    int i9 = parseInt2 + 6;
                    int i10 = parseInt3 - 6;
                    if (!Arrays.equals(bArr, f5323q0)) {
                        throw new IOException("Invalid identifier");
                    }
                    byte[] bArr2 = new byte[i10];
                    gVar.readFully(bArr2);
                    this.f5349p = i9;
                    R(bArr2, 0);
                }
                if (f5328v) {
                    Log.d("ExifInterface", "Heif meta: " + str3 + "x" + str + ", rotation " + str2);
                }
            } catch (RuntimeException unused) {
                throw new UnsupportedOperationException("Failed to read EXIF from HEIF file. Given stream is either malformed or unsupported.");
            }
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:64:0x018f, code lost:
        r22.d(r21.f5341h);
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x0194, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:34:0x00ba A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:59:0x0179 A[LOOP:0: B:10:0x0038->B:59:0x0179, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:76:0x0183 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void k(androidx.exifinterface.media.a.b r22, int r23, int r24) {
        /*
            Method dump skipped, instructions count: 530
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.k(androidx.exifinterface.media.a$b, int, int):void");
    }

    private int m(BufferedInputStream bufferedInputStream) {
        bufferedInputStream.mark(5000);
        byte[] bArr = new byte[5000];
        bufferedInputStream.read(bArr);
        bufferedInputStream.reset();
        if (D(bArr)) {
            return 4;
        }
        if (G(bArr)) {
            return 9;
        }
        if (C(bArr)) {
            return 12;
        }
        if (E(bArr)) {
            return 7;
        }
        if (H(bArr)) {
            return 10;
        }
        if (F(bArr)) {
            return 13;
        }
        return M(bArr) ? 14 : 0;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x008c  */
    /* JADX WARN: Removed duplicated region for block: B:33:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private void n(androidx.exifinterface.media.a.g r7) {
        /*
            Method dump skipped, instructions count: 246
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.n(androidx.exifinterface.media.a$g):void");
    }

    private void o(b bVar) {
        if (f5328v) {
            Log.d("ExifInterface", "getPngAttributes starting with: " + bVar);
        }
        bVar.d(ByteOrder.BIG_ENDIAN);
        byte[] bArr = H;
        bVar.f(bArr.length);
        int length = bArr.length + 0;
        while (true) {
            try {
                int readInt = bVar.readInt();
                byte[] bArr2 = new byte[4];
                bVar.readFully(bArr2);
                int i8 = length + 4 + 4;
                if (i8 == 16 && !Arrays.equals(bArr2, J)) {
                    throw new IOException("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");
                }
                if (Arrays.equals(bArr2, K)) {
                    return;
                }
                if (Arrays.equals(bArr2, I)) {
                    byte[] bArr3 = new byte[readInt];
                    bVar.readFully(bArr3);
                    int readInt2 = bVar.readInt();
                    CRC32 crc32 = new CRC32();
                    crc32.update(bArr2);
                    crc32.update(bArr3);
                    if (((int) crc32.getValue()) == readInt2) {
                        this.f5349p = i8;
                        R(bArr3, 0);
                        f0();
                        b0(new b(bArr3));
                        return;
                    }
                    throw new IOException("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: " + readInt2 + ", calculated CRC value: " + crc32.getValue());
                }
                int i9 = readInt + 4;
                bVar.f(i9);
                length = i8 + i9;
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt PNG file.");
            }
        }
    }

    private void p(b bVar) {
        boolean z4 = f5328v;
        if (z4) {
            Log.d("ExifInterface", "getRafAttributes starting with: " + bVar);
        }
        bVar.f(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        bVar.readFully(bArr);
        bVar.readFully(bArr2);
        bVar.readFully(bArr3);
        int i8 = ByteBuffer.wrap(bArr).getInt();
        int i9 = ByteBuffer.wrap(bArr2).getInt();
        int i10 = ByteBuffer.wrap(bArr3).getInt();
        byte[] bArr4 = new byte[i9];
        bVar.f(i8 - bVar.b());
        bVar.readFully(bArr4);
        k(new b(bArr4), i8, 5);
        bVar.f(i10 - bVar.b());
        bVar.d(ByteOrder.BIG_ENDIAN);
        int readInt = bVar.readInt();
        if (z4) {
            Log.d("ExifInterface", "numberOfDirectoryEntry: " + readInt);
        }
        for (int i11 = 0; i11 < readInt; i11++) {
            int readUnsignedShort = bVar.readUnsignedShort();
            int readUnsignedShort2 = bVar.readUnsignedShort();
            if (readUnsignedShort == f5311e0.f5368a) {
                short readShort = bVar.readShort();
                short readShort2 = bVar.readShort();
                d j8 = d.j(readShort, this.f5341h);
                d j9 = d.j(readShort2, this.f5341h);
                this.f5339f[0].put("ImageLength", j8);
                this.f5339f[0].put("ImageWidth", j9);
                if (f5328v) {
                    Log.d("ExifInterface", "Updated to length: " + ((int) readShort) + ", width: " + ((int) readShort2));
                    return;
                }
                return;
            }
            bVar.f(readUnsignedShort2);
        }
    }

    private void q(g gVar) {
        d dVar;
        O(gVar);
        S(gVar, 0);
        e0(gVar, 0);
        e0(gVar, 5);
        e0(gVar, 4);
        f0();
        if (this.f5337d != 8 || (dVar = this.f5339f[1].get("MakerNote")) == null) {
            return;
        }
        g gVar2 = new g(dVar.f5367d);
        gVar2.d(this.f5341h);
        gVar2.f(6);
        S(gVar2, 9);
        d dVar2 = this.f5339f[9].get("ColorSpace");
        if (dVar2 != null) {
            this.f5339f[1].put("ColorSpace", dVar2);
        }
    }

    private void s(g gVar) {
        if (f5328v) {
            Log.d("ExifInterface", "getRw2Attributes starting with: " + gVar);
        }
        q(gVar);
        d dVar = this.f5339f[0].get("JpgFromRaw");
        if (dVar != null) {
            k(new b(dVar.f5367d), (int) dVar.f5366c, 5);
        }
        d dVar2 = this.f5339f[0].get("ISO");
        d dVar3 = this.f5339f[1].get("PhotographicSensitivity");
        if (dVar2 == null || dVar3 != null) {
            return;
        }
        this.f5339f[1].put("PhotographicSensitivity", dVar2);
    }

    private void t(g gVar) {
        byte[] bArr = f5323q0;
        gVar.f(bArr.length);
        byte[] bArr2 = new byte[gVar.available()];
        gVar.readFully(bArr2);
        this.f5349p = bArr.length;
        R(bArr2, 0);
    }

    private void w(b bVar) {
        if (f5328v) {
            Log.d("ExifInterface", "getWebpAttributes starting with: " + bVar);
        }
        bVar.d(ByteOrder.LITTLE_ENDIAN);
        bVar.f(L.length);
        int readInt = bVar.readInt() + 8;
        byte[] bArr = M;
        bVar.f(bArr.length);
        int length = bArr.length + 8;
        while (true) {
            try {
                byte[] bArr2 = new byte[4];
                bVar.readFully(bArr2);
                int readInt2 = bVar.readInt();
                int i8 = length + 4 + 4;
                if (Arrays.equals(N, bArr2)) {
                    byte[] bArr3 = new byte[readInt2];
                    bVar.readFully(bArr3);
                    this.f5349p = i8;
                    R(bArr3, 0);
                    b0(new b(bArr3));
                    return;
                }
                if (readInt2 % 2 == 1) {
                    readInt2++;
                }
                length = i8 + readInt2;
                if (length == readInt) {
                    return;
                }
                if (length > readInt) {
                    throw new IOException("Encountered WebP file with invalid chunk size");
                }
                bVar.f(readInt2);
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt WebP file.");
            }
        }
    }

    private static Pair<Integer, Integer> x(String str) {
        if (str.contains(",")) {
            String[] split = str.split(",", -1);
            Pair<Integer, Integer> x8 = x(split[0]);
            if (((Integer) x8.first).intValue() == 2) {
                return x8;
            }
            for (int i8 = 1; i8 < split.length; i8++) {
                Pair<Integer, Integer> x9 = x(split[i8]);
                int intValue = (((Integer) x9.first).equals(x8.first) || ((Integer) x9.second).equals(x8.first)) ? ((Integer) x8.first).intValue() : -1;
                int intValue2 = (((Integer) x8.second).intValue() == -1 || !(((Integer) x9.first).equals(x8.second) || ((Integer) x9.second).equals(x8.second))) ? -1 : ((Integer) x8.second).intValue();
                if (intValue == -1 && intValue2 == -1) {
                    return new Pair<>(2, -1);
                }
                if (intValue == -1) {
                    x8 = new Pair<>(Integer.valueOf(intValue2), -1);
                } else if (intValue2 == -1) {
                    x8 = new Pair<>(Integer.valueOf(intValue), -1);
                }
            }
            return x8;
        } else if (!str.contains("/")) {
            try {
                try {
                    Long valueOf = Long.valueOf(Long.parseLong(str));
                    return (valueOf.longValue() < 0 || valueOf.longValue() > 65535) ? valueOf.longValue() < 0 ? new Pair<>(9, -1) : new Pair<>(4, -1) : new Pair<>(3, 4);
                } catch (NumberFormatException unused) {
                    return new Pair<>(2, -1);
                }
            } catch (NumberFormatException unused2) {
                Double.parseDouble(str);
                return new Pair<>(12, -1);
            }
        } else {
            String[] split2 = str.split("/", -1);
            if (split2.length == 2) {
                try {
                    long parseDouble = (long) Double.parseDouble(split2[0]);
                    long parseDouble2 = (long) Double.parseDouble(split2[1]);
                    if (parseDouble >= 0 && parseDouble2 >= 0) {
                        if (parseDouble <= 2147483647L && parseDouble2 <= 2147483647L) {
                            return new Pair<>(10, 5);
                        }
                        return new Pair<>(5, -1);
                    }
                    return new Pair<>(10, -1);
                } catch (NumberFormatException unused3) {
                }
            }
            return new Pair<>(2, -1);
        }
    }

    private void y(b bVar, HashMap hashMap) {
        d dVar = (d) hashMap.get("JPEGInterchangeFormat");
        d dVar2 = (d) hashMap.get("JPEGInterchangeFormatLength");
        if (dVar == null || dVar2 == null) {
            return;
        }
        int m8 = dVar.m(this.f5341h);
        int m9 = dVar2.m(this.f5341h);
        if (this.f5337d == 7) {
            m8 += this.q;
        }
        if (m8 > 0 && m9 > 0) {
            this.f5342i = true;
            if (this.f5334a == null && this.f5336c == null && this.f5335b == null) {
                byte[] bArr = new byte[m9];
                bVar.f(m8);
                bVar.readFully(bArr);
                this.f5347n = bArr;
            }
            this.f5345l = m8;
            this.f5346m = m9;
        }
        if (f5328v) {
            Log.d("ExifInterface", "Setting thumbnail attributes with offset: " + m8 + ", length: " + m9);
        }
    }

    private void z(b bVar, HashMap hashMap) {
        d dVar = (d) hashMap.get("StripOffsets");
        d dVar2 = (d) hashMap.get("StripByteCounts");
        if (dVar == null || dVar2 == null) {
            return;
        }
        long[] c9 = androidx.exifinterface.media.b.c(dVar.o(this.f5341h));
        long[] c10 = androidx.exifinterface.media.b.c(dVar2.o(this.f5341h));
        if (c9 == null || c9.length == 0) {
            Log.w("ExifInterface", "stripOffsets should not be null or have zero length.");
        } else if (c10 == null || c10.length == 0) {
            Log.w("ExifInterface", "stripByteCounts should not be null or have zero length.");
        } else if (c9.length != c10.length) {
            Log.w("ExifInterface", "stripOffsets and stripByteCounts should have same length.");
        } else {
            long j8 = 0;
            for (long j9 : c10) {
                j8 += j9;
            }
            int i8 = (int) j8;
            byte[] bArr = new byte[i8];
            int i9 = 1;
            this.f5344k = true;
            this.f5343j = true;
            this.f5342i = true;
            int i10 = 0;
            int i11 = 0;
            int i12 = 0;
            while (i10 < c9.length) {
                int i13 = (int) c9[i10];
                int i14 = (int) c10[i10];
                if (i10 < c9.length - i9 && i13 + i14 != c9[i10 + 1]) {
                    this.f5344k = false;
                }
                int i15 = i13 - i11;
                if (i15 < 0) {
                    Log.d("ExifInterface", "Invalid strip offset value");
                    return;
                }
                try {
                    bVar.f(i15);
                    int i16 = i11 + i15;
                    byte[] bArr2 = new byte[i14];
                    try {
                        bVar.readFully(bArr2);
                        i11 = i16 + i14;
                        System.arraycopy(bArr2, 0, bArr, i12, i14);
                        i12 += i14;
                        i10++;
                        i9 = 1;
                    } catch (EOFException unused) {
                        Log.d("ExifInterface", "Failed to read " + i14 + " bytes.");
                        return;
                    }
                } catch (EOFException unused2) {
                    Log.d("ExifInterface", "Failed to skip " + i15 + " bytes.");
                    return;
                }
            }
            this.f5347n = bArr;
            if (this.f5344k) {
                this.f5345l = (int) c9[0];
                this.f5346m = i8;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:72:0x00f2 A[Catch: all -> 0x011e, Exception -> 0x0120, TryCatch #17 {Exception -> 0x0120, all -> 0x011e, blocks: (B:70:0x00ee, B:72:0x00f2, B:74:0x00f6, B:77:0x010d, B:75:0x0105), top: B:138:0x00ee }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0105 A[Catch: all -> 0x011e, Exception -> 0x0120, TryCatch #17 {Exception -> 0x0120, all -> 0x011e, blocks: (B:70:0x00ee, B:72:0x00f2, B:74:0x00f6, B:77:0x010d, B:75:0x0105), top: B:138:0x00ee }] */
    /* JADX WARN: Removed duplicated region for block: B:99:0x0157  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void W() {
        /*
            Method dump skipped, instructions count: 388
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.W():void");
    }

    public void a0(String str, String str2) {
        StringBuilder sb;
        e eVar;
        int i8;
        int i9;
        HashMap<String, d> hashMap;
        d a9;
        HashMap<String, d> hashMap2;
        d c9;
        Matcher matcher;
        String str3 = str;
        String str4 = str2;
        Objects.requireNonNull(str3, "tag shouldn't be null");
        if (("DateTime".equals(str3) || "DateTimeOriginal".equals(str3) || "DateTimeDigitized".equals(str3)) && str4 != null) {
            boolean find = f5327u0.matcher(str4).find();
            boolean find2 = f5329v0.matcher(str4).find();
            if (str2.length() != 19 || (!find && !find2)) {
                sb = new StringBuilder();
                sb.append("Invalid value for ");
                sb.append(str3);
                sb.append(" : ");
                sb.append(str4);
                Log.w("ExifInterface", sb.toString());
                return;
            } else if (find2) {
                str4 = str4.replaceAll("-", ":");
            }
        }
        if ("ISOSpeedRatings".equals(str3)) {
            if (f5328v) {
                Log.d("ExifInterface", "setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
            }
            str3 = "PhotographicSensitivity";
        }
        int i10 = 2;
        int i11 = 1;
        if (str4 != null && f5320n0.contains(str3)) {
            if (str3.equals("GPSTimeStamp")) {
                if (!f5326t0.matcher(str4).find()) {
                    sb = new StringBuilder();
                    sb.append("Invalid value for ");
                    sb.append(str3);
                    sb.append(" : ");
                    sb.append(str4);
                    Log.w("ExifInterface", sb.toString());
                    return;
                }
                str4 = Integer.parseInt(matcher.group(1)) + "/1," + Integer.parseInt(matcher.group(2)) + "/1," + Integer.parseInt(matcher.group(3)) + "/1";
            } else {
                try {
                    str4 = new f(Double.parseDouble(str4)).toString();
                } catch (NumberFormatException unused) {
                    sb = new StringBuilder();
                }
            }
        }
        int i12 = 0;
        int i13 = 0;
        while (i13 < f5316j0.length) {
            if ((i13 != 4 || this.f5342i) && (eVar = f5319m0[i13].get(str3)) != null) {
                if (str4 == null) {
                    this.f5339f[i13].remove(str3);
                } else {
                    Pair<Integer, Integer> x8 = x(str4);
                    int i14 = -1;
                    if (eVar.f5370c == ((Integer) x8.first).intValue() || eVar.f5370c == ((Integer) x8.second).intValue()) {
                        i8 = eVar.f5370c;
                    } else {
                        int i15 = eVar.f5371d;
                        if (i15 == -1 || !(i15 == ((Integer) x8.first).intValue() || eVar.f5371d == ((Integer) x8.second).intValue())) {
                            int i16 = eVar.f5370c;
                            if (i16 == i11 || i16 == 7 || i16 == i10) {
                                i8 = i16;
                            } else if (f5328v) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("Given tag (");
                                sb2.append(str3);
                                sb2.append(") value didn't match with one of expected formats: ");
                                String[] strArr = W;
                                sb2.append(strArr[eVar.f5370c]);
                                int i17 = eVar.f5371d;
                                String str5 = BuildConfig.FLAVOR;
                                sb2.append(i17 == -1 ? BuildConfig.FLAVOR : ", " + strArr[eVar.f5371d]);
                                sb2.append(" (guess: ");
                                sb2.append(strArr[((Integer) x8.first).intValue()]);
                                if (((Integer) x8.second).intValue() != -1) {
                                    str5 = ", " + strArr[((Integer) x8.second).intValue()];
                                }
                                sb2.append(str5);
                                sb2.append(")");
                                Log.d("ExifInterface", sb2.toString());
                            }
                        } else {
                            i8 = eVar.f5371d;
                        }
                    }
                    switch (i8) {
                        case 1:
                            i9 = i11;
                            hashMap = this.f5339f[i13];
                            a9 = d.a(str4);
                            hashMap.put(str3, a9);
                            break;
                        case 2:
                        case 7:
                            i9 = i11;
                            hashMap = this.f5339f[i13];
                            a9 = d.e(str4);
                            hashMap.put(str3, a9);
                            break;
                        case 3:
                            i9 = i11;
                            String[] split = str4.split(",", -1);
                            int[] iArr = new int[split.length];
                            for (int i18 = 0; i18 < split.length; i18++) {
                                iArr[i18] = Integer.parseInt(split[i18]);
                            }
                            hashMap = this.f5339f[i13];
                            a9 = d.k(iArr, this.f5341h);
                            hashMap.put(str3, a9);
                            break;
                        case 4:
                            i9 = i11;
                            String[] split2 = str4.split(",", -1);
                            long[] jArr = new long[split2.length];
                            for (int i19 = 0; i19 < split2.length; i19++) {
                                jArr[i19] = Long.parseLong(split2[i19]);
                            }
                            hashMap = this.f5339f[i13];
                            a9 = d.g(jArr, this.f5341h);
                            hashMap.put(str3, a9);
                            break;
                        case 5:
                            String[] split3 = str4.split(",", -1);
                            f[] fVarArr = new f[split3.length];
                            int i20 = 0;
                            while (i20 < split3.length) {
                                String[] split4 = split3[i20].split("/", i14);
                                fVarArr[i20] = new f((long) Double.parseDouble(split4[0]), (long) Double.parseDouble(split4[1]));
                                i20++;
                                i14 = -1;
                            }
                            i9 = 1;
                            hashMap = this.f5339f[i13];
                            a9 = d.i(fVarArr, this.f5341h);
                            hashMap.put(str3, a9);
                            break;
                        case 6:
                        case 8:
                        case 11:
                        default:
                            i9 = i11;
                            if (f5328v) {
                                Log.d("ExifInterface", "Data format isn't one of expected formats: " + i8);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            String[] split5 = str4.split(",", -1);
                            int[] iArr2 = new int[split5.length];
                            for (int i21 = 0; i21 < split5.length; i21++) {
                                iArr2[i21] = Integer.parseInt(split5[i21]);
                            }
                            hashMap2 = this.f5339f[i13];
                            c9 = d.c(iArr2, this.f5341h);
                            hashMap2.put(str3, c9);
                            i9 = 1;
                            break;
                        case 10:
                            String[] split6 = str4.split(",", -1);
                            f[] fVarArr2 = new f[split6.length];
                            int i22 = i12;
                            while (i22 < split6.length) {
                                String[] split7 = split6[i22].split("/", -1);
                                fVarArr2[i22] = new f((long) Double.parseDouble(split7[i12]), (long) Double.parseDouble(split7[i11]));
                                i22++;
                                split6 = split6;
                                i12 = 0;
                                i11 = 1;
                            }
                            hashMap2 = this.f5339f[i13];
                            c9 = d.d(fVarArr2, this.f5341h);
                            hashMap2.put(str3, c9);
                            i9 = 1;
                            break;
                        case 12:
                            String[] split8 = str4.split(",", -1);
                            double[] dArr = new double[split8.length];
                            for (int i23 = i12; i23 < split8.length; i23++) {
                                dArr[i23] = Double.parseDouble(split8[i23]);
                            }
                            this.f5339f[i13].put(str3, d.b(dArr, this.f5341h));
                            break;
                    }
                    i13++;
                    i11 = i9;
                    i10 = 2;
                    i12 = 0;
                }
            }
            i9 = i11;
            i13++;
            i11 = i9;
            i10 = 2;
            i12 = 0;
        }
    }

    public double e(double d8) {
        double g8 = g("GPSAltitude", -1.0d);
        int h8 = h("GPSAltitudeRef", -1);
        if (g8 < 0.0d || h8 < 0) {
            return d8;
        }
        return g8 * (h8 != 1 ? 1 : -1);
    }

    public String f(String str) {
        String str2;
        Objects.requireNonNull(str, "tag shouldn't be null");
        d i8 = i(str);
        if (i8 != null) {
            if (!f5320n0.contains(str)) {
                return i8.n(this.f5341h);
            }
            if (str.equals("GPSTimeStamp")) {
                int i9 = i8.f5364a;
                if (i9 == 5 || i9 == 10) {
                    f[] fVarArr = (f[]) i8.o(this.f5341h);
                    if (fVarArr != null && fVarArr.length == 3) {
                        return String.format("%02d:%02d:%02d", Integer.valueOf((int) (((float) fVarArr[0].f5372a) / ((float) fVarArr[0].f5373b))), Integer.valueOf((int) (((float) fVarArr[1].f5372a) / ((float) fVarArr[1].f5373b))), Integer.valueOf((int) (((float) fVarArr[2].f5372a) / ((float) fVarArr[2].f5373b))));
                    }
                    str2 = "Invalid GPS Timestamp array. array=" + Arrays.toString(fVarArr);
                } else {
                    str2 = "GPS Timestamp format is not rational. format=" + i8.f5364a;
                }
                Log.w("ExifInterface", str2);
                return null;
            }
            try {
                return Double.toString(i8.l(this.f5341h));
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    public double g(String str, double d8) {
        Objects.requireNonNull(str, "tag shouldn't be null");
        d i8 = i(str);
        if (i8 == null) {
            return d8;
        }
        try {
            return i8.l(this.f5341h);
        } catch (NumberFormatException unused) {
            return d8;
        }
    }

    public int h(String str, int i8) {
        Objects.requireNonNull(str, "tag shouldn't be null");
        d i9 = i(str);
        if (i9 == null) {
            return i8;
        }
        try {
            return i9.m(this.f5341h);
        } catch (NumberFormatException unused) {
            return i8;
        }
    }

    public double[] l() {
        String f5 = f("GPSLatitude");
        String f8 = f("GPSLatitudeRef");
        String f9 = f("GPSLongitude");
        String f10 = f("GPSLongitudeRef");
        if (f5 == null || f8 == null || f9 == null || f10 == null) {
            return null;
        }
        try {
            return new double[]{b(f5, f8), b(f9, f10)};
        } catch (IllegalArgumentException unused) {
            Log.w("ExifInterface", "Latitude/longitude values are not parsable. " + String.format("latValue=%s, latRef=%s, lngValue=%s, lngRef=%s", f5, f8, f9, f10));
            return null;
        }
    }

    public int r() {
        switch (h("Orientation", 1)) {
            case 3:
            case 4:
                return 180;
            case 5:
            case 8:
                return 270;
            case 6:
            case 7:
                return 90;
            default:
                return 0;
        }
    }

    public byte[] u() {
        int i8 = this.f5348o;
        if (i8 == 6 || i8 == 7) {
            return v();
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0062 A[Catch: Exception -> 0x0081, all -> 0x009e, TRY_ENTER, TRY_LEAVE, TryCatch #6 {all -> 0x009e, blocks: (B:36:0x0062, B:43:0x0083, B:44:0x0088, B:49:0x0090), top: B:60:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x0083 A[Catch: Exception -> 0x0081, all -> 0x009e, TRY_ENTER, TryCatch #6 {all -> 0x009e, blocks: (B:36:0x0062, B:43:0x0083, B:44:0x0088, B:49:0x0090), top: B:60:0x000a }] */
    /* JADX WARN: Removed duplicated region for block: B:52:0x009a  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00a5  */
    /* JADX WARN: Type inference failed for: r1v1, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v5, types: [android.content.res.AssetManager$AssetInputStream, java.io.Closeable, java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.Closeable, java.io.InputStream] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public byte[] v() {
        /*
            r8 = this;
            java.lang.String r0 = "ExifInterface"
            boolean r1 = r8.f5342i
            r2 = 0
            if (r1 != 0) goto L8
            return r2
        L8:
            byte[] r1 = r8.f5347n
            if (r1 == 0) goto Ld
            return r1
        Ld:
            android.content.res.AssetManager$AssetInputStream r1 = r8.f5336c     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            if (r1 == 0) goto L2e
            boolean r3 = r1.markSupported()     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L29
            if (r3 == 0) goto L1c
            r1.reset()     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L29
        L1a:
            r3 = r2
            goto L60
        L1c:
            java.lang.String r3 = "Cannot read thumbnail from inputstream without mark/reset support"
            android.util.Log.d(r0, r3)     // Catch: java.lang.Throwable -> L25 java.lang.Exception -> L29
            androidx.exifinterface.media.b.b(r1)
            return r2
        L25:
            r0 = move-exception
            r3 = r2
            goto L9f
        L29:
            r3 = move-exception
            r4 = r3
            r3 = r2
            goto L90
        L2e:
            java.lang.String r1 = r8.f5334a     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            if (r1 == 0) goto L3a
            java.io.FileInputStream r1 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            java.lang.String r3 = r8.f5334a     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            goto L1a
        L3a:
            int r1 = android.os.Build.VERSION.SDK_INT     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            r3 = 21
            if (r1 < r3) goto L5e
            java.io.FileDescriptor r1 = r8.f5335b     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            java.io.FileDescriptor r1 = androidx.exifinterface.media.b.a.b(r1)     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8c
            r3 = 0
            int r5 = android.system.OsConstants.SEEK_SET     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L59
            androidx.exifinterface.media.b.a.c(r1, r3, r5)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L59
            java.io.FileInputStream r3 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L59
            r3.<init>(r1)     // Catch: java.lang.Throwable -> L56 java.lang.Exception -> L59
            r7 = r3
            r3 = r1
            r1 = r7
            goto L60
        L56:
            r0 = move-exception
            r3 = r1
            goto La0
        L59:
            r3 = move-exception
            r4 = r3
            r3 = r1
            r1 = r2
            goto L90
        L5e:
            r1 = r2
            r3 = r1
        L60:
            if (r1 == 0) goto L83
            androidx.exifinterface.media.a$b r4 = new androidx.exifinterface.media.a$b     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            r4.<init>(r1)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            int r5 = r8.f5345l     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            int r6 = r8.f5349p     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            int r5 = r5 + r6
            r4.f(r5)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            int r5 = r8.f5346m     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            byte[] r5 = new byte[r5]     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            r4.readFully(r5)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            r8.f5347n = r5     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            androidx.exifinterface.media.b.b(r1)
            if (r3 == 0) goto L80
            androidx.exifinterface.media.b.a(r3)
        L80:
            return r5
        L81:
            r4 = move-exception
            goto L90
        L83:
            java.io.FileNotFoundException r4 = new java.io.FileNotFoundException     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            r4.<init>()     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
            throw r4     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L9e
        L89:
            r0 = move-exception
            r3 = r2
            goto La0
        L8c:
            r3 = move-exception
            r1 = r2
            r4 = r3
            r3 = r1
        L90:
            java.lang.String r5 = "Encountered exception while getting thumbnail"
            android.util.Log.d(r0, r5, r4)     // Catch: java.lang.Throwable -> L9e
            androidx.exifinterface.media.b.b(r1)
            if (r3 == 0) goto L9d
            androidx.exifinterface.media.b.a(r3)
        L9d:
            return r2
        L9e:
            r0 = move-exception
        L9f:
            r2 = r1
        La0:
            androidx.exifinterface.media.b.b(r2)
            if (r3 == 0) goto La8
            androidx.exifinterface.media.b.a(r3)
        La8:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.a.v():byte[]");
    }
}
