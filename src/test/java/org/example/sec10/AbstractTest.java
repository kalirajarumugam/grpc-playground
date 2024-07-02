package org.example.sec10;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.protobuf.ProtoUtils;
import org.example.common.AbstractChannelTest;
import org.example.common.GrpcServer;

import org.example.models.sec10.BankServiceGrpc;
import org.example.models.sec10.ErrorMessage;
import org.example.models.sec10.ValidationCode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.Optional;

public class AbstractTest extends AbstractChannelTest {

    public static final Metadata.Key<ErrorMessage> ERROR_MESSAGE_KEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());
    private final GrpcServer grpcServer = GrpcServer.create(new BankService());
    protected BankServiceGrpc.BankServiceBlockingStub blockingStub;

    protected BankServiceGrpc.BankServiceStub stub;

    @BeforeAll
    public void setup(){
        this.grpcServer.start();
        blockingStub = BankServiceGrpc.newBlockingStub(channel);
        stub = BankServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop(){
        this.grpcServer.stop();
    }

    protected ValidationCode getValidationCode(Throwable throwable){
        return Optional.ofNullable(Status.trailersFromThrowable(throwable))
                .map(m->m.get(ERROR_MESSAGE_KEY))
                .map(ErrorMessage::getValidationCode)
                .orElseThrow();
    }

}
