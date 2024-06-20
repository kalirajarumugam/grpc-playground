package org.example.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.example.models.sec06.*;
import org.example.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

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


    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAllAccounts(Empty request, StreamObserver<AllAccountsResponse> responseObserver) {

        var accountList = AccountRepository.getAllAcounts().entrySet()
                .stream().map(a-> AccountBalance.newBuilder().setAccountNumber(a.getKey()).setBalance(a.getValue()).build())
                .toList();

        var response = AllAccountsResponse.newBuilder().addAllAccounts(accountList).build();

        System.out.println("Account List : " + response);
        responseObserver.onNext(response);
        responseObserver.onCompleted();


    }



    public void withDraw(WithdrawRequest request, StreamObserver<Money> responseObserver){

        var accountNumber   = request.getAccountNumber();
        var withdrawalAmount= request.getAmount();
        var accountBalance  = AccountRepository.getBalance(accountNumber);

        if(withdrawalAmount > accountBalance){
            responseObserver.onCompleted();
            return;
        }

        for(int i =0; i < (withdrawalAmount/10); i++){
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("Money sent *** {} ", money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        }
        responseObserver.onCompleted();

    }

}
