CREATE INDEX feedback_created_date_idx ON feedback (created_date);
CREATE INDEX feedback_store_id_and_created_date_idx ON feedback (store_id, created_date);
CREATE INDEX feedback_store_id_idx ON feedback (store_id);
CREATE INDEX feedback_buyer_id_and_created_date_idx ON feedback (buyer_id, created_date);