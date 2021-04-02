package com.swp.ihelp.app.feedback.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swp.ihelp.app.feedback.FeedbackEntity;
import com.swp.ihelp.app.status.StatusEntity;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Data
public class FeedbackResponse implements Serializable {

    private String id;

    private Integer rating;

    private String comment;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho-Chi-Minh")
    private Timestamp createdDate;

    private String email;

    private String eventId;

    private String serviceId;

    private StatusEntity status;

    public FeedbackResponse(FeedbackEntity feedbackEntity) {
        this.id = feedbackEntity.getId();
        this.rating = feedbackEntity.getRating();
        this.comment = feedbackEntity.getComment();
        this.createdDate = feedbackEntity.getCreatedDate();
        this.email = feedbackEntity.getAccount().getEmail();
        this.eventId = feedbackEntity.getEvent().getId();
        this.serviceId = feedbackEntity.getService().getId();
        this.status = feedbackEntity.getStatus();
    }

    public static List<FeedbackResponse> convertToListResponse(List<FeedbackEntity> entityList) {
        List<FeedbackResponse> result = new ArrayList<>();
        for (FeedbackEntity entity : entityList) {
            result.add(new FeedbackResponse(entity));
        }
        return result;
    }

}