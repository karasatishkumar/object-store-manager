package org.satish.cloud.storage;

public enum CloudStorageType {
    AMAZON_S3("AMAZON_S3"), MINIO("MINIO");

    private String type;

    CloudStorageType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }
}
