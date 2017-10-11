package com.terraware.server.nio;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * @author Daniel Terranova <mailto:daniel.terranova@so4it.com>
 * @since 0.8.0
 */
public class SimpleFormatter extends Formatter {
    static final int DATE_TIME = 0;
    static final int CLASS_NAME = 1;
    static final int METHOD_NAME = 2;
    static final int LEVEL = 3;
    static final int LOGGER_NAME = 4;
    static final int MESSAGE = 5;
    static final int CONTEXT = 6;
    static final int THREAD_NAME = 7;
    static final int THREAD_ID = 8;
    static final int LRMI_INVOCATION_SHORT_CONTEXT = 9;
    static final int LRMI_INVOCATION_LONG_CONTEXT = 10;
    static final int lastIndex = 11;
    private static final String defaultPattern = "{0,date,yyyy-MM-dd HH:mm:ss,SSS} {7} {3} [{4}] - {5}";
    private final MessageFormat messageFormat;
    private final boolean[] patternIds;
    private static final String lineSeparator = System.getProperty("line.separator");
    private final Object[] _args;
    private final Date _date;

    public SimpleFormatter() {
        this(getDefinedPattern());
    }

    public SimpleFormatter(String pattern) {
        this.patternIds = new boolean[11];
        this._args = new Object[11];
        this._date = new Date();
        this.messageFormat = new MessageFormat(pattern);

        for(int i = 0; i < 11; ++i) {
            if (pattern.contains(String.valueOf("{" + i + "}")) || pattern.contains(String.valueOf("{" + i + ","))) {
                this.patternIds[i] = true;
            }
        }

    }

    private static String getDefinedPattern() {
        LogManager manager = LogManager.getLogManager();
        String pattern = manager.getProperty(SimpleFormatter.class.getName() + ".format");
        return pattern == null ? "[{0,date,yyyy-MM-dd HH:mm:ss,SSS}] [{7}] {3} [{4}] - {5}" : pattern;
    }

    public synchronized String format(LogRecord record) {
        StringBuffer text = new StringBuffer();
        this.setArgsWithRecordData(record);
        this.messageFormat.format(this._args, text, (FieldPosition)null);
        Throwable thrown = record.getThrown();
        if (thrown != null) {
            Logger exceptionLogger = Logger.getLogger("com.gigaspaces.exceptions");
            if (!exceptionLogger.isLoggable(record.getLevel()) && !(thrown instanceof RuntimeException)) {
                text.append("; Caused by: ").append(record.getThrown().toString());
            } else {
                try {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    thrown.printStackTrace(pw);
                    pw.close();
                    text.append("; Caused by: ").append(sw.toString());
                } catch (Exception var7) {
                    text.append("; Caused by: ").append(record.getThrown().toString());
                    text.append(" - Unable to parse stack trace; Caught: ").append(var7);
                }
            }
        }

        text.append(lineSeparator);
        return text.toString();
    }

    private void setArgsWithRecordData(LogRecord record) {
        if (this.patternIds[0]) {
            this._date.setTime(record.getMillis());
            this._args[0] = this._date;
        }

        if (this.patternIds[1]) {
            this._args[1] = record.getSourceClassName();
            if (this._args[1] == null) {
                this._args[1] = "";
            }
        }

        if (this.patternIds[2]) {
            this._args[2] = record.getSourceMethodName();
            if (this._args[2] == null) {
                this._args[2] = "";
            }
        }

        if (this.patternIds[3]) {
            this._args[3] = record.getLevel().getName();
        }

        if (this.patternIds[4]) {
            this._args[4] = record.getLoggerName();
        }

        if (this.patternIds[5]) {
            this._args[5] = this.formatMessage(record);
        }

        if (this.patternIds[6]) {
            this._args[6] = "Context";
        }

        if (this.patternIds[7]) {
            this._args[7] = Thread.currentThread().getName();
        }

        if (this.patternIds[8]) {
            this._args[8] = record.getThreadID();
        }


    }
//
//    private String findContext() {
//        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
//        if (classLoader == null) {
//            return "null";
////            } else if (classLoader instanceof LoggableClassLoader) {
////                return ((LoggableClassLoader)classLoader).getLogName();
////            } else {
////                classLoader = classLoader.getParent();
////                return classLoader instanceof LoggableClassLoader ? ((LoggableClassLoader)classLoader).getLogName() : "";
////            }
//        }
//    }

}
