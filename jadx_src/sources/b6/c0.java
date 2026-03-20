package b6;

import android.os.SystemClock;
import com.google.android.exoplayer2.upstream.Loader;
import com.google.android.gms.dynamite.descriptors.com.google.mlkit.dynamite.barcode.ModuleDescriptor;
import com.google.android.libraries.barhopper.RecognitionOptions;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class c0 {

    /* renamed from: a  reason: collision with root package name */
    private static final Object f8023a = new Object();

    /* renamed from: b  reason: collision with root package name */
    private static final Object f8024b = new Object();

    /* renamed from: c  reason: collision with root package name */
    private static boolean f8025c = false;

    /* renamed from: d  reason: collision with root package name */
    private static long f8026d = 0;

    /* renamed from: e  reason: collision with root package name */
    private static String f8027e = "time.android.com";

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public interface b {
        void a(IOException iOException);

        void b();
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c implements Loader.b<Loader.e> {

        /* renamed from: a  reason: collision with root package name */
        private final b f8028a;

        public c(b bVar) {
            this.f8028a = bVar;
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        public void j(Loader.e eVar, long j8, long j9, boolean z4) {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        public void k(Loader.e eVar, long j8, long j9) {
            if (this.f8028a != null) {
                if (c0.k()) {
                    this.f8028a.b();
                } else {
                    this.f8028a.a(new IOException(new ConcurrentModificationException()));
                }
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.b
        public Loader.c t(Loader.e eVar, long j8, long j9, IOException iOException, int i8) {
            b bVar = this.f8028a;
            if (bVar != null) {
                bVar.a(iOException);
            }
            return Loader.f10908f;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class d implements Loader.e {
        private d() {
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.e
        public void a() {
            synchronized (c0.f8023a) {
                synchronized (c0.f8024b) {
                    if (c0.f8025c) {
                        return;
                    }
                    long e8 = c0.e();
                    synchronized (c0.f8024b) {
                        long unused = c0.f8026d = e8;
                        boolean unused2 = c0.f8025c = true;
                    }
                }
            }
        }

        @Override // com.google.android.exoplayer2.upstream.Loader.e
        public void c() {
        }
    }

    static /* synthetic */ long e() {
        return l();
    }

    private static void g(byte b9, byte b10, int i8, long j8) {
        if (b9 == 3) {
            throw new IOException("SNTP: Unsynchronized server");
        }
        if (b10 != 4 && b10 != 5) {
            throw new IOException("SNTP: Untrusted mode: " + ((int) b10));
        } else if (i8 != 0 && i8 <= 15) {
            if (j8 == 0) {
                throw new IOException("SNTP: Zero transmitTime");
            }
        } else {
            throw new IOException("SNTP: Untrusted stratum: " + i8);
        }
    }

    public static long h() {
        long j8;
        synchronized (f8024b) {
            j8 = f8025c ? f8026d : -9223372036854775807L;
        }
        return j8;
    }

    public static String i() {
        String str;
        synchronized (f8024b) {
            str = f8027e;
        }
        return str;
    }

    public static void j(Loader loader, b bVar) {
        if (k()) {
            if (bVar != null) {
                bVar.b();
                return;
            }
            return;
        }
        if (loader == null) {
            loader = new Loader("SntpClient");
        }
        loader.n(new d(), new c(bVar), 1);
    }

    public static boolean k() {
        boolean z4;
        synchronized (f8024b) {
            z4 = f8025c;
        }
        return z4;
    }

    private static long l() {
        InetAddress byName = InetAddress.getByName(i());
        DatagramSocket datagramSocket = new DatagramSocket();
        try {
            datagramSocket.setSoTimeout(ModuleDescriptor.MODULE_VERSION);
            byte[] bArr = new byte[48];
            DatagramPacket datagramPacket = new DatagramPacket(bArr, 48, byName, 123);
            bArr[0] = 27;
            long currentTimeMillis = System.currentTimeMillis();
            long elapsedRealtime = SystemClock.elapsedRealtime();
            o(bArr, 40, currentTimeMillis);
            datagramSocket.send(datagramPacket);
            datagramSocket.receive(new DatagramPacket(bArr, 48));
            long elapsedRealtime2 = SystemClock.elapsedRealtime();
            long j8 = currentTimeMillis + (elapsedRealtime2 - elapsedRealtime);
            long n8 = n(bArr, 24);
            long n9 = n(bArr, 32);
            long n10 = n(bArr, 40);
            g((byte) ((bArr[0] >> 6) & 3), (byte) (bArr[0] & 7), bArr[1] & 255, n10);
            long j9 = (j8 + (((n9 - n8) + (n10 - j8)) / 2)) - elapsedRealtime2;
            datagramSocket.close();
            return j9;
        } catch (Throwable th) {
            try {
                datagramSocket.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
            throw th;
        }
    }

    private static long m(byte[] bArr, int i8) {
        int i9 = bArr[i8];
        int i10 = bArr[i8 + 1];
        int i11 = bArr[i8 + 2];
        int i12 = bArr[i8 + 3];
        if ((i9 & RecognitionOptions.ITF) == 128) {
            i9 = (i9 & 127) + RecognitionOptions.ITF;
        }
        if ((i10 & RecognitionOptions.ITF) == 128) {
            i10 = (i10 & 127) + RecognitionOptions.ITF;
        }
        if ((i11 & RecognitionOptions.ITF) == 128) {
            i11 = (i11 & 127) + RecognitionOptions.ITF;
        }
        if ((i12 & RecognitionOptions.ITF) == 128) {
            i12 = (i12 & 127) + RecognitionOptions.ITF;
        }
        return (i9 << 24) + (i10 << 16) + (i11 << 8) + i12;
    }

    private static long n(byte[] bArr, int i8) {
        long m8 = m(bArr, i8);
        long m9 = m(bArr, i8 + 4);
        if (m8 == 0 && m9 == 0) {
            return 0L;
        }
        return ((m8 - 2208988800L) * 1000) + ((m9 * 1000) / 4294967296L);
    }

    private static void o(byte[] bArr, int i8, long j8) {
        if (j8 == 0) {
            Arrays.fill(bArr, i8, i8 + 8, (byte) 0);
            return;
        }
        long j9 = j8 / 1000;
        long j10 = j9 + 2208988800L;
        int i9 = i8 + 1;
        bArr[i8] = (byte) (j10 >> 24);
        int i10 = i9 + 1;
        bArr[i9] = (byte) (j10 >> 16);
        int i11 = i10 + 1;
        bArr[i10] = (byte) (j10 >> 8);
        int i12 = i11 + 1;
        bArr[i11] = (byte) (j10 >> 0);
        long j11 = ((j8 - (j9 * 1000)) * 4294967296L) / 1000;
        int i13 = i12 + 1;
        bArr[i12] = (byte) (j11 >> 24);
        int i14 = i13 + 1;
        bArr[i13] = (byte) (j11 >> 16);
        bArr[i14] = (byte) (j11 >> 8);
        bArr[i14 + 1] = (byte) (Math.random() * 255.0d);
    }
}
