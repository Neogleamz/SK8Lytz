package com.google.android.exoplayer2.upstream;

import a6.e;
import android.net.Uri;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.SocketTimeoutException;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class UdpDataSource extends e {

    /* renamed from: e  reason: collision with root package name */
    private final int f10933e;

    /* renamed from: f  reason: collision with root package name */
    private final byte[] f10934f;

    /* renamed from: g  reason: collision with root package name */
    private final DatagramPacket f10935g;

    /* renamed from: h  reason: collision with root package name */
    private Uri f10936h;

    /* renamed from: i  reason: collision with root package name */
    private DatagramSocket f10937i;

    /* renamed from: j  reason: collision with root package name */
    private MulticastSocket f10938j;

    /* renamed from: k  reason: collision with root package name */
    private InetAddress f10939k;

    /* renamed from: l  reason: collision with root package name */
    private boolean f10940l;

    /* renamed from: m  reason: collision with root package name */
    private int f10941m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class UdpDataSourceException extends DataSourceException {
        public UdpDataSourceException(Throwable th, int i8) {
            super(th, i8);
        }
    }

    public UdpDataSource() {
        this(2000);
    }

    public UdpDataSource(int i8) {
        this(i8, 8000);
    }

    public UdpDataSource(int i8, int i9) {
        super(true);
        this.f10933e = i9;
        byte[] bArr = new byte[i8];
        this.f10934f = bArr;
        this.f10935g = new DatagramPacket(bArr, 0, i8);
    }

    @Override // a6.h
    public void close() {
        this.f10936h = null;
        MulticastSocket multicastSocket = this.f10938j;
        if (multicastSocket != null) {
            try {
                multicastSocket.leaveGroup((InetAddress) b6.a.e(this.f10939k));
            } catch (IOException unused) {
            }
            this.f10938j = null;
        }
        DatagramSocket datagramSocket = this.f10937i;
        if (datagramSocket != null) {
            datagramSocket.close();
            this.f10937i = null;
        }
        this.f10939k = null;
        this.f10941m = 0;
        if (this.f10940l) {
            this.f10940l = false;
            m();
        }
    }

    @Override // a6.f
    public int read(byte[] bArr, int i8, int i9) {
        if (i9 == 0) {
            return 0;
        }
        if (this.f10941m == 0) {
            try {
                ((DatagramSocket) b6.a.e(this.f10937i)).receive(this.f10935g);
                int length = this.f10935g.getLength();
                this.f10941m = length;
                l(length);
            } catch (SocketTimeoutException e8) {
                throw new UdpDataSourceException(e8, 2002);
            } catch (IOException e9) {
                throw new UdpDataSourceException(e9, 2001);
            }
        }
        int length2 = this.f10935g.getLength();
        int i10 = this.f10941m;
        int min = Math.min(i10, i9);
        System.arraycopy(this.f10934f, length2 - i10, bArr, i8, min);
        this.f10941m -= min;
        return min;
    }

    @Override // a6.h
    public Uri v() {
        return this.f10936h;
    }

    @Override // a6.h
    public long x(a aVar) {
        Uri uri = aVar.f10942a;
        this.f10936h = uri;
        String str = (String) b6.a.e(uri.getHost());
        int port = this.f10936h.getPort();
        n(aVar);
        try {
            this.f10939k = InetAddress.getByName(str);
            InetSocketAddress inetSocketAddress = new InetSocketAddress(this.f10939k, port);
            if (this.f10939k.isMulticastAddress()) {
                MulticastSocket multicastSocket = new MulticastSocket(inetSocketAddress);
                this.f10938j = multicastSocket;
                multicastSocket.joinGroup(this.f10939k);
                this.f10937i = this.f10938j;
            } else {
                this.f10937i = new DatagramSocket(inetSocketAddress);
            }
            this.f10937i.setSoTimeout(this.f10933e);
            this.f10940l = true;
            o(aVar);
            return -1L;
        } catch (IOException e8) {
            throw new UdpDataSourceException(e8, 2001);
        } catch (SecurityException e9) {
            throw new UdpDataSourceException(e9, 2006);
        }
    }
}
