package com.yo1000.postcode.application;

import com.yo1000.postcode.config.NodeProperties;
import com.yo1000.postcode.domain.vo.NodeIdHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;

@Service
public class TaskApplicationService {
    private final PostApplicationService postApplicationService;
    private final NodeApplicationService nodeApplicationService;
    private final NodeProperties nodeProps;
    private final NodeIdHolder nodeIdHolder;
    private final Clock clock;

    private final Logger logger = LoggerFactory.getLogger(TaskApplicationService.class);

    public TaskApplicationService(
            PostApplicationService postApplicationService,
            NodeApplicationService nodeApplicationService,
            NodeProperties nodeProps,
            NodeIdHolder nodeIdHolder,
            Clock clock) {
        this.postApplicationService = postApplicationService;
        this.nodeApplicationService = nodeApplicationService;
        this.nodeProps = nodeProps;
        this.nodeIdHolder = nodeIdHolder;
        this.clock = clock;
    }

    public void updatePosts() {
        long execTime = clock.millis();

        logger.info("Node={} Time={} | Starting table updates.", nodeIdHolder.value(), execTime);
        if (!nodeApplicationService.exists()) {
            nodeApplicationService.init().ifPresent(waitTime -> {
                logger.info("Node={} Time={} | Sleep {}-millis.", nodeIdHolder.value(), execTime, waitTime.millis());
                waitTime.sleep();
            });
            nodeApplicationService.register(execTime);
        }
        nodeApplicationService.rank(execTime).ifPresent(waitTime -> {
            logger.info("Node={} Time={} | Sleep {}-millis.", nodeIdHolder.value(), execTime, waitTime.millis());
            waitTime.sleep();
        });
        postApplicationService.update(execTime);
        logger.info("Node={} Time={} | Ending table updates.", nodeIdHolder.value(), execTime);

        logger.info("Node={} Time={} | Starting table purges.", nodeIdHolder.value(), execTime);
        postApplicationService.delete(nodeProps.getGenerations());
        nodeApplicationService.cleanup(execTime);
        logger.info("Node={} Time={} | Ending table purges.", nodeIdHolder.value(), execTime);
    }
}
