package org.example.sec02;

import org.example.models.sec02.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {


        var person1 = createPerson();
        var person2 = createPerson();

        log.info("equal {}",person1.equals(person2));
        log.info("equal {}",(person1 == person2));

        //Immutable
        var person3 = person1.toBuilder().setName("Mike").build();
        log.info("equal immutable {}",person1.equals(person3));
        log.info("equal immutable{}",(person1 == person3));

        var person4 = person1.toBuilder().clearName().build();
        log.info("Person4:{}",person4);

    }
    private static Person createPerson(){
        return Person.newBuilder()
                .setName("Sam")
                .setAge(12)
                .build();
    }


}