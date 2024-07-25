package org.example.sec11;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import org.example.models.sec11.DeadlineBankServiceGrpc;
import org.example.sec11.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class DeadlineBankService extends DeadlineBankServiceGrpc.DeadlineBankServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(DeadlineBankService.class);

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAccountBalance(org.example.models.sec11.BalanceCheckRequest request, StreamObserver<org.example.models.sec11.AccountBalance> responseObserver) {

        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);
        System.out.println("request = " + request + ", responseObserver = " + responseObserver);
        var accountBalance = org.example.models.sec11.AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();
        System.out.println("Account Balance : " + accountBalance);
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }


    public void withDraw(org.example.models.sec11.WithdrawRequest request, StreamObserver<org.example.models.sec11.Money> responseObserver){

        var accountNumber   = request.getAccountNumber();
        var withdrawalAmount= request.getAmount();
        var accountBalance  = org.example.sec06.repository.AccountRepository.getBalance(accountNumber);

        if(withdrawalAmount > accountBalance){
            responseObserver.onError(Status.FAILED_PRECONDITION.asRuntimeException());
//            responseObserver.onCompleted();
            return;
        }

        for(int i =0; i < (withdrawalAmount/10); i++){
            var money = org.example.models.sec11.Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("Money sent *** {} ", money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        }
        responseObserver.onCompleted();

    }


}



