package ru.practicum.config;

import java.net.URI;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.conf.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class HdfsConfig {
    @Value("${spring.hadoop.hdfs.uri}")
    private String hdfsUri;

    @Bean
    public FileSystem fileSystem() throws IOException {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS", hdfsUri);
        return FileSystem.get(URI.create(hdfsUri), conf);
    }
}