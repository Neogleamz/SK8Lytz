package androidx.room;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Pair;
import androidx.room.h;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class h implements t1.c, n {

    /* renamed from: a  reason: collision with root package name */
    private final t1.c f7121a;

    /* renamed from: b  reason: collision with root package name */
    private final a f7122b;

    /* renamed from: c  reason: collision with root package name */
    private final androidx.room.a f7123c;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static final class a implements t1.b {

        /* renamed from: a  reason: collision with root package name */
        private final androidx.room.a f7124a;

        a(androidx.room.a aVar) {
            this.f7124a = aVar;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Object d(String str, t1.b bVar) {
            bVar.H(str);
            return null;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Boolean f(t1.b bVar) {
            return Build.VERSION.SDK_INT >= 16 ? Boolean.valueOf(bVar.I1()) : Boolean.FALSE;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static /* synthetic */ Object h(t1.b bVar) {
            return null;
        }

        @Override // t1.b
        public boolean A1() {
            if (this.f7124a.d() == null) {
                return false;
            }
            return ((Boolean) this.f7124a.c(new n.a() { // from class: androidx.room.g
                @Override // n.a
                public final Object apply(Object obj) {
                    return Boolean.valueOf(((t1.b) obj).A1());
                }
            })).booleanValue();
        }

        @Override // t1.b
        public Cursor C0(t1.e eVar, CancellationSignal cancellationSignal) {
            try {
                return new c(this.f7124a.e().C0(eVar, cancellationSignal), this.f7124a);
            } catch (Throwable th) {
                this.f7124a.b();
                throw th;
            }
        }

        @Override // t1.b
        public List<Pair<String, String>> F() {
            return (List) this.f7124a.c(new n.a() { // from class: androidx.room.e
                @Override // n.a
                public final Object apply(Object obj) {
                    return ((t1.b) obj).F();
                }
            });
        }

        @Override // t1.b
        public void H(final String str) {
            this.f7124a.c(new n.a() { // from class: androidx.room.b
                @Override // n.a
                public final Object apply(Object obj) {
                    Object d8;
                    d8 = h.a.d(str, (t1.b) obj);
                    return d8;
                }
            });
        }

        @Override // t1.b
        @SuppressLint({"UnsafeNewApiCall"})
        public boolean I1() {
            return ((Boolean) this.f7124a.c(new n.a() { // from class: androidx.room.c
                @Override // n.a
                public final Object apply(Object obj) {
                    Boolean f5;
                    f5 = h.a.f((t1.b) obj);
                    return f5;
                }
            })).booleanValue();
        }

        @Override // t1.b
        public void J0() {
            if (this.f7124a.d() == null) {
                throw new IllegalStateException("End transaction called but delegateDb is null");
            }
            try {
                this.f7124a.d().J0();
            } finally {
                this.f7124a.b();
            }
        }

        @Override // t1.b
        public t1.f O(String str) {
            return new b(str, this.f7124a);
        }

        @Override // t1.b
        public Cursor X(t1.e eVar) {
            try {
                return new c(this.f7124a.e().X(eVar), this.f7124a);
            } catch (Throwable th) {
                this.f7124a.b();
                throw th;
            }
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.f7124a.a();
        }

        void i() {
            this.f7124a.c(new n.a() { // from class: androidx.room.d
                @Override // n.a
                public final Object apply(Object obj) {
                    Object h8;
                    h8 = h.a.h((t1.b) obj);
                    return h8;
                }
            });
        }

        @Override // t1.b
        public void i0() {
            t1.b d8 = this.f7124a.d();
            if (d8 == null) {
                throw new IllegalStateException("setTransactionSuccessful called but delegateDb is null");
            }
            d8.i0();
        }

        @Override // t1.b
        public boolean isOpen() {
            t1.b d8 = this.f7124a.d();
            if (d8 == null) {
                return false;
            }
            return d8.isOpen();
        }

        @Override // t1.b
        public void l0() {
            try {
                this.f7124a.e().l0();
            } catch (Throwable th) {
                this.f7124a.b();
                throw th;
            }
        }

        @Override // t1.b
        public Cursor w0(String str) {
            try {
                return new c(this.f7124a.e().w0(str), this.f7124a);
            } catch (Throwable th) {
                this.f7124a.b();
                throw th;
            }
        }

        @Override // t1.b
        public void x() {
            try {
                this.f7124a.e().x();
            } catch (Throwable th) {
                this.f7124a.b();
                throw th;
            }
        }

        @Override // t1.b
        public String y1() {
            return (String) this.f7124a.c(new n.a() { // from class: androidx.room.f
                @Override // n.a
                public final Object apply(Object obj) {
                    return ((t1.b) obj).y1();
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b implements t1.f {

        /* renamed from: a  reason: collision with root package name */
        private final String f7125a;

        /* renamed from: b  reason: collision with root package name */
        private final ArrayList<Object> f7126b = new ArrayList<>();

        /* renamed from: c  reason: collision with root package name */
        private final androidx.room.a f7127c;

        b(String str, androidx.room.a aVar) {
            this.f7125a = str;
            this.f7127c = aVar;
        }

        private void b(t1.f fVar) {
            int i8 = 0;
            while (i8 < this.f7126b.size()) {
                int i9 = i8 + 1;
                Object obj = this.f7126b.get(i8);
                if (obj == null) {
                    fVar.o1(i9);
                } else if (obj instanceof Long) {
                    fVar.h0(i9, ((Long) obj).longValue());
                } else if (obj instanceof Double) {
                    fVar.Q(i9, ((Double) obj).doubleValue());
                } else if (obj instanceof String) {
                    fVar.I(i9, (String) obj);
                } else if (obj instanceof byte[]) {
                    fVar.o0(i9, (byte[]) obj);
                }
                i8 = i9;
            }
        }

        private <T> T c(final n.a<t1.f, T> aVar) {
            return (T) this.f7127c.c(new n.a() { // from class: androidx.room.i
                @Override // n.a
                public final Object apply(Object obj) {
                    Object d8;
                    d8 = h.b.this.d(aVar, (t1.b) obj);
                    return d8;
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ Object d(n.a aVar, t1.b bVar) {
            t1.f O = bVar.O(this.f7125a);
            b(O);
            return aVar.apply(O);
        }

        private void f(int i8, Object obj) {
            int i9 = i8 - 1;
            if (i9 >= this.f7126b.size()) {
                for (int size = this.f7126b.size(); size <= i9; size++) {
                    this.f7126b.add(null);
                }
            }
            this.f7126b.set(i9, obj);
        }

        @Override // t1.d
        public void I(int i8, String str) {
            f(i8, str);
        }

        @Override // t1.f
        public int N() {
            return ((Integer) c(new n.a() { // from class: androidx.room.j
                @Override // n.a
                public final Object apply(Object obj) {
                    return Integer.valueOf(((t1.f) obj).N());
                }
            })).intValue();
        }

        @Override // t1.d
        public void Q(int i8, double d8) {
            f(i8, Double.valueOf(d8));
        }

        @Override // t1.f
        public long W1() {
            return ((Long) c(new n.a() { // from class: androidx.room.k
                @Override // n.a
                public final Object apply(Object obj) {
                    return Long.valueOf(((t1.f) obj).W1());
                }
            })).longValue();
        }

        @Override // java.io.Closeable, java.lang.AutoCloseable
        public void close() {
        }

        @Override // t1.d
        public void h0(int i8, long j8) {
            f(i8, Long.valueOf(j8));
        }

        @Override // t1.d
        public void o0(int i8, byte[] bArr) {
            f(i8, bArr);
        }

        @Override // t1.d
        public void o1(int i8) {
            f(i8, null);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static final class c implements Cursor {

        /* renamed from: a  reason: collision with root package name */
        private final Cursor f7128a;

        /* renamed from: b  reason: collision with root package name */
        private final androidx.room.a f7129b;

        c(Cursor cursor, androidx.room.a aVar) {
            this.f7128a = cursor;
            this.f7129b = aVar;
        }

        @Override // android.database.Cursor, java.io.Closeable, java.lang.AutoCloseable
        public void close() {
            this.f7128a.close();
            this.f7129b.b();
        }

        @Override // android.database.Cursor
        public void copyStringToBuffer(int i8, CharArrayBuffer charArrayBuffer) {
            this.f7128a.copyStringToBuffer(i8, charArrayBuffer);
        }

        @Override // android.database.Cursor
        @Deprecated
        public void deactivate() {
            this.f7128a.deactivate();
        }

        @Override // android.database.Cursor
        public byte[] getBlob(int i8) {
            return this.f7128a.getBlob(i8);
        }

        @Override // android.database.Cursor
        public int getColumnCount() {
            return this.f7128a.getColumnCount();
        }

        @Override // android.database.Cursor
        public int getColumnIndex(String str) {
            return this.f7128a.getColumnIndex(str);
        }

        @Override // android.database.Cursor
        public int getColumnIndexOrThrow(String str) {
            return this.f7128a.getColumnIndexOrThrow(str);
        }

        @Override // android.database.Cursor
        public String getColumnName(int i8) {
            return this.f7128a.getColumnName(i8);
        }

        @Override // android.database.Cursor
        public String[] getColumnNames() {
            return this.f7128a.getColumnNames();
        }

        @Override // android.database.Cursor
        public int getCount() {
            return this.f7128a.getCount();
        }

        @Override // android.database.Cursor
        public double getDouble(int i8) {
            return this.f7128a.getDouble(i8);
        }

        @Override // android.database.Cursor
        public Bundle getExtras() {
            return this.f7128a.getExtras();
        }

        @Override // android.database.Cursor
        public float getFloat(int i8) {
            return this.f7128a.getFloat(i8);
        }

        @Override // android.database.Cursor
        public int getInt(int i8) {
            return this.f7128a.getInt(i8);
        }

        @Override // android.database.Cursor
        public long getLong(int i8) {
            return this.f7128a.getLong(i8);
        }

        @Override // android.database.Cursor
        @SuppressLint({"UnsafeNewApiCall"})
        public Uri getNotificationUri() {
            return this.f7128a.getNotificationUri();
        }

        @Override // android.database.Cursor
        @SuppressLint({"UnsafeNewApiCall"})
        public List<Uri> getNotificationUris() {
            return this.f7128a.getNotificationUris();
        }

        @Override // android.database.Cursor
        public int getPosition() {
            return this.f7128a.getPosition();
        }

        @Override // android.database.Cursor
        public short getShort(int i8) {
            return this.f7128a.getShort(i8);
        }

        @Override // android.database.Cursor
        public String getString(int i8) {
            return this.f7128a.getString(i8);
        }

        @Override // android.database.Cursor
        public int getType(int i8) {
            return this.f7128a.getType(i8);
        }

        @Override // android.database.Cursor
        public boolean getWantsAllOnMoveCalls() {
            return this.f7128a.getWantsAllOnMoveCalls();
        }

        @Override // android.database.Cursor
        public boolean isAfterLast() {
            return this.f7128a.isAfterLast();
        }

        @Override // android.database.Cursor
        public boolean isBeforeFirst() {
            return this.f7128a.isBeforeFirst();
        }

        @Override // android.database.Cursor
        public boolean isClosed() {
            return this.f7128a.isClosed();
        }

        @Override // android.database.Cursor
        public boolean isFirst() {
            return this.f7128a.isFirst();
        }

        @Override // android.database.Cursor
        public boolean isLast() {
            return this.f7128a.isLast();
        }

        @Override // android.database.Cursor
        public boolean isNull(int i8) {
            return this.f7128a.isNull(i8);
        }

        @Override // android.database.Cursor
        public boolean move(int i8) {
            return this.f7128a.move(i8);
        }

        @Override // android.database.Cursor
        public boolean moveToFirst() {
            return this.f7128a.moveToFirst();
        }

        @Override // android.database.Cursor
        public boolean moveToLast() {
            return this.f7128a.moveToLast();
        }

        @Override // android.database.Cursor
        public boolean moveToNext() {
            return this.f7128a.moveToNext();
        }

        @Override // android.database.Cursor
        public boolean moveToPosition(int i8) {
            return this.f7128a.moveToPosition(i8);
        }

        @Override // android.database.Cursor
        public boolean moveToPrevious() {
            return this.f7128a.moveToPrevious();
        }

        @Override // android.database.Cursor
        public void registerContentObserver(ContentObserver contentObserver) {
            this.f7128a.registerContentObserver(contentObserver);
        }

        @Override // android.database.Cursor
        public void registerDataSetObserver(DataSetObserver dataSetObserver) {
            this.f7128a.registerDataSetObserver(dataSetObserver);
        }

        @Override // android.database.Cursor
        @Deprecated
        public boolean requery() {
            return this.f7128a.requery();
        }

        @Override // android.database.Cursor
        public Bundle respond(Bundle bundle) {
            return this.f7128a.respond(bundle);
        }

        @Override // android.database.Cursor
        @SuppressLint({"UnsafeNewApiCall"})
        public void setExtras(Bundle bundle) {
            this.f7128a.setExtras(bundle);
        }

        @Override // android.database.Cursor
        public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
            this.f7128a.setNotificationUri(contentResolver, uri);
        }

        @Override // android.database.Cursor
        @SuppressLint({"UnsafeNewApiCall"})
        public void setNotificationUris(ContentResolver contentResolver, List<Uri> list) {
            this.f7128a.setNotificationUris(contentResolver, list);
        }

        @Override // android.database.Cursor
        public void unregisterContentObserver(ContentObserver contentObserver) {
            this.f7128a.unregisterContentObserver(contentObserver);
        }

        @Override // android.database.Cursor
        public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
            this.f7128a.unregisterDataSetObserver(dataSetObserver);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public h(t1.c cVar, androidx.room.a aVar) {
        this.f7121a = cVar;
        this.f7123c = aVar;
        aVar.f(cVar);
        this.f7122b = new a(aVar);
    }

    @Override // androidx.room.n
    public t1.c a() {
        return this.f7121a;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public androidx.room.a b() {
        return this.f7123c;
    }

    @Override // t1.c, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        try {
            this.f7122b.close();
        } catch (IOException e8) {
            r1.e.a(e8);
        }
    }

    @Override // t1.c
    public String getDatabaseName() {
        return this.f7121a.getDatabaseName();
    }

    @Override // t1.c
    public void setWriteAheadLoggingEnabled(boolean z4) {
        this.f7121a.setWriteAheadLoggingEnabled(z4);
    }

    @Override // t1.c
    public t1.b v0() {
        this.f7122b.i();
        return this.f7122b;
    }
}
