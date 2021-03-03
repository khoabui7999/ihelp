package com.swp.ihelp.app.service.response;

import com.swp.ihelp.app.entity.StatusEntity;
import com.swp.ihelp.app.service.ServiceEntity;
import com.swp.ihelp.app.servicetype.ServiceTypeEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDetailResponse implements Serializable {
    private String id;
    private String title;
    private String description;
    private String location;
    private int point;
    private int quota;
    private int spot;
    private long createdDate;
    private long startDate;
    private long endDate;
    private String accountEmail;
    private StatusEntity status;
    private ServiceTypeEntity serviceType;


    public ServiceDetailResponse(ServiceEntity service) {
        this.id = service.getId();
        this.title = service.getTitle();
        this.description = service.getDescription();
        this.location = service.getLocation();
        this.point = service.getPoint();
        this.quota = service.getQuota();
        this.createdDate = service.getCreatedDate();
        this.startDate = service.getStartDate();
        this.endDate = service.getEndDate();
        this.accountEmail = service.getAuthorAccount().getEmail();
        this.status = service.getStatus();
        this.serviceType = service.getServiceType();
    }

    public static List<ServiceDetailResponse> convertToResponseList(List<ServiceEntity> serviceEntityList) {
        List<ServiceDetailResponse> responseList = new ArrayList<>();
        for (ServiceEntity serviceEntity : serviceEntityList) {
            responseList.add(new ServiceDetailResponse(serviceEntity));
        }
        return responseList;
    }
}