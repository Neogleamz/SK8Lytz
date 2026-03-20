package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.os.BadParcelableException;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.ResultReceiver;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.b;
import android.util.Log;
import androidx.core.app.g;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class MediaSessionCompat {

    /* renamed from: b  reason: collision with root package name */
    public static final int f306b;

    /* renamed from: a  reason: collision with root package name */
    private final MediaControllerCompat f307a;

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class QueueItem implements Parcelable {
        public static final Parcelable.Creator<QueueItem> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private final MediaDescriptionCompat f308a;

        /* renamed from: b  reason: collision with root package name */
        private final long f309b;

        /* renamed from: c  reason: collision with root package name */
        private MediaSession.QueueItem f310c;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<QueueItem> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public QueueItem createFromParcel(Parcel parcel) {
                return new QueueItem(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public QueueItem[] newArray(int i8) {
                return new QueueItem[i8];
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        public static class b {
            static MediaSession.QueueItem a(MediaDescription mediaDescription, long j8) {
                return new MediaSession.QueueItem(mediaDescription, j8);
            }

            static MediaDescription b(MediaSession.QueueItem queueItem) {
                return queueItem.getDescription();
            }

            static long c(MediaSession.QueueItem queueItem) {
                return queueItem.getQueueId();
            }
        }

        private QueueItem(MediaSession.QueueItem queueItem, MediaDescriptionCompat mediaDescriptionCompat, long j8) {
            if (mediaDescriptionCompat == null) {
                throw new IllegalArgumentException("Description cannot be null");
            }
            if (j8 == -1) {
                throw new IllegalArgumentException("Id cannot be QueueItem.UNKNOWN_ID");
            }
            this.f308a = mediaDescriptionCompat;
            this.f309b = j8;
            this.f310c = queueItem;
        }

        QueueItem(Parcel parcel) {
            this.f308a = MediaDescriptionCompat.CREATOR.createFromParcel(parcel);
            this.f309b = parcel.readLong();
        }

        public static QueueItem a(Object obj) {
            if (obj == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            MediaSession.QueueItem queueItem = (MediaSession.QueueItem) obj;
            return new QueueItem(queueItem, MediaDescriptionCompat.a(b.b(queueItem)), b.c(queueItem));
        }

        public static List<QueueItem> b(List<?> list) {
            if (list == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            Iterator<?> it = list.iterator();
            while (it.hasNext()) {
                arrayList.add(a(it.next()));
            }
            return arrayList;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String toString() {
            return "MediaSession.QueueItem {Description=" + this.f308a + ", Id=" + this.f309b + " }";
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            this.f308a.writeToParcel(parcel, i8);
            parcel.writeLong(this.f309b);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class ResultReceiverWrapper implements Parcelable {
        public static final Parcelable.Creator<ResultReceiverWrapper> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        ResultReceiver f311a;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<ResultReceiverWrapper> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public ResultReceiverWrapper createFromParcel(Parcel parcel) {
                return new ResultReceiverWrapper(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public ResultReceiverWrapper[] newArray(int i8) {
                return new ResultReceiverWrapper[i8];
            }
        }

        ResultReceiverWrapper(Parcel parcel) {
            this.f311a = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            this.f311a.writeToParcel(parcel, i8);
        }
    }

    @SuppressLint({"BanParcelableUsage"})
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class Token implements Parcelable {
        public static final Parcelable.Creator<Token> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private final Object f312a;

        /* renamed from: b  reason: collision with root package name */
        private final Object f313b;

        /* renamed from: c  reason: collision with root package name */
        private b f314c;

        /* renamed from: d  reason: collision with root package name */
        private y1.b f315d;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<Token> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public Token createFromParcel(Parcel parcel) {
                return new Token(Build.VERSION.SDK_INT >= 21 ? parcel.readParcelable(null) : parcel.readStrongBinder());
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public Token[] newArray(int i8) {
                return new Token[i8];
            }
        }

        Token(Object obj) {
            this(obj, null, null);
        }

        Token(Object obj, b bVar) {
            this(obj, bVar, null);
        }

        Token(Object obj, b bVar, y1.b bVar2) {
            this.f312a = new Object();
            this.f313b = obj;
            this.f314c = bVar;
            this.f315d = bVar2;
        }

        public static Token a(Bundle bundle) {
            if (bundle == null) {
                return null;
            }
            bundle.setClassLoader(Token.class.getClassLoader());
            b d8 = b.a.d(g.a(bundle, "android.support.v4.media.session.EXTRA_BINDER"));
            y1.b b9 = y1.a.b(bundle, "android.support.v4.media.session.SESSION_TOKEN2");
            Token token = (Token) bundle.getParcelable("android.support.v4.media.session.TOKEN");
            if (token == null) {
                return null;
            }
            return new Token(token.f313b, d8, b9);
        }

        public static Token b(Object obj) {
            return c(obj, null);
        }

        public static Token c(Object obj, b bVar) {
            if (obj == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            if (obj instanceof MediaSession.Token) {
                return new Token(obj, bVar);
            }
            throw new IllegalArgumentException("token is not a valid MediaSession.Token object");
        }

        public b d() {
            b bVar;
            synchronized (this.f312a) {
                bVar = this.f314c;
            }
            return bVar;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public y1.b e() {
            y1.b bVar;
            synchronized (this.f312a) {
                bVar = this.f315d;
            }
            return bVar;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj instanceof Token) {
                Object obj2 = this.f313b;
                Object obj3 = ((Token) obj).f313b;
                if (obj2 == null) {
                    return obj3 == null;
                } else if (obj3 == null) {
                    return false;
                } else {
                    return obj2.equals(obj3);
                }
            }
            return false;
        }

        public Object f() {
            return this.f313b;
        }

        public void g(b bVar) {
            synchronized (this.f312a) {
                this.f314c = bVar;
            }
        }

        public void h(y1.b bVar) {
            synchronized (this.f312a) {
                this.f315d = bVar;
            }
        }

        public int hashCode() {
            Object obj = this.f313b;
            if (obj == null) {
                return 0;
            }
            return obj.hashCode();
        }

        public Bundle i() {
            Bundle bundle = new Bundle();
            bundle.putParcelable("android.support.v4.media.session.TOKEN", this);
            synchronized (this.f312a) {
                b bVar = this.f314c;
                if (bVar != null) {
                    g.b(bundle, "android.support.v4.media.session.EXTRA_BINDER", bVar.asBinder());
                }
                y1.b bVar2 = this.f315d;
                if (bVar2 != null) {
                    y1.a.c(bundle, "android.support.v4.media.session.SESSION_TOKEN2", bVar2);
                }
            }
            return bundle;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            if (Build.VERSION.SDK_INT >= 21) {
                parcel.writeParcelable((Parcelable) this.f313b, i8);
            } else {
                parcel.writeStrongBinder((IBinder) this.f313b);
            }
        }
    }

    static {
        f306b = androidx.core.os.a.c() ? 33554432 : 0;
    }

    public static void a(Bundle bundle) {
        if (bundle != null) {
            bundle.setClassLoader(MediaSessionCompat.class.getClassLoader());
        }
    }

    public static Bundle c(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        a(bundle);
        try {
            bundle.isEmpty();
            return bundle;
        } catch (BadParcelableException unused) {
            Log.e("MediaSessionCompat", "Could not unparcel the data.");
            return null;
        }
    }

    public MediaControllerCompat b() {
        return this.f307a;
    }
}
