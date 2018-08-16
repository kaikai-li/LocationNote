package com.lkk.locationnote.common.log;

public interface ILog {
    void init();
    void v(String tag, String msg);
    void d(String tag, String msg);
    void i(String tag, String msg);
    void w(String tag, String msg);
    void e(String tag, String msg);
}
