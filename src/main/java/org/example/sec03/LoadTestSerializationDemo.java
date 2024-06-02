package org.example.sec03;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.example.models.sec03.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class LoadTestSerializationDemo {

    private static final Logger log = LoggerFactory.getLogger(ScalarTypeDemo.class);

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        var protoPerson = Person.newBuilder().setLastName("Sam")
                .setAge(12)
                .setEmail("sam@gmail.com")
                .setSalary(100.2354)
                .setBankAccountNumber(123654789L)
                .setBalance(-1000)
                .build();

        var jsonPerson = new JsonPerson("Sam", 12, "sam@gmail.com", 100.2354, 123654789L, -1000);

        for(int i=0; i< 5; i++) {
           /* runTest("proto", () -> proto(protoPerson));
            runTest("json", () -> json(jsonPerson));*/
            runTest("proto", null, protoPerson, jsonPerson);
            runTest("json",  null, protoPerson, jsonPerson);
            runTest("proto", () -> proto(protoPerson), protoPerson, jsonPerson);
            runTest("json",  () -> json(jsonPerson), protoPerson, jsonPerson);
        }

    }

    private static void proto(Person person){

        try {
            var bytes = person.toByteArray();
//            log.info("proto bytes length: {}", bytes.length);
            Person.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }
    }


    private static void json(JsonPerson person){


        try {
            var bytes = mapper.writeValueAsBytes(person);
//            log.info("json bytes length: {}", bytes.length);
            mapper.readValue(bytes, JsonPerson.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void runTest(String testName, Runnable runnable, Person protoPerson, JsonPerson jsonPerson ){

        String test = testName;
        var start = System.nanoTime();


        for (int i = 0; i < 1_000_000; i++) {
            if (runnable != null) {
                test = testName + " Thread";
                runnable.run();
            }else {
                if (testName.equals("proto"))
                    proto(protoPerson);
                else
                    json(jsonPerson);
            }
        }
        var end = System.nanoTime();
        log.info("time taken for {} - {} ns", test, (end - start) / 1000000);


    }


}
