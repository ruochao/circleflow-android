# CircleFlow

[![Build Status](https://travis-ci.org/mocircle/circleflow-android.svg?branch=master)](https://travis-ci.org/mocircle/circleflow-android)

CircleFlow is a lightweight flow library for Android. It simulates the flow diagram or activity diagram of UML. User can define some tasks, and connect these tasks as a flow. CircleFlow can control the flow execution, monitor the status and get the results. The task is a executable unit and it should be reusable. There is another unit called control unit which is used to control the flow direction, e.g. Decicion, Fork, Join, Merge, etc.

Please check the [wiki](https://github.com/mocircle/circleflow-android/wiki) for details.

## How to define a task
For simple task, just need to extend from ActionNode.
```
public class CheckOutTask extends ActionNode {
    @Override
    public Token execute() {
        // Task details
        return new Token(Token.TYPE_SUCCESS);
    }
}
```

## How to create a flow
Once you have some basic tasks, you can connect them as a flow. Implement FlowDefinition interface to define a flow.
```
public class OrderFlow implements FlowDefinition {
    @Override
    public InitialNode getDefinition() {
        InitialNode node = new InitialNode();
        node.addOutgoingNode(new CheckOutTask().addOutgoingNode(
                new Decision()
                        .addOutgoingNode(Token.TYPE_FAILURE, new FinalNode())
                        .addOutgoingNode(Token.TYPE_SUCCESS,
                                new WaitForPaymentTask().addOutgoingNode(new FinalNode()))));
        return node;
    }
}
```

## How to execute a flow
For quick flow execution, you can just call like this:
```
CircleFlow.getEngine().executeFlow(new OrderFlow(), inputData);
```
If you want to get flow id before the execution, you also can do as: (this is mostly for listening flow execution)
```
FlowExecutor executor = CircleFlow.getEngine().prepareFlow(new OrderFlow(), inputData);
String flowId = executor.getFlowId();
executor.execute();
```

## Add CircleFlow to your project (incoming...)

CircleFlow is available on Maven Central, you can include it in your project by:

Gradle:
```
compile 'com.mocircle:circleflow:1.0-SNAPSHOT'
```

Maven:
```
<dependency>
    <groupId>com.mocircle</groupId>
    <artifactId>circleflow</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## License

CircleFlow is released under version 2.0 of the Apache License.
