   有时候我们需要通过同一套 mybatis mapper 来操作相同的表结构但是却拥有不同的表名的时候，这个插件可以帮助我们在执行mapper的时候动态更改操作的表名字。目前只提供在做增删改查的时候，动态的追加表名后缀到mybatis的mapper sql 中。

特性

基于springboot2开发，实现了自动装配。完美兼容 pagehelper 插件，只捕获处理 select,insert,delete,update 语句。项目地址: tablehelper

blog 地址: https://magicdogs.github.io/blog/2019-03/mybatis%E8%A1%A8%E5%90%8D%E4%BF%AE%E6%94%B9%E6%8F%92%E4%BB%B6/

MAVEN使用姿势
<dependency>
   <groupId>com.magicdogs.tablehelper</groupId>
   <artifactId>springboot2-tablehelper</artifactId>
   <version>1.0.0-SNAPSHOT</version>
</dependency>

项目中使用方式

PageHelper.startPage(1,10);

TableNameHelper.suffix("_default");

int id = 587;

RiskRuleProperty riskRuleProperty = riskRulePropertyMapper.selectByPrimaryKey(id);

TableNameHelper.suffix("_default");

riskRulePropertyMapper.updateByPrimaryKeySelective(riskRuleProperty);

TableNameHelper.suffix("_default");

riskRulePropertyMapper.deleteByPrimaryKey(id);

TableNameHelper.suffix("_default");

riskRulePropertyMapper.insertSelective(riskRuleProperty);
