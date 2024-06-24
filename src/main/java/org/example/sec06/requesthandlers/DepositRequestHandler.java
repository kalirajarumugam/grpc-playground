package org.example.sec06.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.DepositRequest;
import org.example.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DepositRequestHandler implements StreamObserver<DepositRequest> {

    private static final Logger log = LoggerFactory.getLogger(DepositRequestHandler.class);

    private final StreamObserver<AccountBalance> responseObserver;
    private int accountNumber;

    public DepositRequestHandler(StreamObserver<AccountBalance> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(DepositRequest depositRequest) {
        log.info("Received deposit request: {}", depositRequest);
        switch(depositRequest.getRequestCase()){
            case ACCOUNT_NUMBER: accountNumber = depositRequest.getAccountNumber(); break;
            case MONEY :
                AccountRepository.addAmount(accountNumber,depositRequest.getMoney().getAmount());
        }

    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Error received: {}", throwable.getMessage());
        this.responseObserver.onError(throwable);
    }

    @Override
    public void onCompleted() {
        log.info("Completed deposit request");
        var accountBalance = AccountBalance.newBuilder().setAccountNumber(accountNumber).setBalance(AccountRepository.getBalance(accountNumber)).build();
        log.info("Completed deposit request : " + accountBalance);
        responseObserver.onNext(accountBalance);
        this.responseObserver.onCompleted();
    }
}
