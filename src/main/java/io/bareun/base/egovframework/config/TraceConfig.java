package io.bareun.base.egovframework.config;

import org.egovframe.rte.fdl.cmmn.trace.LeaveaTrace;
import org.egovframe.rte.fdl.cmmn.trace.handler.DefaultTraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.handler.TraceHandler;
import org.egovframe.rte.fdl.cmmn.trace.manager.DefaultTraceHandleManager;
import org.egovframe.rte.fdl.cmmn.trace.manager.TraceHandlerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

/**
 * 트레이스 설정을 위한 구성 클래스입니다.
 * eGovFrame의 트레이스 핸들러와 매니저를 설정합니다.
 */
@Configuration
@ComponentScan(basePackages = "egovframework", includeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, value = Service.class),
        @ComponentScan.Filter(type = ANNOTATION, value = Repository.class)
}, excludeFilters = {
        @ComponentScan.Filter(type = ANNOTATION, value = Controller.class),
        @ComponentScan.Filter(type = ANNOTATION, value = Configuration.class)
})
public class TraceConfig {

    /**
     * AntPathMatcher 빈을 생성합니다.
     *
     * @return AntPathMatcher 객체
     */
    @Bean
    protected AntPathMatcher antPathMatcher() {
        return new AntPathMatcher();
    }

    /**
     * DefaultTraceHandler 빈을 생성합니다.
     *
     * @return DefaultTraceHandler 객체
     */
    @Bean
    protected DefaultTraceHandler defaultTraceHandler() {
        return new DefaultTraceHandler();
    }

    /**
     * DefaultTraceHandleManager 빈을 생성하고 설정합니다.
     *
     * @return DefaultTraceHandleManager 객체
     */
    @Bean
    protected DefaultTraceHandleManager traceHandlerService() {
        DefaultTraceHandleManager defaultTraceHandleManager = new DefaultTraceHandleManager();
        defaultTraceHandleManager.setReqExpMatcher(antPathMatcher());
        defaultTraceHandleManager.setPatterns(new String[]{"*"});
        defaultTraceHandleManager.setHandlers(new TraceHandler[]{defaultTraceHandler()});
        return defaultTraceHandleManager;
    }

    /**
     * LeaveaTrace 빈을 생성하고 설정합니다.
     *
     * @return LeaveaTrace 객체
     */
    @Bean
    protected LeaveaTrace leaveaTrace() {
        LeaveaTrace leaveaTrace = new LeaveaTrace();
        leaveaTrace.setTraceHandlerServices(new TraceHandlerService[]{traceHandlerService()});
        return leaveaTrace;
    }
}