package org.example.sec06;

import com.google.common.util.concurrent.Uninterruptibles;
import org.example.common.ResponseObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.DepositRequest;
import org.example.models.sec06.Money;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Lec04ClientStreamingTest extends AbstractTest{

    @Test
    public void depositTest(){

        var responseObserver = ResponseObserver.<AccountBalance>create();
        var requestObserver  = this.stub.deposit(responseObserver);

        requestObserver.onNext(DepositRequest.newBuilder().setAccountNumber(5).build());
//        Uninterruptibles.sleepUninterruptibly(2, TimeUnit.SECONDS);
//        requestObserver.onError(new RuntimeException("Test exception"));
        IntStream.rangeClosed(1, 10)
                .mapToObj(i -> Money.newBuilder().setAmount(10).build())
                .map(money -> DepositRequest.newBuilder().setMoney(money).build())
                .forEach(requestObserver::onNext);

/*        IntStream.rangeClosed(1, 10)
                .map(i-> DepositRequest.newBuilder().setMoney(Money.newBuilder().setAmount(10).build()).build())
                .forEach(requestObserver::onNext);*/

        requestObserver.onCompleted();

        responseObserver.await();

        Assertions.assertEquals(1, responseObserver.getItems().size());
        Assertions.assertEquals(200, responseObserver.getItems().get(0).getBalance());
        Assertions.assertNull(responseObserver.getThrowable());



    }
}
