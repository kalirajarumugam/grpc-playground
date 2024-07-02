package org.example.common;

import org.example.sec10.BankService;
import org.example.sec06.TransferService;
import org.example.sec07.FlowControlService;
import org.example.sec08.GuessNumberService;

public class Demo {
    public static void main(String[] args) {

//        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService())
//        GrpcServer.create(new FlowControlService())
//          GrpcServer.create(new GuessNumberService())
        GrpcServer.create(new BankService())
                .start()
                .await();
    }
}
