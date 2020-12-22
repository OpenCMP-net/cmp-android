package com.opencmp.inapplib;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OpenCmpConfig {

    public final String domain;
    public final String storageName;
    public final OpenCmpErrorHandler errorHandler;
    public final OnConsentChangedListener changesListener;

    private OpenCmpConfig(
        @NonNull String domain,
        @Nullable String storageName,
        @Nullable OpenCmpErrorHandler errorHandler,
        @Nullable OnConsentChangedListener changesListener
    ) {
        this.domain = domain;
        this.storageName = storageName;
        this.errorHandler = errorHandler;
        this.changesListener = changesListener;
    }

    public static class Builder {

        private final String domain;

        private String storageName = null;

        private OpenCmpErrorHandler errorHandler;

        private OnConsentChangedListener changesListener;

        public Builder(String domain) {
            this.domain = domain;
        }

        public Builder setStorageName(String name) {
            storageName = name;
            return this;
        }

        public Builder setErrorHandler(OpenCmpErrorHandler errorHandler) {
            this.errorHandler = errorHandler;
            return this;
        }

        public Builder setChangesListener(OnConsentChangedListener changesListener) {
            this.changesListener = changesListener;
            return this;
        }

        public OpenCmpConfig build() {
            return new OpenCmpConfig(domain, storageName, errorHandler, changesListener);
        }
    }
}
