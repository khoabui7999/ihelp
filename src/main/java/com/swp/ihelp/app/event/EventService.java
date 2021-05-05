package com.swp.ihelp.app.event;

import com.swp.ihelp.app.event.request.CreateEventRequest;
import com.swp.ihelp.app.event.request.EvaluationRequest;
import com.swp.ihelp.app.event.request.RejectEventRequest;
import com.swp.ihelp.app.event.request.UpdateEventRequest;
import com.swp.ihelp.app.event.response.EventDetailResponse;

import java.util.List;
import java.util.Map;

public interface EventService {
    Map<String, Object> findAll(int page) throws Exception;

    Map<String, Object> findAll(int page, String search) throws Exception;

    EventDetailResponse findById(String id) throws Exception;

    Map<String, Object> findByTitle(String title, int page) throws Exception;

    Map<String, Object> findTitleById(String eventId) throws Exception;

    String insert(CreateEventRequest event) throws Exception;

    EventDetailResponse update(UpdateEventRequest event) throws Exception;

    void deleteById(String id) throws Exception;

    Map<String, Object> findByCategoryId(int categoryId, int page) throws Exception;

    Map<String, Object> findByStatusId(int statusId, int page) throws Exception;

    Map<String, Object> findByAuthorEmail(String email, int page) throws Exception;

    Map<String, Object> findByParticipantEmail(String email, Integer statusId, int page) throws Exception;

    boolean hasParticipants(String eventId) throws Exception;

    void joinEvent(String email, String eventId) throws Exception;

    void updateStatus(String eventId, int statusId) throws Exception;

    void disableEvent(String eventId) throws Exception;

    String enableEvent(String eventId) throws Exception;

    EventEntity approve(String eventId, String managerEmail) throws Exception;

    EventEntity reject(RejectEventRequest request) throws Exception;

    void evaluateMember(EvaluationRequest request) throws Exception;

    void quitEvent(String eventId, String email) throws Exception;

    Map<String, Object> findNearbyEvents(int page, float radius, double lat, double lng) throws Exception;

    List<String> findEvaluateRequiredByAuthorEmail(String email) throws Exception;

    Map<Integer, Integer> getMonthlyHostedEventNumber(int year) throws Exception;

    Boolean isUserHasEnoguhPoint(String email, String eventId) throws Exception;
}
