package org.example.sec06;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.BalanceCheckRequest;
import org.example.models.sec06.BankServiceGrpc;
import org.example.sec06.repository.AccountRepository;

import java.util.Arrays;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        System.out.println("request = " + request + ", responseObserver = " + responseObserver);
        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        System.out.println("Account Balance : " + accountBalance);
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }
}
