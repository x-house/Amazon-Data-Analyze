package edu.tongji.sse.DataWarehouse.Controller;

import edu.tongji.sse.DataWarehouse.Model.MySQLModel.Movie;
import edu.tongji.sse.DataWarehouse.Service.Hive.HiveCheckService;
import edu.tongji.sse.DataWarehouse.Service.Hive.HiveTimeService;
import edu.tongji.sse.DataWarehouse.Service.MySQL.MySQLCheckService;
import edu.tongji.sse.DataWarehouse.Service.MySQL.MySQLTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/time")
public class TimeController {

    @Autowired
    private MySQLTimeService mySQLTimeService;

    @Autowired
    private MySQLCheckService mySQLCheckService;

    @Autowired
    private HiveTimeService hiveTimeService;

    @Autowired
    private HiveCheckService hiveCheckService;

    @GetMapping("/index")
    public Object getMoviesByTime(@RequestParam(value = "time", defaultValue = "")String time){
        Map<String, Object> result = new HashMap<>();
        long start_mysql = System.currentTimeMillis();
        List<Movie> movies = mySQLTimeService.getMoviesByTime(time);
        if(movies == null || movies.size() == 0)
            result.put("data", new ArrayList<>());
        else
            result.put("data", mySQLCheckService.generateMovieAndProductsList(movies));
        long end_mysql = System.currentTimeMillis();
        result.put("time_mysql", ((double)(end_mysql - start_mysql))/1000);
        if(movies == null)
            result.put("number", 0);
        else{
            result.put("number", movies.size());
        }
        long start_hive = System.currentTimeMillis();
        try{
            List<Movie> movies1 = mySQLTimeService.getMoviesByTime(time);
            if(movies1 == null || movies1.size() == 0)
                new ArrayList<>();
            else
                mySQLCheckService.generateMovieAndProductsList(movies1);
        }catch (Exception e){
            System.out.println("HiveModel Exception!");
        }
        long end_hive = System.currentTimeMillis();
        result.put("time_hive", ((double)(end_hive - start_hive))/1000);
        return result;
    }
}
