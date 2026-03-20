package com.example.seedpoint;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import com.google.gson.e;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public class AppLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    private static final int DELAY = 5000;
    private static final String LAST_WORDS = "last_words";
    private static final String SP_NAME = "app_end";
    private static final String TAG = AppLifecycleCallback.class.getName();
    private static final e gson = new e();
    private long appStartTime;
    private final AppStartEndListener listener;
    private ScheduledFuture<?> scheduledFuture;
    private final SharedPreferences sp;
    private long startTime;
    private final AtomicInteger foregroundCount = new AtomicInteger(0);
    private boolean appStart = false;
    private boolean firstCreate = false;
    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    /* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
    private static class LastWord {
        private long actionTime;
        private long eventTime;
        private String words;

        public LastWord(long j8, long j9, String str) {
            this.eventTime = j8;
            this.actionTime = j9;
            this.words = str;
        }

        public static LastWord toLastWords(String str) {
            if (str == null) {
                return null;
            }
            try {
                return (LastWord) AppLifecycleCallback.gson.l(str, LastWord.class);
            } catch (Throwable th) {
                Log.e(AppLifecycleCallback.TAG, th.getMessage());
                return null;
            }
        }

        public long getActionTime() {
            return this.actionTime;
        }

        public long getEventTime() {
            return this.eventTime;
        }

        public String getWords() {
            return this.words;
        }

        public void setActionTime(long j8) {
            this.actionTime = j8;
        }

        public void setEventTime(long j8) {
            this.eventTime = j8;
        }

        public void setWords(String str) {
            this.words = str;
        }

        public String toValue() {
            return AppLifecycleCallback.gson.u(this);
        }
    }

    public AppLifecycleCallback(Context context, AppStartEndListener appStartEndListener) {
        this.sp = context.getSharedPreferences(SP_NAME, 0);
        this.listener = appStartEndListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$onActivityStopped$0(long j8, long j9, String str) {
        synchronized (this) {
            if (this.scheduledFuture != null) {
                String str2 = TAG;
                Log.i(str2, "appEnd : " + j8);
                this.scheduledFuture = null;
                this.appStart = false;
                this.sp.edit().remove(LAST_WORDS).apply();
                this.listener.appEnd(j9, j8, str);
            }
        }
    }

    private String toLastWords(long j8, long j9) {
        return j8 + "," + j9;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.i(TAG, "onActivityCreated");
        if (this.appStart) {
            return;
        }
        this.startTime = System.currentTimeMillis();
        this.firstCreate = true;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        synchronized (this) {
            if (this.foregroundCount.incrementAndGet() >= 1) {
                if (this.scheduledFuture != null) {
                    String str = TAG;
                    Log.i(str, " background scheduled cancel. ");
                    this.scheduledFuture.cancel(true);
                    this.scheduledFuture = null;
                    this.sp.edit().remove(LAST_WORDS).apply();
                    Log.i(str, "appReenter");
                } else if (!this.appStart) {
                    String str2 = TAG;
                    Log.i(str2, "appEnter");
                    this.appStart = true;
                    this.appStartTime = System.currentTimeMillis();
                    LastWord lastWords = LastWord.toLastWords(this.sp.getString(LAST_WORDS, null));
                    if (lastWords != null) {
                        Log.i(str2, "push last words. " + lastWords);
                        this.sp.edit().remove(LAST_WORDS).apply();
                        this.listener.appEnd(lastWords.eventTime, lastWords.actionTime, lastWords.words);
                    }
                    if (this.firstCreate) {
                        Log.i(str2, "first create ");
                        this.listener.appStart(this.firstCreate, System.currentTimeMillis() - this.startTime);
                        this.firstCreate = false;
                    } else {
                        Log.i(str2, "not first create ");
                        this.listener.appStart(this.firstCreate, System.currentTimeMillis() - this.appStartTime);
                    }
                }
            }
        }
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        synchronized (this) {
            if (this.foregroundCount.decrementAndGet() <= 0) {
                Log.i(TAG, " background scheduled start. ");
                final long currentTimeMillis = System.currentTimeMillis();
                final long j8 = currentTimeMillis - this.appStartTime;
                final String lastWords = this.listener.lastWords();
                this.sp.edit().putString(LAST_WORDS, new LastWord(currentTimeMillis, j8, lastWords).toValue()).apply();
                this.scheduledFuture = this.executorService.schedule(new Runnable() { // from class: com.example.seedpoint.a
                    @Override // java.lang.Runnable
                    public final void run() {
                        AppLifecycleCallback.this.lambda$onActivityStopped$0(j8, currentTimeMillis, lastWords);
                    }
                }, 5000L, TimeUnit.MILLISECONDS);
            }
        }
    }
}
