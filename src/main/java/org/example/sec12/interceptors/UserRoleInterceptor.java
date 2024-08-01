package org.example.sec12.interceptors;

import io.grpc.*;
import org.example.sec12.Constants;
import org.example.sec12.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

public class UserRoleInterceptor implements ServerInterceptor {

    public static final Logger log = LoggerFactory.getLogger(UserRoleInterceptor.class);
    private static final Set<String> PRIME_SET = Set.of("user-token-1", "user-token-2");
    private static final Set<String> STANDARD_SET = Set.of("user-token-3", "user-token-4");

    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {


        var token = extractToken(metadata.get(Constants.USER_TOKEN_KEY));
        log.info("Token {}", token);
        var context = toContext(token);

        if(Objects.nonNull(context)){
            return Contexts.interceptCall(context, serverCall, metadata, serverCallHandler);
        }

        return close(serverCall, metadata, Status.PERMISSION_DENIED.withDescription("user is not allowed to do this operation"));
    }

    private String extractToken(String value){

        return Objects.nonNull(value) && value.startsWith(Constants.BEARER)?
                value.substring(Constants.BEARER.length()).trim() : null;
    }

    private boolean isValid(String token){

        return Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token));
    }


    private Context toContext(String token){

        if (Objects.nonNull(token) && (PRIME_SET.contains(token) || STANDARD_SET.contains(token))){
            var role = PRIME_SET.contains(token)? UserRole.PRIME:UserRole.STANDARD;
            return Context.current().withValue(Constants.USER_ROLE_KEY, role);
        }
        return null;
    }


    private <ReqT, RespT> ServerCall.Listener<ReqT> close(ServerCall<ReqT, RespT> serverCall, Metadata metadata, Status status){
        serverCall.close(status, metadata);
        return new ServerCall.Listener<ReqT>(){
        };
    }

}
