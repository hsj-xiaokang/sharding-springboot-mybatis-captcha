# sharding-jdbc springboot mybatis分库分表、图片滑动验证（服务端生成抠图、滑块和X轴偏移量，前端获取用户拖动X轴偏移量，传入服务端并校验）demo示例

#### 介绍
sharding-springboot-mybatis

 **_分库分表【application-dbtable.yml】【无法跨库join操作】_** ，两个库：test_order0和test_order1,每个库里面三张表:t_address/t_user0/t_user1

 **_单库分表【application-table.yml】【可以跨库join操作】_** ，1个库：test_order2,库里面7张表:t_address/t_user0/t_user1/t_user2/t_user3/t_user4/t_user5

雪花算法【new SnowFlake(2, 3).nextId()】根据主键id取模hash；

分库分表：库分片算法：id%2；表分片算法：id/2%2;

单库分表：表分片算法：id%6;

包含inline配置算法和自定义class方式算法demo。

作者：2356899074@qq.com
