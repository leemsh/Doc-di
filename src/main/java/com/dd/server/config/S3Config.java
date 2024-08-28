package com.dd.server.config;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3Config {

    @Value("${AWS.SECRET_KEY}")
    private String secretKey;

    @Value("${AWS.ACCESS_KEY}")
    private String accessKey;

    @Value("${AWS.REGION}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client() {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return  AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .enablePathStyleAccess()
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }
}
