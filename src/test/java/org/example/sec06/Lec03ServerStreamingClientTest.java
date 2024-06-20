package org.example.sec06;

import org.example.common.ResponseObserver;
import org.example.models.sec06.BankServiceGrpc;
import org.example.models.sec06.Money;
import org.example.models.sec06.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class Lec03ServerStreamingClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03ServerStreamingClientTest.class);

    @Test
    public void blockingClientWithdrawalTest(){

        var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(20).build();
        var iterator = this.blockingStub.withDraw(request);

        int count = 0;
        while(iterator.hasNext()){
            log.info("Money Received {} ", iterator.next());
            count++;
        }

        Assertions.assertEquals(2,count);

    }

    @Test
    public void asyncClientWithdrawTest(){
        var request = WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(20).build();

        var observer = ResponseObserver.<Money>create();
        this.stub.withDraw(request, observer);
        observer.await();

        Assertions.assertEquals(2, observer.getItems().size());
        Assertions.assertEquals(10, observer.getItems().get(0).getAmount());
        Assertions.assertNull(observer.getThrowable());


    }

}
