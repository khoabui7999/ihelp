package com.swp.ihelp.app.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, String> {
    @Query("SELECT e from EventEntity e order by e.status.id")
    Page<EventEntity> findAll(Pageable pageable);

    @Query("SELECT e from EventEntity e where e.title like %:title%")
    Page<EventEntity> findByTitle(String title, Pageable pageable);

    @Query(value = "SELECT t1.* from ihelp.event t1 " +
            "INNER JOIN event_category_has_event t2 " +
            "ON t1.id = t2.event_id AND t2.event_category_id = :categoryId", nativeQuery = true)
    Page<EventEntity> findByCategoryId(int categoryId, Pageable pageable);

    @Query("SELECT e from EventEntity e where e.status.id = :statusId")
    Page<EventEntity> findByStatusId(int statusId, Pageable pageable);

    @Query("SELECT e from EventEntity e where e.authorAccount.email = :email")
    Page<EventEntity> findByAuthorEmail(String email, Pageable pageable);

    @Query("SELECT e.event from EventHasAccountEntity e where e.account.email = :email and e.event.status.id = :statusId")
    Page<EventEntity> findByParticipantEmail(String email, int statusId, Pageable pageable);

    @Query("SELECT count(e.event.authorAccount) from EventHasAccountEntity e where e.event.id = :eventId")
    int getSpotUsed(String eventId);

    @Query("SELECT e.startDate From EventEntity e")
    List<Timestamp> getAllStartDates();

    @Query(value =
            "SELECT (e.quota - (SELECT count(account_email) " +
                    "            FROM ihelp.event_has_account " +
                    "            Where event_id = :eventId)) AS RemainingSpot " +
                    "FROM ihelp.event e " +
                    "Where e.id = :eventId ", nativeQuery = true)
    int getRemainingSpot(String eventId);

    @Query("SELECT e.quota from EventEntity e where e.id = :eventId")
    int getQuota(String eventId);

    @Modifying
    @Query(value = "UPDATE ihelp.event e Set e.status_id = :statusId Where e.id = :eventId ", nativeQuery = true)
    void updateStatus(String eventId, int statusId);

    @Query(value = "SELECT e.end_date FROM ihelp.event e WHERE e.end_date <= :date " +
            "AND e.account_email = :email " +
            "ORDER BY e.end_date DESC Limit 1 ", nativeQuery = true)
    Date getNearestEventEndDate(String email, String date);

    @Query(value = "SELECT e.start_date FROM ihelp.event e WHERE e.start_date >= :date " +
            "AND e.account_email = :email " +
            "ORDER BY e.start_date ASC Limit 1 ", nativeQuery = true)
    Date getNearestEventStartDate(String email, String date);

}

