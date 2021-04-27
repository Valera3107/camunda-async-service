package com.layo.kafkaexample.service.common;

import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class MessageQueue {
  
  public static class Message {
    
    protected Map<String, Object> data;

    public Message(Map<String, Object> data) {
      this.data = data;
    }
    
    public Map<String, Object> getData() {
      return data;
    }
  }
  
  protected List<Message> queue = new LinkedList<>();

  public void send(Message m) {
    queue.add(m);
  }
  
  public Message getNextMessage() {
    return queue.remove(0);
  }
}