package org.example.sec10.validator;

import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.protobuf.ProtoUtils;
import org.example.models.sec10.ErrorMessage;
import org.example.models.sec10.ValidationCode;

import java.util.Optional;

public class RequestValidator {

    public static final Metadata.Key<ErrorMessage> ERROR_KEY = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());

    public static Optional<StatusRuntimeException> validateAccount(int accountNumber){
        if (accountNumber > 0 && accountNumber <11)
            return Optional.empty();
        var metadata = toMetaData(ValidationCode.INVALID_ACCOUNT);
//        return Optional.of(Status.INVALID_ARGUMENT.withDescription("Account Number should be between 1 and 10"));
        return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));
    }

    public static Optional<StatusRuntimeException> isAmountDivisibleBy10(int amount){
        if (amount > 0 && amount % 10 == 0)
            return Optional.empty();
        var metadata = toMetaData(ValidationCode.INVALID_AMOUNT);
//        return Optional.of(Status.INVALID_ARGUMENT.withDescription("Account Number should be multiples of 10"));
        return Optional.of(Status.INVALID_ARGUMENT.asRuntimeException(metadata));

    }



    public static Optional<StatusRuntimeException> hasSufficientBalance(int amount, int balance){
        if (amount <= balance)
            return Optional.empty();
        var metadata = toMetaData(ValidationCode.INSUFFICIENT_BALANCE);
//        return Optional.of(Status.FAILED_PRECONDITION.withDescription("Insufficient Balance"));
        return Optional.of(Status.FAILED_PRECONDITION.asRuntimeException(metadata));

    }

    private static Metadata toMetaData(ValidationCode code){
        var metadata = new Metadata();
//        var key = ProtoUtils.keyForProto(ErrorMessage.getDefaultInstance());
        var errorMessage = ErrorMessage.newBuilder().setValidationCode(code).build();
        metadata.put(ERROR_KEY, errorMessage);
        var key = Metadata.Key.of("description", Metadata.ASCII_STRING_MARSHALLER);
        metadata.put(key, code.toString());
        return metadata;
    }

}
