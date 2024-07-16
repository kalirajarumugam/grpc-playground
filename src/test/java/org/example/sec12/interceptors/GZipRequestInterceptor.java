package org.example.sec12.interceptors;

import io.grpc.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipRequestInterceptor implements ClientInterceptor {

    private static final Logger log = LoggerFactory.getLogger(GZipRequestInterceptor.class);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        log.info("DeadlineInterceptor, callOptions {} , methodDescriptor {} ", callOptions, methodDescriptor);

//                callOptions = Objects.isNull(callOptions)? callOptions.withDeadlineAfter(duration.toSeconds(), TimeUnit.SECONDS) : callOptions;

        return channel.newCall(methodDescriptor, callOptions.withCompression("gzip"));
    }
}
