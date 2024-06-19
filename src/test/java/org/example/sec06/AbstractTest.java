package org.example.sec06;

import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;
import org.example.models.sec06.BankServiceGrpc;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

public class AbstractTest extends AbstractChannelTest {

    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

}
