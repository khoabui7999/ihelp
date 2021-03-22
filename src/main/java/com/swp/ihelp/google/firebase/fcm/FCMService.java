package com.swp.ihelp.google.firebase.fcm;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMService {

    public void subscribeToTopic(SubscriptionRequest subscriptionRequest) {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(subscriptionRequest.getTokens(), subscriptionRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public void unsubscribeFromTopic(SubscriptionRequest subscriptionRequest) {
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(subscriptionRequest.getTokens(), subscriptionRequest.getTopicName());
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }

    public String sendNotificationToTopic(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage())
                .build();

        Message message = Message.builder()
                .setTopic(notificationRequest.getTopic())
                .setNotification(notification)
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .build();

        String response = null;

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        return response;
    }

    public String sendNotificationToDevice(NotificationRequest notificationRequest) {
        Notification notification = Notification.builder()
                .setTitle(notificationRequest.getTitle())
                .setBody(notificationRequest.getMessage())
                .build();

        Message message = Message.builder()
                .setToken(notificationRequest.getToken())
                .setNotification(notification)
                .putData("content", notificationRequest.getTitle())
                .putData("body", notificationRequest.getMessage())
                .build();

        String response = null;

        try {
            FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }

        return response;
    }

    public void sendMessageToToken(NotificationRequest request) throws ExecutionException, InterruptedException {
        Message message = getPreconfiguredMessageToToken(request);
        String response = sendAndGetResponse(message);
    }

    public void sendMessageToTopic(NotificationRequest request) throws ExecutionException, InterruptedException {
        Message message = getPreconfiguredMessageToTopic(request);
        String response = sendAndGetResponse(message);
    }

    public void sendMessageWithData(Map<String, String> data, NotificationRequest request) throws InterruptedException, ExecutionException {
        Message message = getPreconfiguredMessageWithData(data, request);
        String response = sendAndGetResponse(message);
    }

    private String sendAndGetResponse(Message message) throws InterruptedException, ExecutionException {
        return FirebaseMessaging.getInstance().sendAsync(message).get();
    }

    private Message getPreconfiguredMessageToToken(NotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setToken(request.getToken()).build();
    }

    private Message getPreconfiguredMessageToTopic(NotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).setTopic(request.getTopic()).build();
    }

    private Message getPreconfiguredMessageWithData(Map<String, String> data, NotificationRequest request) {
        return getPreconfiguredMessageBuilder(request).putAllData(data).setTopic(request.getTopic()).build();
    }

    private Message.Builder getPreconfiguredMessageBuilder(NotificationRequest request) {
        AndroidConfig androidConfig = getAndroidConfig(request.getTopic());
        ApnsConfig apnsConfig = getApnsConfig(request.getTopic());
        Notification notification = Notification.builder()
                .setTitle(request.getTitle())
                .setBody(request.getMessage())
                .build();
        return Message.builder()
                .setApnsConfig(apnsConfig).setAndroidConfig(androidConfig)
                .setNotification(notification);
    }

    private AndroidConfig getAndroidConfig(String topic) {
        return AndroidConfig.builder()
                .setTtl(Duration.ofMinutes(2).toMillis())
                .setCollapseKey(topic)
                .setPriority(AndroidConfig.Priority.HIGH)
                .setNotification(AndroidNotification.builder().setSound(NotificationParameter.SOUND.getValue())
                .setColor(NotificationParameter.COLOR.getValue()).setTag(topic).build()).build();
    }

    private ApnsConfig getApnsConfig(String topic) {
        return ApnsConfig.builder()
                .setAps(Aps.builder().setCategory(topic).setThreadId(topic).build()).build();
    }

}