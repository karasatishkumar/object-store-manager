package org.satish.cloud.storage.exception;

public class CloudStorageException extends Exception {
    public CloudStorageException() {
        super();
    }

    public CloudStorageException(String message) {
        super(message);
    }

    public CloudStorageException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudStorageException(Throwable cause) {
        super(cause);
    }

    protected CloudStorageException(String message, Throwable cause,
                                    boolean enableSuppression,
                                    boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
