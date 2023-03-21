package xyz.chener.zp.zpusermodule.ws;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalNotification;
import jakarta.websocket.Session;
import xyz.chener.zp.zpusermodule.ws.entity.WsClient;

import java.time.Duration;
import java.util.Collection;

/**
 * @Author: chenzp
 * @Date: 2023/03/14/16:40
 * @Email: chen@chener.xyz
 */
public class WsCache {

    public static Cache<String, WsClient> unAuthConnect;
    public static Cache<String, WsClient> authConnect;

    static {
        unAuthConnect = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(10))
                .removalListener(WsCache::removeListener)
                .build();
        authConnect = CacheBuilder.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(60))
                .removalListener(WsCache::removeListener)
                .build();
        Thread t = new Thread(()->{
            while (!Thread.interrupted()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                unAuthConnect.cleanUp();
                authConnect.cleanUp();
            }
        });
        t.setDaemon(true);
        t.setName("websocet cache flush thread");
        t.start();
    }


    public static void putUnAuthConnect(String sessionId, WsClient wsClient){
        unAuthConnect.put(sessionId, wsClient);
    }
    public static WsClient getUnAuthConnect(String sessionId){
        return unAuthConnect.getIfPresent(sessionId);
    }

    public static void putAuthConnect(String sessionId, WsClient wsClient){
        authConnect.put(sessionId, wsClient);
    }

    public static WsClient getAuthConnect(String sessionId){
        return authConnect.getIfPresent(sessionId);
    }

    public static Collection<WsClient> getAllAuthConnect(){
        return authConnect.asMap().values();
    }

    public static void removeConnect(String sessionId){
        unAuthConnect.invalidate(sessionId);
        authConnect.invalidate(sessionId);
    }

    private static void removeListener(RemovalNotification<String, WsClient> notification){
        if (notification.getCause().toString().equals("EXPIRED") && notification.getValue() != null) {
            closeConnect(notification.getValue().getSessionId(), notification.getValue().getSession());
        }
    }


    public static void closeConnect(String sessionId, Session session)  {
        try {
            WsConnector.close(session);
        } catch (Exception ignored) { }
    }

}