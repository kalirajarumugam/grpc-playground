package org.example.sec06;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.BalanceCheckRequest;
import org.example.models.sec06.BankServiceGrpc;

import java.util.Arrays;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

        var accountNumber = request.getAccountNumber();
        System.out.println("request = " + request + ", responseObserver = " + responseObserver);
        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(accountNumber*10)
                .build();
        System.out.println("Account Balance : " + accountBalance);
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }
}
