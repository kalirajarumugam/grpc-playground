package org.example.sec06;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.BalanceCheckRequest;
import org.example.models.sec06.BankServiceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcClient {
    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    public static void main(String[] args) throws Exception {
        var channel = ManagedChannelBuilder.forAddress("localhost", 6565)
                .usePlaintext()
                .build();

//        var stub = BankServiceGrpc.newBlockingStub(channel);

        var stub = BankServiceGrpc.newStub(channel);

//        var balance = stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build());
        stub.getAccountBalance(BalanceCheckRequest.newBuilder().setAccountNumber(2).build(), new StreamObserver<AccountBalance>() {
            @Override
            public void onNext(AccountBalance accountBalance) {
                log.info("{}", accountBalance);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error("{}", throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.error("completed");
            }
        });

        log.info("Account Balance : {} ", "done");
        Thread.sleep(1000);

    }

}
