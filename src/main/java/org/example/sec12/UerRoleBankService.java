
package org.example.sec12;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.example.models.sec12.*;
import org.example.sec12.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class UerRoleBankService extends BankServiceGrpc.BankServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(UerRoleBankService.class);
    @Override
    public void getAccountBalance(BalanceCheckRequest request, StreamObserver<AccountBalance> responseObserver) {
        var accountNumber = request.getAccountNumber();
        var balance = AccountRepository.getBalance(accountNumber);

        log.info("Context Key : {}", Constants.USER_ROLE_KEY.get());
        if(UserRole.STANDARD.equals(Constants.USER_ROLE_KEY.get())){
            var fee = balance > 0 ?1 : 0;
            AccountRepository.deductAmount(accountNumber, fee);
            balance = balance - fee;
        }

        var accountBalance = AccountBalance.newBuilder()
                .setAccountNumber(accountNumber)
                .setBalance(balance)
                .build();

        responseObserver.onNext(accountBalance);
        responseObserver.onCompleted();
    }


}