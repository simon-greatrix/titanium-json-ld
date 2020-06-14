package com.apicatalog.jsonld.lang;

import java.util.Arrays;

import javax.json.JsonValue;

import com.apicatalog.jsonld.json.JsonUtils;

public class NodeObject {

    NodeObject() {
    }

    public static final boolean isNodeObject(JsonValue value) {
        return JsonUtils.isObject(value)
                    && ((!value.asJsonObject().containsKey(Keywords.VALUE)
                                && !value.asJsonObject().containsKey(Keywords.LIST) 
                                && !value.asJsonObject().containsKey(Keywords.SET))
                            
                        || value.asJsonObject().keySet().stream().allMatch(Arrays.asList(Keywords.CONTEXT, Keywords.GRAPH)::contains)
                        )
        ;
        // TODO https://www.w3.org/TR/json-ld11/#dfn-node-object
    }
    
    public static final boolean isNodeReference(JsonValue value) {
        return JsonUtils.isObject(value) 
                    && value.asJsonObject().size() == 1 
                    && value.asJsonObject().containsKey(Keywords.ID);        
    }
    
}
