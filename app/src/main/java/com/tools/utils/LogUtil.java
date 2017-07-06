package com.tools.utils;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * log的封装类
 */
public class LogUtil {
    private static final String BASE_TAG = "CommonTools ==>>";
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
    private static final int JSON_INDENT = 4;

    private static final int V = 0x1;
    private static final int D = 0x2;
    private static final int I = 0x3;
    private static final int W = 0x4;
    private static final int E = 0x5;
    private static final int WTF = 0x6;
    private static final int JSON = 0x7;
    private static final int OUT = 0x8;
    private static final int ERR = 0x9;

    private static boolean DEBUG_ENABLE = true;// 是否调试模式

    /**
     * @param tag
     * @param log
     */
    public static void d(String tag, String log) {
        printLog(D, tag, log);
    }

    public static void d(String msg) {
        printLog(D, null, msg);
    }

    /**
     * @param tag
     * @param log
     */
    public static void e(String tag, String log) {
        printLog(E, tag, log);
    }

    public static void e(String msg) {
        printLog(E, null, msg);
    }

    /**
     * @param tag
     * @param log
     */
    public static void i(String tag, String log) {
        printLog(I, tag, log);
    }

    public static void i(String msg) {
        printLog(I, null, msg);
    }

    /**
     * @param tag
     * @param log
     */
    public static void v(String tag, String log) {
        printLog(V, tag, log);
    }

    public static void v(String msg) {
        printLog(V, null, msg);
    }

    /**
     * @param tag
     * @param log
     */
    public static void w(String tag, String log) {
        printLog(W, tag, log);
    }

    public static void w(String msg) {
        printLog(W, null, msg);
    }

    /**
     * @param log
     */
    public static void out(String log) {
        printLog(OUT, null, log);
    }

    /**
     * @param log
     */
    public static void err(String log) {
        printLog(ERR, null, log);
    }

    /**
     * @param msg
     */
    public static void wtf(String msg) {
        printLog(WTF, null, msg);
    }

    /**
     * @param tag
     * @param msg
     */
    public static void wtf(String tag, String msg) {
        printLog(WTF, tag, msg);
    }

    /**
     * @param jsonFormat
     */
    public static void json(String jsonFormat) {
        printLog(JSON, null, jsonFormat);
    }

    /**
     * @param tag
     * @param jsonFormat
     */
    public static void json(String tag, String jsonFormat) {
        printLog(JSON, tag, jsonFormat);
    }

    /**
     * @param type
     * @param tagStr
     * @param msg
     */
    private static void printLog(int type, String tagStr, String msg) {
        if (!DEBUG_ENABLE) {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = BASE_TAG + (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[(").append(className).append(":").append(lineNumber).append(")#").append(methodName).append("] ");//className 和 lineNumber 必须要用 : 拼接，不然没法实现点击跳转

        if (msg != null && type != JSON) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case V:
                Log.v(tag, logStr);
                break;

            case D:
                Log.d(tag, logStr);
                break;

            case I:
                Log.i(tag, logStr);
                break;

            case W:
                Log.w(tag, logStr);
                break;

            case E:
                Log.e(tag, logStr);
                break;

            case WTF:
                Log.wtf(tag, logStr);
                break;

            case OUT:
                System.out.println(tag + " " + logStr);
                break;

            case ERR:
                System.err.println(tag + " " + logStr);
                break;

            case JSON: {
                if (TextUtils.isEmpty(msg)) {
                    Log.d(tag, "Empty or Null json content");
                    return;
                }

                String message = null;

                try {
                    if (msg.startsWith("{")) {
                        JSONObject jsonObject = new JSONObject(msg);
                        message = jsonObject.toString(JSON_INDENT);
                    } else if (msg.startsWith("[")) {
                        JSONArray jsonArray = new JSONArray(msg);
                        message = jsonArray.toString(JSON_INDENT);
                    }
                } catch (JSONException e) {
                    e(tag, e.getCause().getMessage() + "\n" + msg);
                    return;
                }

                printLine(tag, true);
                message = logStr + LINE_SEPARATOR + message;
                String[] lines = message.split(LINE_SEPARATOR);
                StringBuilder jsonContent = new StringBuilder();
                for (String line : lines) {
                    jsonContent.append("║ ").append(line).append(LINE_SEPARATOR);
                }
                Log.d(tag, jsonContent.toString());
                printLine(tag, false);
            }
            break;
        }
    }

    /**
     * @param tag
     * @param isTop
     */
    private static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔════════════════════════════════ start ═════════════════════════════════");
        } else {
            Log.d(tag, "╚════════════════════════════════  end  ═════════════════════════════════");
        }
    }
}
