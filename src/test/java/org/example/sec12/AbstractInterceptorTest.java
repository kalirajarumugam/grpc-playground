package org.example.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.common.GrpcServer;
import org.example.models.sec12.BankServiceGrpc;
import org.example.sec12.interceptors.ApiKeyValidationInterceptor;
import org.example.sec12.interceptors.GZipRequestInterceptor;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractInterceptorTest {

    protected ManagedChannel channel;

    private  GrpcServer grpcServer;
    protected BankServiceGrpc.BankServiceBlockingStub bankBlockingStub;
    protected BankServiceGrpc.BankServiceStub bankStub;

    protected abstract List<ClientInterceptor> getClientInterceptors();

    @BeforeAll
    public void setup(){
        this.grpcServer = createServer();
        this.grpcServer.start();
        this.channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .intercept(getClientInterceptors())
                .build();
        this.bankBlockingStub = BankServiceGrpc.newBlockingStub(channel);
        this.bankStub = BankServiceGrpc.newStub(channel);
    }

    @AfterAll
    public void stop() throws InterruptedException {
        this.channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        this.grpcServer.stop();
    }

    protected GrpcServer createServer(){

        return GrpcServer.create(6565, builder -> {builder.addService(new BankService()).intercept(new ApiKeyValidationInterceptor());});

    }

}
