package org.example.sec10;


import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.example.common.ResponseObserver;

import org.example.models.sec10.Money;
import org.example.models.sec10.ValidationCode;
import org.example.models.sec10.WithdrawRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

public class Lec02ServerStreamingInputValidationTest extends AbstractTest {


    @ParameterizedTest
    @MethodSource("testData")
    public void blockingInputValidationTest(WithdrawRequest request, ValidationCode code){

        var ex = Assertions.assertThrows(StatusRuntimeException.class, () -> {
           var response= this.blockingStub.withDraw(request).hasNext();
        });
//        Assertions.assertEquals(code, ex.getStatus().getCode());
        Assertions.assertEquals(code, getValidationCode(ex));
    }

    @ParameterizedTest
    @MethodSource("testData")
    public void asyncInputValidationTest(WithdrawRequest request, ValidationCode code){

        var observer = ResponseObserver.<Money>create();
        this.stub.withDraw(request, observer);
        observer.await();

        Assertions.assertTrue(observer.getItems().isEmpty());
        Assertions.assertNotNull(observer.getThrowable());
//        Assertions.assertEquals(code, ((StatusRuntimeException)observer.getThrowable()).getStatus().getCode());
        Assertions.assertEquals(code, getValidationCode(observer.getThrowable()));


    }



    private Stream<Arguments> testData(){

        /*return Stream.of(Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(50).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(17).build(), Status.Code.INVALID_ARGUMENT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(170).build(), Status.Code.FAILED_PRECONDITION)
        );*/

        return Stream.of(Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(11).setAmount(50).build(), ValidationCode.INVALID_ACCOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(20).build(), ValidationCode.INVALID_AMOUNT),
                Arguments.of(WithdrawRequest.newBuilder().setAccountNumber(1).setAmount(170).build(), ValidationCode.INSUFFICIENT_BALANCE)
        );

    }

}
