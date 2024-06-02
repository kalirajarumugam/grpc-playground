package org.example.sec03;

import org.example.models.sec03.Address;
import org.example.models.sec03.School;
import org.example.models.sec03.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompositionDemo {

    private static final Logger log = LoggerFactory.getLogger(CompositionDemo.class);

    public static void main(String[] args) {

        var address = Address.newBuilder()
                .setStreet("123 Main Street")
                .setCity("San Francisco")
                .setState("CA")
                .build();

        var student = Student.newBuilder()
                .setName("sam")
                .setAddress(address)
                .build();

        var school = School.newBuilder()
                .setId(1)
                .setName("High School")
                .setAddress(address.toBuilder().setStreet("234 Main Street").build())
                .build();


        log.info("school:  {}", school);
        log.info("student: {}", student);
        log.info("student: {}", student.getAddress().getStreet());



    }
}
