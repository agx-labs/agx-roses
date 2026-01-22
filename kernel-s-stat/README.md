# 点击统计

如需让业务实现点击状态统计，需要在实体上实现两个接口，如下所示：

```java

public class BusinessEntity implements ClickStatusCallback, ClickCountCallback {

    // ...

}
```

在业务的详情接口：

```java
// 增加一次点击次数
Long clickCount = clickCountCalcApi.addOneClickCount(entity);
clickStatusCalcApi.addClickStatus(entity);
```

在业务的列表接口：

```java
// 统计点击状态
clickStatusCalcApi.calcClickStatus(records);
```
