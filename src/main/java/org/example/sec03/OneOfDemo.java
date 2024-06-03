package org.example.sec03;

import org.example.models.sec03.Credentials;
import org.example.models.sec03.Email;
import org.example.models.sec03.Phone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneOfDemo {

    private static final Logger log = LoggerFactory.getLogger(OneOfDemo.class);


    public static void main(String[] args) {

        var email = Email.newBuilder().setAddress("sam@gmail.com").setPassword("admin").build();
        var phone = Phone.newBuilder().setNumber(123456789).setCode(123).build();

        login(Credentials.newBuilder().setEmail(email).build());
        login(Credentials.newBuilder().setPhone(phone).build());
        login(Credentials.newBuilder().setPhone(phone).setEmail(email).build()); // last set one will take
    }

    private static void login(Credentials credentials) {

        switch (credentials.getLoginTypeCase()){
            case EMAIL -> log.info("email {}", credentials.getEmail());
            case PHONE -> log.info("phone {}", credentials.getPhone());

        }

    }
}
