package org.example.sec05;

import com.google.protobuf.InvalidProtocolBufferException;
import org.example.models.sec05.v1.Television;
import org.example.sec04.ImportDemo;
import org.example.sec05.parser.V1Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class V1VersionCompatibility {

    private static final Logger log = LoggerFactory.getLogger(V1VersionCompatibility.class);

    public static void main(String[] args) throws InvalidProtocolBufferException {

        var tv = Television.newBuilder()
                .setBrand("samsung")
                .setYear(1992)
                .build();

        V1Parser.parse(tv.toByteArray());

    }


}
