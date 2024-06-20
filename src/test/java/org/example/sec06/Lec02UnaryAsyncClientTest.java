package org.example.sec06;

import com.google.protobuf.Empty;
import org.example.common.ResponseObserver;
import org.example.models.sec06.AccountBalance;
import org.example.models.sec06.AllAccountsResponse;
import org.example.models.sec06.BalanceCheckRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lec02UnaryAsyncClientTest extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec02UnaryAsyncClientTest.class);

    @Test
    public void getBalanceTest(){
        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        var observer = ResponseObserver.<AccountBalance>create();
        this.stub.getAccountBalance(request, observer);
        observer.await();
        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(100, observer.getItems().get(0).getBalance());
        Assertions.assertNull(observer.getThrowable());

        log.info("Unary Balance Received {} ", observer);

    }

    @Test
    public void allAccountsTest(){

        var observer = ResponseObserver.<AllAccountsResponse>create();

        this.stub.getAllAccounts(Empty.getDefaultInstance(), observer);
        observer.await();

        Assertions.assertEquals(1, observer.getItems().size());
        Assertions.assertEquals(100, observer.getItems().get(0).getAccountsCount());
        Assertions.assertNull(observer.getThrowable());

    }




}
