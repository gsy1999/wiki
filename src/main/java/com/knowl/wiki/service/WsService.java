package com.knowl.wiki.service;

import com.knowl.wiki.websocket.WebSocketServer;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class WsService {

    @Resource
    public WebSocketServer webSocketServer;

    @Async
    public void sendInfo(String message,String logId) {
        MDC.put("LOG_ID", logId); //把流水号放到线程中
        webSocketServer.sendInfo(message);
    }

    /**
     * 因为@Async会为类生成一个代理类，所以必须要放到另外一个类里，不然直接在DocService里方法互相调用不起作用
     */
}
