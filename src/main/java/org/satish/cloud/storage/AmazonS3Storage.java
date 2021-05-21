package org.satish.cloud.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import org.satish.cloud.storage.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation that is created to
 * work with Amazon Simple Storage(S3) as Cloud Storage.
 *
 * Application Parameter : cloud.storage.type=AMAZON_S3
 */
@Service
@Qualifier("AmazonS3Storage")
public class AmazonS3Storage implements CloudStorage {

    private Logger logger = LoggerFactory.getLogger(AmazonS3Storage.class);

    @Value("${aws.s3.access.key.id}")
    protected String accessKeyId;

    @Value("${aws.s3.secret.access.key}")
    protected String accessKey;

    @Value("${aws.s3.region}")
    protected String region;

    protected AmazonS3 amazonS3;

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKey);
        logger.info("Selected S3 Region {}", this.region);
        amazonS3 = AmazonS3ClientBuilder.standard().withRegion(Regions.valueOf(region))
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }


    @Override
    public void createBucket(String bucketName) throws CloudStorageException {
        if (amazonS3.doesBucketExistV2(bucketName)) {
            logger.info("Bucket {} already exists", bucketName);
        } else {
            try {
                amazonS3.createBucket(bucketName);
                logger.info("Bucket {} created successfully", bucketName);
            } catch (AmazonS3Exception e) {
                throw new CloudStorageException(e);
            }
        }
    }

    @Override
    public void removeBucket(String bucketName) throws CloudStorageException {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            logger.info("Bucket {} does not exists.", bucketName);
        } else {
            List<String> objects = this.getObjects(bucketName);
            if (objects != null && objects.size() > 0) {
                logger.info("Deleting all the objects from bucket {}", bucketName);
                for (String object : objects) {
                    amazonS3.deleteObject(bucketName, object);
                }
            }
            try {
                amazonS3.deleteBucket(bucketName);
                logger.info("Bucket {} deleted successfully", bucketName);
            } catch (AmazonS3Exception e) {
                throw new CloudStorageException(e);
            }
        }
    }

    @Override
    public List<String> getBuckets() throws CloudStorageException {
        List<String> bucketNames = new ArrayList<>();
        List<Bucket> buckets = amazonS3.listBuckets();
        for (Bucket b : buckets) {
            bucketNames.add(b.getName());
        }
        return bucketNames;
    }

    @Override
    public void createObject(String bucketName, String key, String content) throws CloudStorageException {
        File temp = null;
        try {
            temp = creatTempFile(content);
            amazonS3.putObject(new PutObjectRequest(bucketName, key, temp));
            logger.info("Object {} successfully uploaded to S3 bucket {}", key, bucketName);
        } catch (Exception e) {
            logger.error("Exception while creating the object in S3 ", e);
            throw new CloudStorageException(e);
        }finally{
            if(temp != null && temp.exists()){
                temp.delete();
            }
        }
    }

    @Override
    public List<String> getObjects(String bucketName) throws CloudStorageException {
        List<String> objects = new ArrayList<>();
        try {
            ListObjectsV2Result result = amazonS3.listObjectsV2(bucketName);
            List<S3ObjectSummary> objectSummaries = result.getObjectSummaries();
            for (S3ObjectSummary os : objectSummaries) {
                objects.add(os.getKey());
            }
        } catch (Exception e) {
            logger.error("Exception while getting the objects from S3", e);
            throw new CloudStorageException(e);
        }
        return objects;
    }

    @Override
    public String getObject(String bucketName, String key) throws CloudStorageException {
        try {
            logger.info("Object {} getting downloaded from S3 bucket {}", key, bucketName);
            return getContent(amazonS3.getObject(new GetObjectRequest(bucketName, key)));
        } catch (Exception e) {
            logger.error("Exception while getting the object from S3 ", e);
            throw new CloudStorageException(e);
        }
    }

    @Override
    public InputStream getObjectStream(String bucketName, String key) throws CloudStorageException {
        try {
            logger.info("Object {} getting downloaded from S3 bucket {}", key, bucketName);
            return getInputStream(amazonS3.getObject(new GetObjectRequest(bucketName, key)));
        } catch (Exception e) {
            logger.error("Exception while getting the object from S3 ", e);
            throw new CloudStorageException(e);
        }
    }

    @Override
    public void removeObject(String bucketName, String key) throws CloudStorageException {
        try {
            logger.info("Object {} getting deleted from S3 bucket ", key, bucketName);
            amazonS3.deleteObject(bucketName, key);
        } catch (Exception e) {
            logger.error("Exception while removing the object from S3 ", e);
            throw new CloudStorageException(e);
        }
    }

    private File creatTempFile(String content) throws IOException {
        File temp = null;

        // Create temp file.
        temp = File.createTempFile("payload", ".xml");

        // Delete temp file when program exits.
        temp.deleteOnExit();

        // Write to temp file
        BufferedWriter out = new BufferedWriter(new FileWriter(temp));
        out.write(content);
        out.close();

        return temp;
    }

    private String getContent(S3Object s3Object) throws IOException {
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();
        byte[] content = null;

        content = IOUtils.toByteArray(s3ObjectInputStream);
        s3Object.close();

        return new String(content);
    }

    private InputStream getInputStream(S3Object s3Object) throws IOException {
        return s3Object.getObjectContent();
    }
}
