package com.swp.ihelp.scheduler;

import com.swp.ihelp.app.event.EventEntity;
import com.swp.ihelp.app.event.EventRepository;
import com.swp.ihelp.app.status.StatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CountDownLatch;

public class EventTask implements Runnable {
    private final CountDownLatch completion;

    private String eventId;

    private boolean isStartDate;

    @Autowired
    private EventRepository eventRepository;


    public EventTask(CountDownLatch completion) {
        this.completion = completion;
    }

    public EventTask(CountDownLatch completion, String eventId, boolean isStartDate) {
        this.completion = completion;
        this.eventId = eventId;
        this.isStartDate = isStartDate;
    }

    @Override
    @Async("eventTaskExecutor")
    public void run() {
        System.out.println("Updating event " + eventId + " ...");
        try {
            if (isStartDate) {
                startEvent();
            } else {
                completeEvent();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        completion.countDown();
    }

    private void startEvent() throws InterruptedException {
        EventEntity eventToStart = eventRepository.getOne(eventId);
        if (eventToStart.getStatus().getId().equals(StatusEnum.APPROVED.getId())) {
            eventRepository.updateStatus(eventId, StatusEnum.ONGOING.getId());
        }

    }

    private void completeEvent() throws InterruptedException {
        EventEntity eventToStart = eventRepository.getOne(eventId);
        if (eventToStart.getStatus().getId().equals(StatusEnum.ONGOING.getId())) {
            eventRepository.updateStatus(eventId, StatusEnum.COMPLETED.getId());
        }
    }
}
