package androidx.core.view;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public final class e {

    /* renamed from: a  reason: collision with root package name */
    private final a f4984a;

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    interface a {
        boolean a(MotionEvent motionEvent);
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class b implements a {

        /* renamed from: v  reason: collision with root package name */
        private static final int f4985v = ViewConfiguration.getTapTimeout();

        /* renamed from: w  reason: collision with root package name */
        private static final int f4986w = ViewConfiguration.getDoubleTapTimeout();

        /* renamed from: a  reason: collision with root package name */
        private int f4987a;

        /* renamed from: b  reason: collision with root package name */
        private int f4988b;

        /* renamed from: c  reason: collision with root package name */
        private int f4989c;

        /* renamed from: d  reason: collision with root package name */
        private int f4990d;

        /* renamed from: e  reason: collision with root package name */
        private final Handler f4991e;

        /* renamed from: f  reason: collision with root package name */
        final GestureDetector.OnGestureListener f4992f;

        /* renamed from: g  reason: collision with root package name */
        GestureDetector.OnDoubleTapListener f4993g;

        /* renamed from: h  reason: collision with root package name */
        boolean f4994h;

        /* renamed from: i  reason: collision with root package name */
        boolean f4995i;

        /* renamed from: j  reason: collision with root package name */
        private boolean f4996j;

        /* renamed from: k  reason: collision with root package name */
        private boolean f4997k;

        /* renamed from: l  reason: collision with root package name */
        private boolean f4998l;

        /* renamed from: m  reason: collision with root package name */
        MotionEvent f4999m;

        /* renamed from: n  reason: collision with root package name */
        private MotionEvent f5000n;

        /* renamed from: o  reason: collision with root package name */
        private boolean f5001o;

        /* renamed from: p  reason: collision with root package name */
        private float f5002p;
        private float q;

        /* renamed from: r  reason: collision with root package name */
        private float f5003r;

        /* renamed from: s  reason: collision with root package name */
        private float f5004s;

        /* renamed from: t  reason: collision with root package name */
        private boolean f5005t;

        /* renamed from: u  reason: collision with root package name */
        private VelocityTracker f5006u;

        /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
        private class a extends Handler {
            a() {
            }

            a(Handler handler) {
                super(handler.getLooper());
            }

            @Override // android.os.Handler
            public void handleMessage(Message message) {
                int i8 = message.what;
                if (i8 == 1) {
                    b bVar = b.this;
                    bVar.f4992f.onShowPress(bVar.f4999m);
                } else if (i8 == 2) {
                    b.this.d();
                } else if (i8 != 3) {
                    throw new RuntimeException("Unknown message " + message);
                } else {
                    b bVar2 = b.this;
                    GestureDetector.OnDoubleTapListener onDoubleTapListener = bVar2.f4993g;
                    if (onDoubleTapListener != null) {
                        if (bVar2.f4994h) {
                            bVar2.f4995i = true;
                        } else {
                            onDoubleTapListener.onSingleTapConfirmed(bVar2.f4999m);
                        }
                    }
                }
            }
        }

        b(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            if (handler != null) {
                this.f4991e = new a(handler);
            } else {
                this.f4991e = new a();
            }
            this.f4992f = onGestureListener;
            if (onGestureListener instanceof GestureDetector.OnDoubleTapListener) {
                g((GestureDetector.OnDoubleTapListener) onGestureListener);
            }
            e(context);
        }

        private void b() {
            this.f4991e.removeMessages(1);
            this.f4991e.removeMessages(2);
            this.f4991e.removeMessages(3);
            this.f5006u.recycle();
            this.f5006u = null;
            this.f5001o = false;
            this.f4994h = false;
            this.f4997k = false;
            this.f4998l = false;
            this.f4995i = false;
            if (this.f4996j) {
                this.f4996j = false;
            }
        }

        private void c() {
            this.f4991e.removeMessages(1);
            this.f4991e.removeMessages(2);
            this.f4991e.removeMessages(3);
            this.f5001o = false;
            this.f4997k = false;
            this.f4998l = false;
            this.f4995i = false;
            if (this.f4996j) {
                this.f4996j = false;
            }
        }

        private void e(Context context) {
            if (context == null) {
                throw new IllegalArgumentException("Context must not be null");
            }
            if (this.f4992f == null) {
                throw new IllegalArgumentException("OnGestureListener must not be null");
            }
            this.f5005t = true;
            ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
            int scaledTouchSlop = viewConfiguration.getScaledTouchSlop();
            int scaledDoubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
            this.f4989c = viewConfiguration.getScaledMinimumFlingVelocity();
            this.f4990d = viewConfiguration.getScaledMaximumFlingVelocity();
            this.f4987a = scaledTouchSlop * scaledTouchSlop;
            this.f4988b = scaledDoubleTapSlop * scaledDoubleTapSlop;
        }

        private boolean f(MotionEvent motionEvent, MotionEvent motionEvent2, MotionEvent motionEvent3) {
            if (this.f4998l && motionEvent3.getEventTime() - motionEvent2.getEventTime() <= f4986w) {
                int x8 = ((int) motionEvent.getX()) - ((int) motionEvent3.getX());
                int y8 = ((int) motionEvent.getY()) - ((int) motionEvent3.getY());
                return (x8 * x8) + (y8 * y8) < this.f4988b;
            }
            return false;
        }

        /* JADX WARN: Removed duplicated region for block: B:107:0x0204  */
        /* JADX WARN: Removed duplicated region for block: B:110:0x021b  */
        @Override // androidx.core.view.e.a
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean a(android.view.MotionEvent r13) {
            /*
                Method dump skipped, instructions count: 589
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.core.view.e.b.a(android.view.MotionEvent):boolean");
        }

        void d() {
            this.f4991e.removeMessages(3);
            this.f4995i = false;
            this.f4996j = true;
            this.f4992f.onLongPress(this.f4999m);
        }

        public void g(GestureDetector.OnDoubleTapListener onDoubleTapListener) {
            this.f4993g = onDoubleTapListener;
        }
    }

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    static class c implements a {

        /* renamed from: a  reason: collision with root package name */
        private final GestureDetector f5008a;

        c(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
            this.f5008a = new GestureDetector(context, onGestureListener, handler);
        }

        @Override // androidx.core.view.e.a
        public boolean a(MotionEvent motionEvent) {
            return this.f5008a.onTouchEvent(motionEvent);
        }
    }

    public e(Context context, GestureDetector.OnGestureListener onGestureListener) {
        this(context, onGestureListener, null);
    }

    public e(Context context, GestureDetector.OnGestureListener onGestureListener, Handler handler) {
        this.f4984a = Build.VERSION.SDK_INT > 17 ? new c(context, onGestureListener, handler) : new b(context, onGestureListener, handler);
    }

    public boolean a(MotionEvent motionEvent) {
        return this.f4984a.a(motionEvent);
    }
}
