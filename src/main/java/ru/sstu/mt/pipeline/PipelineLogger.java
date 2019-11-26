package ru.sstu.mt.pipeline;

import java.util.EnumMap;
import java.util.function.Supplier;

import static ru.sstu.mt.pipeline.LoggingMode.*;

public class PipelineLogger {
    public static final PipelineLogger DEFAULT_LOGGER = getWithModeForAll(BOTH);

    public static PipelineLogger getLogger() {
        return new PipelineLogger(DEFAULT_LOGGER);
    }

    public static PipelineLogger getWithModeForAll(LoggingMode mode) {
        PipelineLogger logger = new PipelineLogger();
        for (PipelineEvent event : PipelineEvent.values()) {
            logger.loggableEvents.put(event, mode);
        }
        return logger;
    }

    private final EnumMap<PipelineEvent, LoggingMode> loggableEvents;

    private PipelineLogger() {
        loggableEvents = new EnumMap<>(PipelineEvent.class);

    }

    private PipelineLogger(PipelineLogger other) {
        loggableEvents = new EnumMap<>(other.loggableEvents);
    }

    public PipelineLogger setLogMode(LoggingMode mode, PipelineEvent... events) {
        for (PipelineEvent event : events) {
            loggableEvents.put(event, mode);
        }
        return this;
    }

    public void logEvent(String s, PipelineEvent event) {
        LoggingMode mode = loggableEvents.get(event);
        if (mode == BOTH || mode == EVENT) {
            System.out.println(s);
        }
    }

    public void logEvent(Supplier<String> s, PipelineEvent event) {
        LoggingMode mode = loggableEvents.get(event);
        if (mode == BOTH || mode == EVENT) {
            System.out.println(s.get());
        }
    }

    public void logResult(String s, PipelineEvent event) {
        LoggingMode mode = loggableEvents.get(event);
        if (mode == BOTH || mode == RESULT) {
            System.out.println(s);
        }
    }

    public void logResult(Supplier<String> s, PipelineEvent event) {
        LoggingMode mode = loggableEvents.get(event);
        if (mode == BOTH || mode == RESULT) {
            System.out.println(s.get());
        }
    }
}
