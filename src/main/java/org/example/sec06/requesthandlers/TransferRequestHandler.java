package org.example.sec06.requesthandlers;

import io.grpc.stub.StreamObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.TransferRequest;
import org.example.models.sec06.TransferResponse;
import org.example.models.sec06.TransferStatus;
import org.example.sec06.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransferRequestHandler implements StreamObserver<TransferRequest> {

    private static final Logger log = LoggerFactory.getLogger(TransferRequestHandler.class);
    private final StreamObserver<TransferResponse> responseObserver;

    public TransferRequestHandler(StreamObserver<TransferResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(TransferRequest transferRequest) {

        var status = transfer(transferRequest);
        if(status.equals(TransferStatus.COMPLETED)) {
            var response = TransferResponse.newBuilder()
                    .setFromAccount(setAccountBalance(transferRequest.getFromAccount()))
                    .setToAccount(setAccountBalance(transferRequest.getToAccount()))
                    .setStatus(status).build();
            log.info("TransferRequestHandler onNext: {}", response);
            responseObserver.onNext(response);
        }

    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Error occurred: {}", throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("TransferRequest Handler completed ");
    }


    private TransferStatus transfer(TransferRequest transferRequest) {
        var fromAccount = transferRequest.getFromAccount();
        var toAccount = transferRequest.getToAccount();
        var amount = transferRequest.getAmount();
        var status = TransferStatus.REJECTED;

        if(AccountRepository.getBalance(fromAccount) >= amount && fromAccount != toAccount) {
            AccountRepository.deductAmount(fromAccount,amount);
            AccountRepository.addAmount(toAccount,amount);
            status = TransferStatus.COMPLETED;
        }

        log.info("Transfer Status- {} - {} from {} to {}", status, amount, fromAccount, toAccount);
        return status;
    }

    private AccountBalance setAccountBalance(int accoutNumber) {

        return AccountBalance.newBuilder().setAccountNumber(accoutNumber).setBalance(AccountRepository.getBalance(accoutNumber)).build();
    }

}
