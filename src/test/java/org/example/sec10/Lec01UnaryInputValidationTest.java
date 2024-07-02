package org.example.sec10;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.ResponseObserver;
import org.example.models.sec10.AccountBalance;
import org.example.models.sec10.BalanceCheckRequest;
import org.example.models.sec10.ValidationCode;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec01UnaryInputValidationTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01UnaryInputValidationTest.class);

    @Test
    public void blockingInputValidationTest(){
        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
            var request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
            var response = this.blockingStub.getAccountBalance(request);
        });
//        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ex.getStatus().getCode());
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(ex));
    }

    @Test
    public void asyncInputValidationTest(){
        log.info("Test1");;
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(11).build();
        log.info("Test2");
        var observer = ResponseObserver.<AccountBalance>create();
        log.info("Test3");
        this.stub.getAccountBalance(request, observer);
        log.info("Test4");
        observer.await();
        log.info("Test5");
        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
//        Assertions.assertEquals(Status.Code.INVALID_ARGUMENT, ((StatusRuntimeException) observer.getThrowable()).getStatus().getCode());
        Assertions.assertEquals(ValidationCode.INVALID_ACCOUNT, getValidationCode(observer.getThrowable()));

    }


}
