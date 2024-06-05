package org.example.sec04;

import com.google.protobuf.Int32Value;
import com.google.protobuf.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;


public class WrapperDemo {

    private static final Logger log = LoggerFactory.getLogger(ImportDemo.class);

    public static void main(String[] args){

        var sample = org.example.models.sec04.Sample.newBuilder()
                .setAge(Int32Value.of(12))
                .setLoginTime(Timestamp.newBuilder().setSeconds(Instant.now().getEpochSecond()).build())
                .build();

                log.info("{}", Instant.ofEpochSecond(sample.getLoginTime().getSeconds()));

    }
}
