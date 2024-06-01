package org.example.sec03;

import org.example.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class SerializationDemo {

    private static final Logger log = LoggerFactory.getLogger(SerializationDemo.class);

    private static Path PATH = Path.of("person.out");

    public static void main(String[] args) throws IOException {
        var person = Person.newBuilder().setLastName("Sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setSalary(100.2354)
                .setBankAccountNumber(1236547895)
                .setBalance(-1000)
                .build();

        log.info("Person : {}", person);
        serialize(person);
        log.info("Deserialize {}",deserialize());
        log.info("equals : {}", person.equals(deserialize()));


    }

    private static void serialize(Person person) throws IOException {
//        log.info("serialize");
        try(var stream = Files.newOutputStream(PATH)) {
            person.writeTo(stream);
        }
    }

    private static Person deserialize() throws IOException {
//        log.info("deserialize");
        try(var stream = Files.newInputStream(PATH)) {
            return Person.parseFrom(stream);
        }

    }


}
