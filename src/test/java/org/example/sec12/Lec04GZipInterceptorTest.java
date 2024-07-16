package org.example.sec12;

import io.grpc.ClientInterceptor;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.sec12.interceptors.DeadlineInterceptor;
import org.example.sec12.interceptors.GZipRequestInterceptor;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;

public class Lec04GZipInterceptorTest extends AbstractInterceptorTest{

    private static final Logger log = LoggerFactory.getLogger(Lec04GZipInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return List.of(new GZipRequestInterceptor(), new DeadlineInterceptor(Duration.ofSeconds(2)));
    }

    @Test
    public void gzipDemo(){

        var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
        log.info("request {}", request);
        var response = this.bankBlockingStub.getAccountBalance(request);
        log.info("request {}", response);
    }
}
