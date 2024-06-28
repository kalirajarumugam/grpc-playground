package org.example.sec07;

import com.google.common.util.concurrent.Uninterruptibles;
import io.grpc.stub.StreamObserver;
import org.example.models.sec07.Output;
import org.example.models.sec07.RequestSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ResponseHandler implements StreamObserver<Output> {

    public static final Logger log = LoggerFactory.getLogger(ResponseHandler.class);
    private final CountDownLatch latch = new CountDownLatch(1);
    private StreamObserver<RequestSize> requestObserver;
    private int size;





    @Override
    public void onNext(Output output) {
        this.size--;
        this.process(output);
        if(size == 0) {

            int size1 = ThreadLocalRandom.current().nextInt(1, 6);
            log.info("-------------------------- {}", size1);
            this.request(size1);
        }
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

    }

    public void setRequestObserver(StreamObserver<RequestSize> requestObserver){
        this.requestObserver = requestObserver;
    }

    public void request(int size){
        log.info("requesting size {} ", size);
        this.size = size;
        this.requestObserver.onNext(RequestSize.newBuilder().setSize(size).build());
    }

    private void process(Output output){
        log.info("received {} ", output);
        Uninterruptibles.sleepUninterruptibly(ThreadLocalRandom.current().nextInt(50, 200), TimeUnit.MILLISECONDS);
    }

    public void await(){

        try {
            this.latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        this.request(3);
    }
}
