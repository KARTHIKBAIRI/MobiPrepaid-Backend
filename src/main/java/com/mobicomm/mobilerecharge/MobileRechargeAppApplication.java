package com.mobicomm.mobilerecharge;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.mobicomm") // Ensure it covers all sub-packages
public class MobileRechargeAppApplication {
//    private static final Logger logger = LoggerFactory.getLogger(MobileRechargeAppApplication.class);

    public static void main(String[] args) {
//        logger.info("java.io.tmpdir: {}", System.getProperty("java.io.tmpdir"));
//        logger.info("db_url: {}", System.getenv("db_url"));
//        logger.info("db_username: {}", System.getenv("db_username"));
//        logger.info("db_password: {}", System.getenv("db_password"));
//        logger.info("jwt_secret: {}", System.getenv("jwt_secret"));
//        logger.info("mail_username: {}", System.getenv("mail_username"));
//        logger.info("mail_password: {}", System.getenv("mail_password"));
        SpringApplication.run(MobileRechargeAppApplication.class, args);
    }
}