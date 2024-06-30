package org.example.sec08;

import io.grpc.stub.StreamObserver;
import org.example.models.sec08.GuessNumberGrpc;
import org.example.models.sec08.GuessRequest;
import org.example.models.sec08.GuessResponse;
import org.example.sec07.FlowControlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GuessNumberService extends GuessNumberGrpc.GuessNumberImplBase {

    private static final Logger log = LoggerFactory.getLogger(GuessNumberService.class);

    /**
     * @param responseObserver
     */
    @Override
    public StreamObserver<GuessRequest> makeGuess(StreamObserver<GuessResponse> responseObserver) {
        return new GuessNumberRequestHandler(responseObserver);
    }
}
