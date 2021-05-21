package org.satish.cloud.storage;


import org.satish.cloud.storage.exception.CloudStorageException;

import java.io.InputStream;
import java.util.List;

public interface CloudStorage {
    public void createBucket(String bucketName) throws CloudStorageException;

    public void removeBucket(String bucketName) throws CloudStorageException;

    public List<String> getBuckets() throws CloudStorageException;

    public void createObject(String bucketName, String key, String content) throws CloudStorageException;

    public List<String> getObjects(String bucketName) throws CloudStorageException;

    public String getObject(String bucketName, String key) throws CloudStorageException;

    public InputStream getObjectStream(String bucketName, String key) throws CloudStorageException;

    public void removeObject(String bucketName, String key) throws CloudStorageException;
}
