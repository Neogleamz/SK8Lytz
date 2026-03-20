package com.arthenica.ffmpegkit;
/* loaded from: C:\Users\Magma\OneDrive - Neogleamz\General - Neogleamz\MobileApp\ZENGGE_extracted\blewv2_extracted\classes.dex */
public enum Level {
    AV_LOG_STDERR(-16),
    AV_LOG_QUIET(-8),
    AV_LOG_PANIC(0),
    AV_LOG_FATAL(8),
    AV_LOG_ERROR(16),
    AV_LOG_WARNING(24),
    AV_LOG_INFO(32),
    AV_LOG_VERBOSE(40),
    AV_LOG_DEBUG(48),
    AV_LOG_TRACE(56);
    

    /* renamed from: a  reason: collision with root package name */
    private final int f8767a;

    Level(int i8) {
        this.f8767a = i8;
    }

    public static Level f(int i8) {
        Level level = AV_LOG_STDERR;
        if (i8 == level.h()) {
            return level;
        }
        Level level2 = AV_LOG_QUIET;
        if (i8 == level2.h()) {
            return level2;
        }
        Level level3 = AV_LOG_PANIC;
        if (i8 == level3.h()) {
            return level3;
        }
        Level level4 = AV_LOG_FATAL;
        if (i8 == level4.h()) {
            return level4;
        }
        Level level5 = AV_LOG_ERROR;
        if (i8 == level5.h()) {
            return level5;
        }
        Level level6 = AV_LOG_WARNING;
        if (i8 == level6.h()) {
            return level6;
        }
        Level level7 = AV_LOG_INFO;
        if (i8 == level7.h()) {
            return level7;
        }
        Level level8 = AV_LOG_VERBOSE;
        if (i8 == level8.h()) {
            return level8;
        }
        Level level9 = AV_LOG_DEBUG;
        return i8 == level9.h() ? level9 : AV_LOG_TRACE;
    }

    public int h() {
        return this.f8767a;
    }
}
