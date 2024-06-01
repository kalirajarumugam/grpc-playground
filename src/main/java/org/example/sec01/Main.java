package org.example.sec01;

import org.example.models.sec01.PersonOuterClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {


        var person = PersonOuterClass.Person.newBuilder()
                .setName("ram")
                .setAge(10)
                .build();
        log.info("{}", person);

    }
}