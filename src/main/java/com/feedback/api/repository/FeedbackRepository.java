package com.feedback.api.repository;

import com.feedback.api.enums.FeedbackStatus;
import com.feedback.api.model.Feedback;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface FeedbackRepository extends PagingAndSortingRepository<Feedback, Long> {

  List<Feedback> findAllByStoreIdAndStatus(String store, FeedbackStatus feedbackStatus);

  Page<Feedback> findByBuyerIdAndCreatedDateBetween(
      Long buyer, Date from, Date to, Pageable pageable);

  Page<Feedback> findByStoreIdAndCreatedDateBetween(
      String storeId, Date from, Date to, Pageable pageable);

  @Query("select case when count(f)>0 then true else false end from Feedback f where f.buyerId = ?1")
  boolean existsBuyerId(Long buyer);

  @Query("select case when count(f)>0 then true else false end from Feedback f where f.storeId = ?1")
  boolean existsStoreId(String storeId);
}
