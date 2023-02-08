package com.hcmus.lovelybackend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
@Slf4j
public class LoggingService {
    private static final String REQUEST_ID = "request_id";
    private static final ObjectMapper mapper = new ObjectMapper();

    @SneakyThrows
    public void logRequest(HttpServletRequest httpServletRequest, Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        var data = "\n-----------------------------------\n" +
                "[REQUEST-ID]: " + requestId + "\n" +
                "[BODY REQUEST]: " + "\n\n" +
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body) +
                "\n\n" +
                "-----------------------------------\n";

        log.info(data);
    }

    @SneakyThrows
    public void logResponse(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                            Object body) {
        if (httpServletRequest.getRequestURI().contains("medias")) {
            return;
        }
        Object requestId = httpServletRequest.getAttribute(REQUEST_ID);
        var data = "\n-----------------------------------\n" +
                "[REQUEST-ID]: " + requestId + "\n" +
                "[BODY RESPONSE]: " + "\n" +
                mapper.writerWithDefaultPrettyPrinter().writeValueAsString(body) +
                "\n" +
                "-----------------------------------\n";

        log.info(data);
    }
}
