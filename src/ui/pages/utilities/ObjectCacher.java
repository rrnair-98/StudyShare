package ui.pages.utilities;

/*This will cache the specified objects for future use*/

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ObjectCacher {

    private static ObjectCacher objectCacher;

    private Map<Class,Object> map;

    private ObjectCacher(){
        map= Collections.synchronizedMap(new HashMap<Class,Object>());
    }

    public static ObjectCacher getObjectCacher(){
        if(objectCacher==null)
            return (objectCacher=new ObjectCacher());
        return objectCacher;
    }

    public void put(Class key,Object value){
        map.put(key,value);
    }

    public Object get(Class key){
        return map.get(key);
    }
}
