# 简介
Eureka中的两个名词：  
`Renews threshold`：server期望在每分钟收到的心跳次数  
`Renews(last min)`：上一分钟内收到的心跳次数

EurekaServer一般禁止注册server自己为client，不管server是否禁止，`threshold`初始值是`1`。client的个数为`n`，`threshold=1+2*n`（此为禁止自注册的情况，这里的乘以2是因为默认每分钟发两次心跳）

当 `renews/threshold<0.85`时，就会进入自我保护机制。

解决自我保护机制的建议：
1. 在生产上开启自注册，部署多个server（这样做是为了增大`renews`）
2. 简单粗暴的把自我保护模式关闭（开发或测试，生产不建议）
```yaml
eureka:
  server:
    # 自我保护机制关闭（开发或测试环境，建议生产环境开启）
    enable-replicated-request-compression: false
```