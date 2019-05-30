package com.chimm.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author huangshuai
 * @date 2019-05-05
 */
public class HelloSlf4j {

    private static Logger logger = LoggerFactory.getLogger(HelloSlf4j.class);

    public static void main(String[] args) {
        logger.error("slf4j - error - abcd");
        logger.info("slf4j - error - abcd");
    }
}
