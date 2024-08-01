package org.example.sec12;

import io.grpc.CallCredentials;
import io.grpc.ClientInterceptor;
import io.grpc.Metadata;
import org.example.common.GrpcServer;
import org.example.models.sec12.BalanceCheckRequest;
import org.example.sec12.interceptors.UserRoleInterceptor;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

public class Lec07UserRoleInterceptorTest extends AbstractInterceptorTest {

    public static final Logger log = LoggerFactory.getLogger(Lec07UserRoleInterceptorTest.class);

    @Override
    protected List<ClientInterceptor> getClientInterceptors() {
        return Collections.emptyList();
    }


    protected GrpcServer createServer() {
        log.info("{} ", Lec07UserRoleInterceptorTest.class);
        return GrpcServer.create(6565,
                builder -> {
                    builder.addService(new UerRoleBankService()).intercept(new UserRoleInterceptor());
                });
    }


    @RepeatedTest(5)
    public void unaryUserCredentialsDemo() {

        for (int i = 1; i <= 5; i++) {
            try {
                var request = BalanceCheckRequest.newBuilder().setAccountNumber(1).build();
                var response = this.bankBlockingStub
                        .withCallCredentials(new UserSessionToken("user-token-" + i))
                        .getAccountBalance(request);
                log.info("{}", response);
            } catch (Exception e) {
                log.info("{} ", e.getMessage());
            }
        }
    }


    private static class UserSessionToken extends CallCredentials {

        public final String jwt;
        private static final String TOKEN_FORMAT = "%s %s";

        private UserSessionToken(String jwt) {
            this.jwt = jwt;
        }

        @Override
        public void applyRequestMetadata(RequestInfo requestInfo, Executor executor, MetadataApplier metadataApplier) {

            executor.execute(() -> {
                var metadata = new Metadata();
                metadata.put(Constants.USER_TOKEN_KEY, TOKEN_FORMAT.formatted(Constants.BEARER, jwt));
                metadataApplier.apply(metadata);
            });
        }
    }


}
