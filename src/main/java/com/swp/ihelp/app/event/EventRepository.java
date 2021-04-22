package com.swp.ihelp.app.event;

import com.swp.ihelp.app.event.response.EventHostedReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, String>, JpaSpecificationExecutor<EventEntity> {

    @Query("SELECT e from EventEntity e where e.title like %:title%")
    Page<EventEntity> findByTitle(String title, Pageable pageable);

    @Query(value = "SELECT t1.* from ihelp.event t1 " +
            "INNER JOIN event_category_has_event t2 " +
            "ON t1.id = t2.event_id AND t2.event_category_id = :categoryId", nativeQuery = true)
    Page<EventEntity> findByCategoryId(int categoryId, Pageable pageable);

    @Query("SELECT e from EventEntity e where e.status.id = :statusId")
    Page<EventEntity> findByStatusId(int statusId, Pageable pageable);

    Page<EventEntity> findAllByEventCategories(int categoryId, Specification<EventEntity> specs, Pageable pageable);

    @Query("SELECT e from EventEntity e where e.authorAccount.email = :email")
    Page<EventEntity> findByAuthorEmail(String email, Pageable pageable);

    @Query("SELECT e.event from EventHasAccountEntity e where e.account.email = :email and e.event.status.id = :statusId")
    Page<EventEntity> findByParticipantEmail(String email, int statusId, Pageable pageable);

    @Query("SELECT count(e.event.authorAccount) from EventHasAccountEntity e where e.event.id = :eventId")
    Integer getSpotUsed(String eventId);

    @Query("SELECT e.startDate From EventEntity e")
    List<Timestamp> getAllStartDates();

    @Query(value =
            "SELECT (e.quota - (SELECT count(account_email) " +
                    "            FROM ihelp.event_has_account " +
                    "            Where event_id = :eventId)) AS RemainingSpot " +
                    "FROM ihelp.event e " +
                    "Where e.id = :eventId ", nativeQuery = true)
    Integer getRemainingSpot(String eventId);

    @Query("SELECT e.quota from EventEntity e where e.id = :eventId")
    Integer getQuota(String eventId);

    @Modifying
    @Query(value = "UPDATE ihelp.event e Set e.status_id = :statusId Where e.id = :eventId ", nativeQuery = true)
    void updateStatus(String eventId, int statusId);

    @Query("SELECT DISTINCT 1 " +
            "FROM EventEntity e " +
            "INNER JOIN EventHasAccountEntity ea on e.id = ea.event.id " +
            "WHERE e.startDate <= :date " +
            "AND e.endDate >= :date " +
            "AND ea.account.email = :email ")
    Integer isAccountJoinedAnyEvent(Date date, String email);

//    @Query(value = "SELECT e.id, e.end_date FROM ihelp.event e WHERE e.end_date <= :date " +
//            "ORDER BY e.end_date DESC Limit 1 ", nativeQuery = true)
//    Object[] getNearestEventEndDate(String date);
//
//    @Query(value = "SELECT e.id, e.start_date FROM ihelp.event e WHERE e.start_date >= :date " +
//            "ORDER BY e.start_date ASC Limit 1 ", nativeQuery = true)
//    Object[] getNearestEventStartDate(String date);

    @Query("SELECT COUNT(e.id.accountEmail) FROM EventHasAccountEntity e WHERE e.id.accountEmail=:email")
    Integer getTotalJoinedEvents(String email);

    @Query("SELECT COUNT(e) FROM EventEntity e WHERE e.authorAccount.email=:email")
    Integer getTotalHostEvents(String email);

    @Query(value = "SELECT e.id, " +
            "( " +
            "   6371 * " +
            "   acos(cos(radians(:lat)) *  " +
            "   cos(radians(e.lat)) *  " +
            "   cos(radians(e.lng) -  " +
            "   radians(:lng)) +  " +
            "   sin(radians(:lat)) *  " +
            "   sin(radians(e.lat ))) " +
            ") AS distance  " +
            "FROM ihelp.event e " +
            "WHERE e.status_id = :statusId " +
            "HAVING distance < :radius " +
            "ORDER BY distance ", nativeQuery = true)
    Page<Object[]> getNearbyEvents(float radius, double lat, double lng, int statusId, Pageable pageable);

    @Query(value = "SELECT e.id " +
            "FROM event e " +
            "JOIN event_has_account ea ON e.id = ea.event_id " +
            "WHERE e.status_id = 4 AND ea.is_evaluated <> 1 AND e.account_email=:email " +
            "GROUP BY e.id", nativeQuery = true)
    List<String> findEvaluateRequiredByAuthorEmail(String email);

    @Query(value = "SELECT e.id FROM ihelp.event e " +
            "WHERE Date(e.start_date) = :date AND e.status_id = :status", nativeQuery = true)
    List<String> getEventIdsToStartByDate(String date, int status);

    @Query(value = "SELECT e.id FROM ihelp.event e " +
            "WHERE Date(e.end_date) = :date AND e.status_id = :status", nativeQuery = true)
    List<String> getEventIdsToCompleteByDate(String date, int status);

    @Query(value = "SELECT e.id FROM ihelp.event e " +
            "where datediff(:date, e.created_date) > :maxDaysToApprove and e.status_id = :status ", nativeQuery = true)
    List<String> getExpiredEventIds(@Param("date") String date, @Param("status") int status,
                                    @Param("maxDaysToApprove") int maxDaysToApprove);

    @Query(value = "SELECT e.id FROM ihelp.event e " +
            "WHERE date(e.start_date) = :date " +
            "AND e.id NOT IN (SELECT event_id FROM ihelp.event_has_account) ", nativeQuery = true)
    List<String> getEmptyEventIds(@Param("date") String date);

    @Query(value = "SELECT Month(e.start_date) as month, Count(e.id) as count " +
            "FROM ihelp.event e  " +
            "WHERE Year(e.start_date) = :year " +
            "GROUP BY month " +
            "ORDER BY month ", nativeQuery = true)
    List<EventHostedReport> getMonthlyHostedEventNumber(int year);
}

