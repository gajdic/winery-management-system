package cz.muni.fi.pa165.commonservice.messaging.data;

import cz.muni.fi.pa165.commonservice.messaging.data.entity.InboundMessageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface InboundMessageLogRepository extends JpaRepository<InboundMessageLog, UUID> {
}
