package com.swp.ihelp.app.event.request;

import com.swp.ihelp.app.account.AccountEntity;
import com.swp.ihelp.app.entity.StatusEntity;
import com.swp.ihelp.app.event.EventEntity;
import com.swp.ihelp.app.eventcategory.EventCategoryEntity;
import com.swp.ihelp.app.image.ImageEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class EventRequest {
    private String id;

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @NotBlank(message = "Location is required.")
    private String location;

    @Min(0)
    private int quota;

    @Min(0)
    private int point;

    @Min(0)
    private long startDate;

    @Min(0)
    private long endDate;

    @NotBlank(message = "Author email is required.")
    private String authorEmail;

    @NotNull(message = "Status ID cannot be null.")
    private int statusId;

    @NotNull(message = "Event Category ID cannot be null.")
    private int categoryId;

    private List<ImageEntity> images;

    public static EventEntity convertToEntity(EventRequest request) {
        AccountEntity authorAccount = new AccountEntity().setEmail(request.getAuthorEmail());
        StatusEntity serviceStatus = new StatusEntity().setId(request.getStatusId());
        EventCategoryEntity eventCategory = new EventCategoryEntity().setId(request.getCategoryId());
        return new EventEntity()
                .setTitle(request.getTitle())
                .setDescription(request.getDescription())
                .setLocation(request.getLocation())
                .setQuota(request.getQuota())
                .setPoint(request.getPoint())
                .setCreatedDate(System.currentTimeMillis())
                .setStartDate(request.getStartDate())
                .setEndDate(request.getEndDate())
                .setAuthorAccount(authorAccount)
                .setStatus(serviceStatus)
                .setEventCategory(eventCategory);
    }
}
