package org.example.sec08;

import io.grpc.stub.StreamObserver;
import org.example.models.sec08.GuessRequest;
import org.example.models.sec08.GuessResponse;
import org.example.models.sec08.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;

public class GuessNumberRequestHandler implements StreamObserver<GuessRequest> {

    private static final Logger log = LoggerFactory.getLogger(GuessNumberRequestHandler.class);

    private final StreamObserver<GuessResponse> response;

    private final int serverGuess;
    private final int start = 0;
    private final int end = 100;

    private int attempt = 0;



    public GuessNumberRequestHandler(StreamObserver<GuessResponse> response) {
        this.response = response;
        this.serverGuess = ThreadLocalRandom.current().nextInt(start, end);
        attempt = 0;
        log.info("Server Guess Number : " + serverGuess);
    }


    @Override
    public void onNext(GuessRequest guessRequest) {
        var clientGuess = guessRequest.getGuess();
        var result = Result.CORRECT;
        if( clientGuess < serverGuess)
            result = Result.TOO_LOW;
        else if( clientGuess > serverGuess)
            result = Result.TOO_HIGH;
        attempt++;

        var guessResponse = GuessResponse.newBuilder().setAttempt(attempt).setResult(result).build();
        if(result == Result.CORRECT){
            response.onNext(guessResponse);
            response.onCompleted();
        }else
            response.onNext(guessResponse);
    }

    @Override
    public void onError(Throwable throwable) {
        log.info("Error *** : " + throwable.getMessage());
    }

    @Override
    public void onCompleted() {
        log.info("Completed :");
        response.onCompleted();
    }
}
