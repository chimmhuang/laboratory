package com.chimm.demo02_base_grammar

/**
 * lambda
 *
 * @author chimm
 * @date 2019/12/11
 */

// 测试
fun main() {
    var sumLambda: (Int,Int) -> Int = {x,y -> x + y}
    print(sumLambda(1, 2))
}