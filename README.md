# Object Store Manager
This project created for giving a unified way to access different cloud object stores. Based on the configuration one can toggle between different cloud object stores. 

## MINIO
This also contain configuration which can be used for [MINIO]( https://min.io/). MinIO can be configured locally and can be used for development purpose to reduce cost. MINIO provides APIs similar to S3 and S3 Client code can be used to interact MINIO. This also can be used in production with adequate configuration.Please refer to the licencing part for production use. Please also read through the documentation for a production type of setup.

## MINIO Setup
[MINIO setup document](https://docs.min.io/docs/minio-quickstart-guide.html) can be followed for setting it up in various environment. But when I am taking here about development purpose, I use the docker way of running it in my local. Please note it's volatile, when you run like this. Stopping the container will erase out all the data.

    docker run -p 9001:9000 minio/minio server /data

You may use the default credentials 'minioadmin:minioadmin, to access the object store both from application and [web console](http://127.0.0.1:9001/minio).

## Toggling Setup

To run the setup to use s3, please user the following properties in application. properties file.

    cloud.storage.type=AMAZON_S3
    aws.s3.access.key.id=*******
    aws.s3.secret.access.key=*******
    aws.s3.region=US_WEST_2

To run the setup to use MINIO, use the following properties.
    
    cloud.storage.type = MINIO
    aws.s3.access.key.id=minioadmin
    aws.s3.secret.access.key=minioadmin
    aws.s3.region=US_WEST_2

## Limitations
For the time being in this repository, I have not included any other implementations related to other cloud object provider. The essence  of this repository is to show how s3 client can be used for MINIO server.

## APIs
Following APIs have been created for demonstration of the concept only.

### Create Bucket
    curl --location --request POST 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/'

### Delete Bucket
    curl --location --request DELETE 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/'

### Get Buckets
    curl --location --request GET 'http://localhost:8080/buckets/'

### Create Object
    curl --location --request POST 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/test.json' \
    --header 'Content-Type: application/json' \
    --data-raw '{"test":"test"}'

### Delete Objects
    curl --location --request DELETE 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/test.json'

### Get Objects
    curl --location --request GET 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/'

### Get Object
    curl --location --request GET 'http://localhost:8080/buckets/4ba00bd9-84dc-477b-bab8-9a8e8005c333/test.json'

## Licensing

## Contributions
    

