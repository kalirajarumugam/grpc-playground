package org.example.sec03;

import org.example.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScalarTypeDemo {

    private static final Logger log = LoggerFactory.getLogger(ScalarTypeDemo.class);

    public static void main(String[] args) {
        var person = Person.newBuilder().setLastName("Sam")
                .setAge(12)
//                .setEmail("sam@gmail.com")
//                .setSalary(100.2354)
//                .setBankAccountNumber(1236547895)
//                .setBalance(-1000)
                .build();

        log.info("equals {}", person);

    }
}
