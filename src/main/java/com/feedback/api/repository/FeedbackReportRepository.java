package com.feedback.api.repository;

import com.feedback.api.model.FeedbackReport;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackReportRepository extends CrudRepository<FeedbackReport, String> {}
