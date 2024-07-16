package org.example.sec12;

import io.grpc.ClientInterceptor;
import io.grpc.Deadline;
import io.grpc.Status;
import org.example.common.ResponseObserver;
import org.example.models.sec12.AccountBalance;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.models.sec12.Money;
import org.example.models.sec12.WithdrawRequest;
import org.example.sec12.interceptors.DeadlineInterceptor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lec03DeadlineInterceptorTest extends AbstractInterceptorTest{

    private static final Logger log = LoggerFactory.getLogger(Lec03DeadlineInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
//        return Collections.emptyList();
        return List.of(new DeadlineInterceptor(Duration.ofSeconds(2)));
    }


    @Test
    public void blockingDeadlineTest(){


//        var ex = Assertions.assertThrows(StatusRuntimeException.class,()-> {
            var request = BalanceCheckRequest.newBuilder()
                    .setAccountNumber(1)
                    .build();
            log.info("request {}", request);
            var response = this.bankBlockingStub
//                    .withDeadlineAfter(2,TimeUnit.SECONDS)
                    .getAccountBalance(request);
            log.info("response {}", response);
//        });

//        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,ex.getStatus().getCode());

    }

    @Test
    public void asyncDeadlineTest(){
        var observer = ResponseObserver.<AccountBalance>create();
        var request = BalanceCheckRequest.newBuilder()
                .setAccountNumber(1)
                .build();
        this.bankStub
//                .withDeadlineAfter(2,TimeUnit.SECONDS)
                .getAccountBalance(request,observer);
        observer.await();
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,Status.fromThrowable(observer.getThrowable()).getCode());
    }

    @Test
    public void overrideInterceptor(){

        var observer = ResponseObserver.<Money>create();
        var request = WithdrawRequest.newBuilder().setAmount(50).setAccountNumber(1).build();

        this.bankStub.withDeadline(Deadline.after(4, TimeUnit.SECONDS)).withdraw(request, observer);

        observer.await();


    }


}
