package edu.tongji.sse.DataWarehouse.DAL.Hive;

import edu.tongji.sse.DataWarehouse.Model.Director;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


@Mapper
public interface HiveDirectorMapper {

    @Select("select count(name) from director")
    Integer getDirectorNum();

    @Select("select movies from director where name = \'${name}\'")
    String getMoviesIdByName(@Param(value = "name") String name);

    @Select("select * from director where name = \'${name}\'")
    Director getDirectorStyleByName(@Param(value = "name") String name);
}