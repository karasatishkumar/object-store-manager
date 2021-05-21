package org.satish.cloud.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class CloudStorageFactory {

    private Logger logger = LoggerFactory.getLogger(CloudStorageFactory.class);

    @Autowired
    @Qualifier("AmazonS3Storage")
    private CloudStorage amazonS3Storage;

    @Autowired
    @Qualifier("MinIOStorage")
    private CloudStorage minIOStorage;

    @Value("${cloud.storage.type}")
    private CloudStorageType cloudStorageType;

    @PostConstruct
    public void init(){
        logger.info("************ Configured Cloud Storage ************");
        logger.info("************ {} ************", this.cloudStorageType.getType());
        logger.info("************ ************************* ************");
    }

    public CloudStorage getCloudStorage() {
        CloudStorage cloudStorage = null;
        switch (cloudStorageType) {
            case AMAZON_S3:
                logger.info("Enabled cloud storage : {}", cloudStorageType.AMAZON_S3.getType());
                cloudStorage = amazonS3Storage;
                break;
            case MINIO:
                logger.info("Enabled cloud storage : {}", cloudStorageType.MINIO.getType());
                cloudStorage = minIOStorage;
                break;
            default:
                break;
        }
        return cloudStorage;
    }

}
