package org.superbiz.moviefun;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import java.io.IOException;
import java.util.Optional;

public class S3Store implements BlobStore {
    private final AmazonS3Client s3Client;
    private final String s3BucketName;

    public S3Store(AmazonS3Client s3Client, String s3BucketName) {
        this.s3Client = s3Client;
        this.s3BucketName = s3BucketName;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(blob.contentType);
        PutObjectRequest request = new PutObjectRequest(s3BucketName, blob.name, blob.inputStream, metadata);
        s3Client.putObject(request);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        if( s3Client.doesObjectExist(s3BucketName, name) ) {
            S3Object object = s3Client.getObject(s3BucketName, name);
            Blob blob = new Blob(name, object.getObjectContent(), object.getObjectMetadata().getContentType());
            return Optional.of(blob);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
