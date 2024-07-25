package org.example.sec11;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.ResponseObserver;
import org.example.models.sec11.AccountBalance;
import org.example.models.sec11.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class Lec01UnaryDeadlineTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01UnaryDeadlineTest.class);

    @Test
    public void blockingDeadlineTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder().setAccountNumber(2).build();
            var response = this.blockingStub.withDeadlineAfter(1, TimeUnit.SECONDS).getAccountBalance(request);
            log.info("{} ", response);
        });
        log.info(" ########## {} @- {} @- {}", ex.getMessage(), ex.getStatus().getCode(), Status.fromThrowable(ex).getCode());
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, Status.fromThrowable(ex).getCode());
//        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED, ex.getStatus().getCode());

//        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());

    }

    @Test
    public void asyncDeadlineTest(){
        log.info("Test1");
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(2).build();
        log.info("Test2");
        var observer = ResponseObserver.<AccountBalance>create();
        log.info("Test3");
        this.stub.withDeadlineAfter(2, TimeUnit.SECONDS).getAccountBalance(request, observer);
        log.info("Test4");
        observer.await();
        log.info("Test5");
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
//        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
        log.info("Exception #### {}", observer.getThrowable().getMessage() );
        Assertions.assertEquals(Status.Code.DEADLINE_EXCEEDED,Status.fromThrowable(observer.getThrowable()).getCode());

    }


}
