package org.example.common;

import ch.qos.logback.core.sift.AppenderFactoryUsingSiftModel;
import io.grpc.ServerBuilder;
import org.example.sec06.BankService;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) throws Exception {

        var server = ServerBuilder.forPort(6565)
                .addService(new BankService())
                .build();

        server.start();

        server.awaitTermination();

    }
}
