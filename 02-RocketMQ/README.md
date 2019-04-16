# 1. 简单的消息示例
- 使用RocketMQ以3种方式发送消息：同步发送、异步发送、单向传输
- 使用RocketMQ来消费消息

# 2. OrderExample(顺序消费消息)
- RocketMQ使用FIFO(first input first output先进先出)顺序提供有序消息

# 3. TransactionExample(事务消息)
1. 什么是事务消息？  
它可以被认为是两阶段提交消息实现，以确保分布式系统中的最终一致性。事务性消息确保可以原子方式执行本地事务的执行和消息的发送。

2. 使用限制  
    - 交易消息没有时间表和批量支持。
    - 为了避免多次检查单个消息并导致半队列消息累积，我们默认将单个消息的检查次数限制为15次，但用户可以通过更改“transactionCheckMax”来更改此限制“代理配置中的参数，如果已经通过”transactionCheckMax“检查了一条消息，则代理将默认丢弃此消息并同时打印错误日志。用户可以通过覆盖“AbstractTransactionCheckListener”类来更改此行为。
    - 在经纪人的配置中由参数“transactionTimeout”确定的一段时间之后将检查交易消息。用户也可以通过在发送事务消息时设置用户属性“CHECK_IMMUNITY_TIME_IN_SECONDS”来更改此限制，此参数优先于“transactionMsgTimeout”参数。
    - 可以多次检查或消费交易消息。
    - 对用户的目标主题的已提交消息可能会失败。目前，它取决于日志记录。RocketMQ本身的高可用性机制确保了高可用性。如果要确保事务性消息不会丢失并且保证事务完整性，建议使用同步双写。机制。
    - 事务消息的生产者ID不能与其他类型消息的生产者ID共享。与其他类型的消息不同，事务性消息允许后向查询。MQ Server按其生产者ID查询客户端。


![RocketMQ事务消息](https://github.com/chimmhuang/laboratory/blob/master/02-RocketMQ/images/RocketMQ%E4%BA%8B%E5%8A%A1%E6%B6%88%E6%81%AF.png)
![RocketMQ分布式事务.png](https://github.com/chimmhuang/laboratory/blob/master/02-RocketMQ/images/RocketMQ%E5%88%86%E5%B8%83%E5%BC%8F%E4%BA%8B%E5%8A%A1.png)

# 4. BroadcastingExample(广播消息)
- 广播正在向主题的所有订阅者发送消息。如果您希望所有订阅者都收到有关主题的消息，则广播是一个不错的选择。
