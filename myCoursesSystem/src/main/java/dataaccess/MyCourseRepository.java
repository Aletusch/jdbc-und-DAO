package dataaccess;


import domain.Course;
import domain.CourseType;

import java.util.*;
import java.sql.Date;



public interface MyCourseRepository extends BaseRepository<Course,Long>{
    List<Course> findAllCoursesByName(String name);
    List<Course> findAllCoursesByCourseType(CourseType courseType);
    List<Course> findAllCoursesByDescribtion(String description);
    List<Course> findAllCoursesByNameOrDescription(String searchtext);
    List<Course> findAllCoursesByStartdate(Date startDate);
    List<Course> findAllrunningCourses();

}
