package org.example.sec04;

import org.example.models.common.Address;
import org.example.models.common.BodyStyle;
import org.example.models.common.Car;
import org.example.models.sec04.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImportDemo {

    private static final Logger log = LoggerFactory.getLogger(ImportDemo.class);

    public static void main(String[] args) {

        var address = Address.newBuilder().setCity("Atlanda").build();
        var car = Car.newBuilder().setBodyStyle(BodyStyle.SEDAN).build();
        var person = Person.newBuilder()
                .setLastName("sam")
                .setAge(12)
                .setAddress(address)
                .setCar(car)
                .build();
        log.info("{}", person);
    }


}
