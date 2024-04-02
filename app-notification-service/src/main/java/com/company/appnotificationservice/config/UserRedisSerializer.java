package com.company.appnotificationservice.config;

import com.company.appnotificationservice.dto.UserDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

@Component
public class UserRedisSerializer extends GenericJackson2JsonRedisSerializer {
    public UserRedisSerializer() {
        super(new ObjectMapper()
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false));
    }

    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        return super.deserialize(source, UserDto.class);
    }

}
