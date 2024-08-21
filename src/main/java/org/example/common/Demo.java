package org.example.common;

import org.example.sec06.TransferService;
import org.example.sec07.FlowControlService;
import org.example.sec08.GuessNumberService;
import org.example.sec12.BankService;
import org.example.sec12.interceptors.ApiKeyValidationInterceptor;

public class Demo {
    public static void main(String[] args) {

//        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService())
//        GrpcServer.create(new FlowControlService())
//          GrpcServer.create(new GuessNumberService())
     /*   GrpcServer.create(new BankService())
                .start()
                .await();*/
        System.out.println("Demo");

        GrpcServer.create(6565, builder -> {builder.addService(new BankService()).intercept(new ApiKeyValidationInterceptor());})
                .start()
                .await();

    }
}
