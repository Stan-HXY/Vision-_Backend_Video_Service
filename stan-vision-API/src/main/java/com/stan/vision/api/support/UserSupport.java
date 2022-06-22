package com.stan.vision.api.support;

import com.stan.vision.domain.exception.ConditionException;
import com.stan.vision.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {

    public Long getCurrentUserID(){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userID = TokenUtil.verifyToken(token);
        if(userID < 0){
            throw new ConditionException("Invalid UserID!");
        }
        return userID;
    }

}
