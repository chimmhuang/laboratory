package com.chimm.logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author huangshuai
 * @date 2019-05-05
 */
public class HelloJcl {

    public static void main(String[] args) {
        Log log = LogFactory.getLog(HelloJcl.class);
        log.error("error message jcl");
    }
}
