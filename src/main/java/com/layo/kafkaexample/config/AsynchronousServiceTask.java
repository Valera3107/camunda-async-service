package com.layo.kafkaexample.config;

import com.layo.kafkaexample.service.common.MessageQueue;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AsynchronousServiceTask extends AbstractBpmnActivityBehavior {

    private final MessageQueue queue;

    public static final String EXECUTION_ID = "execution_id";

    public void execute(final ActivityExecution execution) {
        Map<String, Object> data = new HashMap<>(execution.getVariables());
        data.put(EXECUTION_ID, execution.getId());
        queue.send(new MessageQueue.Message(data));
    }

    public void signal(ActivityExecution execution, String signalName, Object signalData) {
        leave(execution);
    }
}
