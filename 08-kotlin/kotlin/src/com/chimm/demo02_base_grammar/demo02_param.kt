package com.chimm.demo02_base_grammar

/**
 * 可变长参数函数
 *
 * @author chimm
 * @date 2019/12/11
 */

/**
 * 函数的变长参数可以用 vararg 关键字进行标识
 */
fun vars(vararg v: Int) {
    for (vt in v) {
        print(vt)
    }
}

//测试
fun main() {
    vars(1,2,3,4)
}