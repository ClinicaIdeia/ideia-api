package com.ideiaapi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.Tag;
import com.amazonaws.services.s3.model.lifecycle.LifecycleFilter;
import com.amazonaws.services.s3.model.lifecycle.LifecycleTagPredicate;

@Configuration
public class S3Config {

    @Value("${ideia.s3.access-key-id}")
    private String s3AccessKey;

    @Value("${ideia.s3.secret-access-key}")
    private String s3SecretAccessKey;

    private String bucket = "oplk-it-ideia-arquivos";

    public String getS3AccessKey() {
        return s3AccessKey;
    }

    public void setS3AccessKey(String s3AccessKey) {
        this.s3AccessKey = s3AccessKey;
    }

    public String getS3SecretAccessKey() {
        return s3SecretAccessKey;
    }

    public void setS3SecretAccessKey(String s3SecretAccessKey) {
        this.s3SecretAccessKey = s3SecretAccessKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public AmazonS3 amazonS3() {

        AWSCredentials credentials = new BasicAWSCredentials(s3AccessKey, s3SecretAccessKey);

        AmazonS3 amazonS3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();

        this.criaBucketAutomaticmente(amazonS3);

        return amazonS3;

    }

    private void criaBucketAutomaticmente(AmazonS3 amazonS3) {
        if (!amazonS3.doesBucketExistV2(bucket)) {
            amazonS3.createBucket(new CreateBucketRequest(bucket));

            BucketLifecycleConfiguration.Rule regraDeExpiracao = new BucketLifecycleConfiguration.Rule()
                    .withId("Regra de expiração para arquivos temporários")
                    .withFilter(new LifecycleFilter(new LifecycleTagPredicate(new Tag("expirar", "true"))))
                    .withExpirationInDays(1)
                    .withStatus(BucketLifecycleConfiguration.ENABLED);

            BucketLifecycleConfiguration configuration = new BucketLifecycleConfiguration()
                    .withRules(regraDeExpiracao);

            amazonS3.setBucketLifecycleConfiguration(bucket, configuration);
        }
    }

}
