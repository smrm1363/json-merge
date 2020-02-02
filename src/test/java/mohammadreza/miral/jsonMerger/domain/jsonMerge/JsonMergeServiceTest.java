package mohammadreza.miral.jsonMerger.domain.jsonMerge;

import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class JsonMergeServiceTest {

    private JsonMergeService jsonMergeService;
    @Before
    public void setUp() {
        jsonMergeService = new JsonMergeService("","");
    }

    @Test
    public void mergeJsons()
    {
        Map<String,Object>  mainForMergeMap = new HashMap<>();
        mainForMergeMap.put("id",1);
        mainForMergeMap.put("name","something");
        List<Map<String,Object>> toBeMergedList = new ArrayList<>();
        Map<String,Object> firstInList=new HashMap<>();
        firstInList.put("userID","0");
        Map<String,Object> secondInList=new HashMap<>();
        secondInList.put("userID","1");
        toBeMergedList.add(firstInList);
        toBeMergedList.add(secondInList);
        Map<String,Object> result =jsonMergeService.mergeJsons(mainForMergeMap,toBeMergedList,"userID","innerList");
        assertTrue(result.containsKey("innerList"));
        assertTrue(result.get("innerList") instanceof List);
    }
}