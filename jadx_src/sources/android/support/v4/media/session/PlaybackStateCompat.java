package android.support.v4.media.session;

import android.annotation.SuppressLint;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;
@SuppressLint({"BanParcelableUsage"})
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class PlaybackStateCompat implements Parcelable {
    public static final Parcelable.Creator<PlaybackStateCompat> CREATOR = new a();

    /* renamed from: a  reason: collision with root package name */
    final int f321a;

    /* renamed from: b  reason: collision with root package name */
    final long f322b;

    /* renamed from: c  reason: collision with root package name */
    final long f323c;

    /* renamed from: d  reason: collision with root package name */
    final float f324d;

    /* renamed from: e  reason: collision with root package name */
    final long f325e;

    /* renamed from: f  reason: collision with root package name */
    final int f326f;

    /* renamed from: g  reason: collision with root package name */
    final CharSequence f327g;

    /* renamed from: h  reason: collision with root package name */
    final long f328h;

    /* renamed from: j  reason: collision with root package name */
    List<CustomAction> f329j;

    /* renamed from: k  reason: collision with root package name */
    final long f330k;

    /* renamed from: l  reason: collision with root package name */
    final Bundle f331l;

    /* renamed from: m  reason: collision with root package name */
    private PlaybackState f332m;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static final class CustomAction implements Parcelable {
        public static final Parcelable.Creator<CustomAction> CREATOR = new a();

        /* renamed from: a  reason: collision with root package name */
        private final String f333a;

        /* renamed from: b  reason: collision with root package name */
        private final CharSequence f334b;

        /* renamed from: c  reason: collision with root package name */
        private final int f335c;

        /* renamed from: d  reason: collision with root package name */
        private final Bundle f336d;

        /* renamed from: e  reason: collision with root package name */
        private PlaybackState.CustomAction f337e;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        class a implements Parcelable.Creator<CustomAction> {
            a() {
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: a */
            public CustomAction createFromParcel(Parcel parcel) {
                return new CustomAction(parcel);
            }

            @Override // android.os.Parcelable.Creator
            /* renamed from: b */
            public CustomAction[] newArray(int i8) {
                return new CustomAction[i8];
            }
        }

        CustomAction(Parcel parcel) {
            this.f333a = parcel.readString();
            this.f334b = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
            this.f335c = parcel.readInt();
            this.f336d = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        }

        CustomAction(String str, CharSequence charSequence, int i8, Bundle bundle) {
            this.f333a = str;
            this.f334b = charSequence;
            this.f335c = i8;
            this.f336d = bundle;
        }

        public static CustomAction a(Object obj) {
            if (obj == null || Build.VERSION.SDK_INT < 21) {
                return null;
            }
            PlaybackState.CustomAction customAction = (PlaybackState.CustomAction) obj;
            Bundle l8 = b.l(customAction);
            MediaSessionCompat.a(l8);
            CustomAction customAction2 = new CustomAction(b.f(customAction), b.o(customAction), b.m(customAction), l8);
            customAction2.f337e = customAction;
            return customAction2;
        }

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public String toString() {
            return "Action:mName='" + ((Object) this.f334b) + ", mIcon=" + this.f335c + ", mExtras=" + this.f336d;
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i8) {
            parcel.writeString(this.f333a);
            TextUtils.writeToParcel(this.f334b, parcel, i8);
            parcel.writeInt(this.f335c);
            parcel.writeBundle(this.f336d);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    class a implements Parcelable.Creator<PlaybackStateCompat> {
        a() {
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: a */
        public PlaybackStateCompat createFromParcel(Parcel parcel) {
            return new PlaybackStateCompat(parcel);
        }

        @Override // android.os.Parcelable.Creator
        /* renamed from: b */
        public PlaybackStateCompat[] newArray(int i8) {
            return new PlaybackStateCompat[i8];
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    public static class b {
        static void a(PlaybackState.Builder builder, PlaybackState.CustomAction customAction) {
            builder.addCustomAction(customAction);
        }

        static PlaybackState.CustomAction b(PlaybackState.CustomAction.Builder builder) {
            return builder.build();
        }

        static PlaybackState c(PlaybackState.Builder builder) {
            return builder.build();
        }

        static PlaybackState.Builder d() {
            return new PlaybackState.Builder();
        }

        static PlaybackState.CustomAction.Builder e(String str, CharSequence charSequence, int i8) {
            return new PlaybackState.CustomAction.Builder(str, charSequence, i8);
        }

        static String f(PlaybackState.CustomAction customAction) {
            return customAction.getAction();
        }

        static long g(PlaybackState playbackState) {
            return playbackState.getActions();
        }

        static long h(PlaybackState playbackState) {
            return playbackState.getActiveQueueItemId();
        }

        static long i(PlaybackState playbackState) {
            return playbackState.getBufferedPosition();
        }

        static List<PlaybackState.CustomAction> j(PlaybackState playbackState) {
            return playbackState.getCustomActions();
        }

        static CharSequence k(PlaybackState playbackState) {
            return playbackState.getErrorMessage();
        }

        static Bundle l(PlaybackState.CustomAction customAction) {
            return customAction.getExtras();
        }

        static int m(PlaybackState.CustomAction customAction) {
            return customAction.getIcon();
        }

        static long n(PlaybackState playbackState) {
            return playbackState.getLastPositionUpdateTime();
        }

        static CharSequence o(PlaybackState.CustomAction customAction) {
            return customAction.getName();
        }

        static float p(PlaybackState playbackState) {
            return playbackState.getPlaybackSpeed();
        }

        static long q(PlaybackState playbackState) {
            return playbackState.getPosition();
        }

        static int r(PlaybackState playbackState) {
            return playbackState.getState();
        }

        static void s(PlaybackState.Builder builder, long j8) {
            builder.setActions(j8);
        }

        static void t(PlaybackState.Builder builder, long j8) {
            builder.setActiveQueueItemId(j8);
        }

        static void u(PlaybackState.Builder builder, long j8) {
            builder.setBufferedPosition(j8);
        }

        static void v(PlaybackState.Builder builder, CharSequence charSequence) {
            builder.setErrorMessage(charSequence);
        }

        static void w(PlaybackState.CustomAction.Builder builder, Bundle bundle) {
            builder.setExtras(bundle);
        }

        static void x(PlaybackState.Builder builder, int i8, long j8, float f5, long j9) {
            builder.setState(i8, j8, f5, j9);
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class c {
        static Bundle a(PlaybackState playbackState) {
            return playbackState.getExtras();
        }

        static void b(PlaybackState.Builder builder, Bundle bundle) {
            builder.setExtras(bundle);
        }
    }

    PlaybackStateCompat(int i8, long j8, long j9, float f5, long j10, int i9, CharSequence charSequence, long j11, List<CustomAction> list, long j12, Bundle bundle) {
        this.f321a = i8;
        this.f322b = j8;
        this.f323c = j9;
        this.f324d = f5;
        this.f325e = j10;
        this.f326f = i9;
        this.f327g = charSequence;
        this.f328h = j11;
        this.f329j = new ArrayList(list);
        this.f330k = j12;
        this.f331l = bundle;
    }

    PlaybackStateCompat(Parcel parcel) {
        this.f321a = parcel.readInt();
        this.f322b = parcel.readLong();
        this.f324d = parcel.readFloat();
        this.f328h = parcel.readLong();
        this.f323c = parcel.readLong();
        this.f325e = parcel.readLong();
        this.f327g = (CharSequence) TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(parcel);
        this.f329j = parcel.createTypedArrayList(CustomAction.CREATOR);
        this.f330k = parcel.readLong();
        this.f331l = parcel.readBundle(MediaSessionCompat.class.getClassLoader());
        this.f326f = parcel.readInt();
    }

    public static PlaybackStateCompat a(Object obj) {
        ArrayList arrayList;
        Bundle bundle = null;
        if (obj == null || Build.VERSION.SDK_INT < 21) {
            return null;
        }
        PlaybackState playbackState = (PlaybackState) obj;
        List<PlaybackState.CustomAction> j8 = b.j(playbackState);
        if (j8 != null) {
            ArrayList arrayList2 = new ArrayList(j8.size());
            for (PlaybackState.CustomAction customAction : j8) {
                arrayList2.add(CustomAction.a(customAction));
            }
            arrayList = arrayList2;
        } else {
            arrayList = null;
        }
        if (Build.VERSION.SDK_INT >= 22) {
            bundle = c.a(playbackState);
            MediaSessionCompat.a(bundle);
        }
        PlaybackStateCompat playbackStateCompat = new PlaybackStateCompat(b.r(playbackState), b.q(playbackState), b.i(playbackState), b.p(playbackState), b.g(playbackState), 0, b.k(playbackState), b.n(playbackState), arrayList, b.h(playbackState), bundle);
        playbackStateCompat.f332m = playbackState;
        return playbackStateCompat;
    }

    public static int b(long j8) {
        if (j8 == 4) {
            return 126;
        }
        if (j8 == 2) {
            return 127;
        }
        if (j8 == 32) {
            return 87;
        }
        if (j8 == 16) {
            return 88;
        }
        if (j8 == 1) {
            return 86;
        }
        if (j8 == 64) {
            return 90;
        }
        if (j8 == 8) {
            return 89;
        }
        return j8 == 512 ? 85 : 0;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String toString() {
        return "PlaybackState {state=" + this.f321a + ", position=" + this.f322b + ", buffered position=" + this.f323c + ", speed=" + this.f324d + ", updated=" + this.f328h + ", actions=" + this.f325e + ", error code=" + this.f326f + ", error message=" + this.f327g + ", custom actions=" + this.f329j + ", active item id=" + this.f330k + "}";
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i8) {
        parcel.writeInt(this.f321a);
        parcel.writeLong(this.f322b);
        parcel.writeFloat(this.f324d);
        parcel.writeLong(this.f328h);
        parcel.writeLong(this.f323c);
        parcel.writeLong(this.f325e);
        TextUtils.writeToParcel(this.f327g, parcel, i8);
        parcel.writeTypedList(this.f329j);
        parcel.writeLong(this.f330k);
        parcel.writeBundle(this.f331l);
        parcel.writeInt(this.f326f);
    }
}
