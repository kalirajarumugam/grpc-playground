package org.example.sec10;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;
import org.example.models.sec10.*;
import org.example.sec10.repository.AccountRepository;
import org.example.sec10.validator.RequestValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class BankService extends BankServiceGrpc.BankServiceImplBase {

    private static final Logger log = LoggerFactory.getLogger(BankService.class);

    /**
     * @param request
     * @param responseObserver
     */
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

        RequestValidator.validateAccount(request.getAccountNumber())
//                .map(Status::asRuntimeException)
                .ifPresentOrElse(responseObserver::onError,
                        () -> sendAccountBalance(request, responseObserver));

    }

    private void sendAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {

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

    public void withDraw(WithdrawRequest request, StreamObserver<Money> responseObserver) {

        RequestValidator.validateAccount(request.getAccountNumber())
                .or(() -> RequestValidator.isAmountDivisibleBy10(request.getAmount()))
                .or(() -> RequestValidator.hasSufficientBalance(request.getAmount(), AccountRepository.getBalance(request.getAccountNumber())))
//                        .map(Status::asRuntimeException)
                .ifPresentOrElse(responseObserver::onError, () -> sendMoney(request, responseObserver));

    }

    private static void sendMoney(WithdrawRequest request, StreamObserver<Money> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var withdrawalAmount = request.getAmount();
        var accountBalance = AccountRepository.getBalance(accountNumber);

        if (withdrawalAmount > accountBalance) {
            responseObserver.onCompleted();
            return;
        }

        for (int i = 0; i < (withdrawalAmount / 10); i++) {
            var money = Money.newBuilder().setAmount(10).build();
            responseObserver.onNext(money);
            log.info("Money sent *** {} ", money);
            AccountRepository.deductAmount(accountNumber, 10);
            Uninterruptibles.sleepUninterruptibly(1, TimeUnit.SECONDS);

        }
        responseObserver.onCompleted();
    }


}



