package org.example.sec03;

import org.example.models.sec03.BodyStyle;
import org.example.models.sec03.Car;
import org.example.models.sec03.Dealer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapDemo {

    private static final Logger log = LoggerFactory.getLogger(MapDemo.class);

    public static void main(String[] args) {

        var car1 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Civic")
                .setYear(2000)
                .setBodyStyle(BodyStyle.SEDAN)
                .build();

        var car2 = Car.newBuilder()
                .setMake("Honda")
                .setModel("Accord")
                .setYear(2002)
                .setBodyStyle(BodyStyle.SUV)
                .build();

        var dealer = Dealer.newBuilder()
                .putInventory(car1.getYear(), car1)
                .putInventory(car2.getYear(), car2)
                .build();

        log.info("{} ", dealer);

        log.info("2002 ? : {} ", dealer.containsInventory(2002));
        log.info("2003 ? : {} ", dealer.containsInventory(2003));

        log.info("2002 Model : {} - {}", dealer.getInventoryOrThrow(2002).getModel(), dealer.getInventoryOrThrow(2002).getBodyStyle());



    }

}
