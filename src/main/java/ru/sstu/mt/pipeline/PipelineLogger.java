package ru.sstu.mt.pipeline;

import java.util.EnumMap;
import java.util.function.Supplier;

import static ru.sstu.mt.pipeline.LoggingMode.*;

public class PipelineLogger {
    private final EnumMap<PipelineEvent, LoggingMode> loggableEvents;

    public PipelineLogger() {
        loggableEvents = new EnumMap<>(PipelineEvent.class);
        for (PipelineEvent event : PipelineEvent.values()) {
            loggableEvents.put(event, BOTH);
        }
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
