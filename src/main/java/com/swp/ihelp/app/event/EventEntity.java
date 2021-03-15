package com.swp.ihelp.app.event;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.swp.ihelp.app.account.AccountEntity;
import com.swp.ihelp.app.eventcategory.EventCategoryEntity;
import com.swp.ihelp.app.eventjointable.EventHasAccountEntity;
import com.swp.ihelp.app.image.ImageEntity;
import com.swp.ihelp.app.status.StatusEntity;
import com.swp.ihelp.config.StringPrefixedSequenceIdGenerator;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "event", schema = "ihelp")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Accessors(chain = true)
@DynamicUpdate
public class EventEntity {
    // ID format: EV_0000x
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_seq")
    @GenericGenerator(
            name = "event_seq",
            strategy = "com.swp.ihelp.config.StringPrefixedSequenceIdGenerator",
            parameters = {
                    @Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "50"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "EV_"),
                    @Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
            }
    )
    private String id;

    @Basic
    @Column(name = "title", nullable = true, length = 100)
    private String title;

    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    private String description;

    @Basic
    @Column(name = "location", nullable = true, length = 300)
    private String location;

    @Basic
    @Column(name = "quota", nullable = true)
    private int quota;

    @Basic
    @Column(name = "point", nullable = true)
    private int point;

    @Basic
    @Column(name = "created_date", nullable = true)
    private long createdDate;

    @Basic
    @Column(name = "start_date", nullable = true)
    private long startDate;

    @Basic
    @Column(name = "end_date", nullable = true)
    private long endDate;

    @Basic
    @Column(name = "is_onsite", nullable = false)
    private boolean isOnsite;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_email", referencedColumnName = "email", nullable = false)
    private AccountEntity authorAccount;

    @ManyToOne
    @JoinColumn(name = "event_category_id", referencedColumnName = "id", nullable = false)
    private EventCategoryEntity eventCategory;

    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
    private StatusEntity status;

    @OneToMany(
            fetch = FetchType.LAZY,
            mappedBy = "event",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH}
    )
    @Getter(value = AccessLevel.NONE)
    private Set<EventHasAccountEntity> eventAccount = new HashSet<>();

    //Methods to add and remove EventHasAccount to avoid infinite loop.
    public Set<EventHasAccountEntity> getEventAccount() {
        return Collections.unmodifiableSet(eventAccount);
    }

    public void addEventAccount(EventHasAccountEntity eventHasAccount) {
        eventHasAccount.setEvent(this);
    }

    public void removeEventAccount(EventHasAccountEntity eventHasAccount) {
        eventHasAccount.setEvent(null);
    }

    public void internalAddEventAccount(EventHasAccountEntity eventHasAccount) {
        eventAccount.add(eventHasAccount);
    }

    public void internalRemoveEventAccount(EventHasAccountEntity eventHasAccount) {
        eventAccount.remove(eventHasAccount);
    }

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(name = "event_has_image",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id"))
    private List<ImageEntity> images = new ArrayList<>();

    public void addImage(ImageEntity imageEntity) {
        if (images.contains(imageEntity)) {
            return;
        }
        images.add(imageEntity);
    }
}

