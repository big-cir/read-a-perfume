package io.perfume.api.common.config.datasourece;

@Deprecated
public class RoutingDataSource {

  //    extends AbstractRoutingDataSource {
  //
  //    private final Logger log = LoggerFactory.getLogger(getClass());
  //
  //    @Override
  //    protected Object determineCurrentLookupKey() {
  //        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
  //
  //        if (readOnly) {
  //            log.info("GET: Slave DB");
  //            return DataSourceType.SLAVE;
  //        }
  //
  //        log.info("Master DB");
  //        return DataSourceType.MASTER;
  //    }
}
