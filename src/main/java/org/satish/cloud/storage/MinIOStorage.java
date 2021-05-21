package org.satish.cloud.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.satish.cloud.storage.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * MINIO is a opensource object storage and it's apis are compatible with
 * Amazon S3 APIs. This implementation has been created, so that it can be
 * used for development purpose. This would help in overall cost cutting.
 *
 * Application Parameter : cloud.storage.type=MINIO
 *
 * To start a MINIO docker container follow the below
 * command
 * <pre>
 *     <code>
 *         docker run -p 9001:9000 minio/minio server /data
 *     </code>
 * </pre>
 *
 * This code is configured to connect MINIO running locally at
 * 9001 port.
 */
@Service
@Qualifier("MinIOStorage")
public class MinIOStorage extends AmazonS3Storage {
    private Logger logger = LoggerFactory.getLogger(MinIOStorage.class);

    @PostConstruct
    public void init() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, accessKey);
        logger.info("Selected MINIO Region {}", this.region);
        amazonS3 = AmazonS3ClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder
                        .EndpointConfiguration("http://localhost:9001", region))
                .withPathStyleAccessEnabled(true).withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }
}
