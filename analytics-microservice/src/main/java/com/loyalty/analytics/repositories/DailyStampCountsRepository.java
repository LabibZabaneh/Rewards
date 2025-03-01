package com.loyalty.analytics.repositories;

import com.loyalty.analytics.domain.DailyStampCount;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

@Repository
public interface DailyStampCountsRepository extends CrudRepository<DailyStampCount, Long>{
}
