package org.example.common;

import org.example.sec06.BankService;
import org.example.sec06.TransferService;
import org.example.sec07.FlowControlService;

public class Demo {
    public static void main(String[] args) {

//        GrpcServer.create(new BankService(), new TransferService(), new FlowControlService())
        GrpcServer.create(new FlowControlService())
                .start()
                .await();
    }
}
