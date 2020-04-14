package com.example.musicplayer.handlererror;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

@Service
public class ExceptionHandlerService {
    String differentStatusCode(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/error404.html";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "error/error500.html";
            } else if (statusCode == HttpStatus.METHOD_NOT_ALLOWED.value()) {
                return "error/error405.html";
            } else if(statusCode == HttpStatus.FORBIDDEN.value()){
                return "error/error403.html";
            }
        }
        return "error/error404.html";
    }
}
