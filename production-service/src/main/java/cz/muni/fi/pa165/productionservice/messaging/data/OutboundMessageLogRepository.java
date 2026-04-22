package cz.muni.fi.pa165.productionservice.messaging.data;

import cz.muni.fi.pa165.productionservice.messaging.data.entity.OutboundMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OutboundMessageLogRepository extends JpaRepository<OutboundMessageLog, UUID> {
}
