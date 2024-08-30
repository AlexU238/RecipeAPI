package com.u238.recipeApi.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class StringTransformer {

    public static String asJsonString(Object o) {
        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cannot transform object " + o + " into JSON for the purposes of the test.", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected exception while transforming object " + o + " to JSON", e);
        }
    }
}
