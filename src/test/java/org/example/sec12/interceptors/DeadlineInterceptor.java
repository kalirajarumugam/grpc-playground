package org.example.sec12.interceptors;

import io.grpc.*;
import org.example.sec12.Lec03DeadlineInterceptorTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class DeadlineInterceptor implements ClientInterceptor {
    private final Duration duration;

    public DeadlineInterceptor(Duration duration) {
        this.duration = duration;
    }
    private static final Logger log = LoggerFactory.getLogger(DeadlineInterceptor.class);


    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor,
                                                               CallOptions callOptions,
                                                               Channel channel) {
//        callOptions = callOptions.withDeadlineAfter(duration.toSeconds(), TimeUnit.SECONDS);
        callOptions = Objects.nonNull(callOptions.getDeadline())? callOptions : callOptions.withDeadlineAfter(duration.toSeconds(), TimeUnit.SECONDS);
        log.info("DeadlineInterceptor, callOptions {} , methodDescriptor {} ", callOptions, methodDescriptor);

//                callOptions = Objects.isNull(callOptions)? callOptions.withDeadlineAfter(duration.toSeconds(), TimeUnit.SECONDS) : callOptions;

        return channel.newCall(methodDescriptor, callOptions);
    }
}
