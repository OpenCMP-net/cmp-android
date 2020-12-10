package com.opencmp.inapplib;

@FunctionalInterface
public interface OpenCmpErrorHandler {
    void onOpenCmpError(Exception error);
}
