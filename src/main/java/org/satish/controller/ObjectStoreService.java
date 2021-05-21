package org.satish.controller;

import org.satish.cloud.storage.CloudStorage;
import org.satish.cloud.storage.CloudStorageFactory;
import org.satish.cloud.storage.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping(path = "/buckets")
public class ObjectStoreService {
    private static Logger logger = LoggerFactory.getLogger(ObjectStoreService.class);

    private CloudStorage cloudStorage;

    @Autowired
    private CloudStorageFactory cloudStorageFactory;

    @PostConstruct
    public void init() {
        cloudStorage = cloudStorageFactory.getCloudStorage();
    }

    @GetMapping(produces = "application/json")
    public List<String> getBuckets() throws CloudStorageException {
        logger.info("Getting the list of buckets");
        return cloudStorage.getBuckets();
    }

    @PostMapping(value = "/{bucketName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createBucket(@PathVariable("bucketName") String bucketName) throws CloudStorageException {
        logger.info("Creating bucket with name {}", bucketName);
        cloudStorage.createBucket(bucketName);
    }

    @DeleteMapping(value = "/{bucketName}")
    @ResponseStatus(HttpStatus.OK)
    public void removeBucket(@PathVariable("bucketName") String bucketName) throws CloudStorageException {
        logger.info("Creating bucket with name {}", bucketName);
        cloudStorage.removeBucket(bucketName);
    }

    @PostMapping(value = "/{bucketName}/{objectName}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createObject(@PathVariable("bucketName") String bucketName,
                             @PathVariable("objectName") String objectName, @RequestBody String content) throws CloudStorageException {
        logger.info("Creating object {} in bucket name {}", objectName, bucketName);
        logger.info("Object : {}", content);
        cloudStorage.createObject(bucketName, objectName, content);
    }

    @GetMapping(value = "/{bucketName}/{objectName}", produces = "application/json")
    public String getObject(@PathVariable("bucketName") String bucketName,
                            @PathVariable("objectName") String objectName) throws CloudStorageException {
        logger.info("getting object {} from  bucket name {}", objectName, bucketName);
        return cloudStorage.getObject(bucketName, objectName);
    }

    @GetMapping(value = "/{bucketName}", produces = "application/json")
    public List<String> getObjects(@PathVariable("bucketName") String bucketName) throws CloudStorageException {
        logger.info("getting objects of bucket name {}", bucketName);
        return cloudStorage.getObjects(bucketName);
    }

    @DeleteMapping(value = "/{bucketName}/{objectName}")
    @ResponseStatus(HttpStatus.OK)
    public void removeObject(@PathVariable("bucketName") String bucketName,
                             @PathVariable("objectName") String objectName) throws CloudStorageException {
        logger.info("Removing object {} from bucket name {}", objectName, bucketName);
        cloudStorage.removeObject(bucketName, objectName);
    }
}
