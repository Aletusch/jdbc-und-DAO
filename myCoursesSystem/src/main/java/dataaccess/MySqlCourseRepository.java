package dataaccess;

import domain.Course;
import domain.CourseType;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlCourseRepository implements MyCourseRepository {
    private Connection connection;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {
//KÖNNTE DER PARAMETER FEHLEN!!!!!!!!!!!!!!!! Der lautet Connection connection
//KÖNNTE DER PARAMETER FEHLEN!!!!!!!!!!!!!!!! Der lautet Connection connection
//KÖNNTE DER PARAMETER FEHLEN!!!!!!!!!!!!!!!! Der lautet Connection connection
//KÖNNTE DER PARAMETER FEHLEN!!!!!!!!!!!!!!!! Der lautet Connection connection
//KÖNNTE DER PARAMETER FEHLEN!!!!!!!!!!!!!!!! Der lautet Connection connection

        this.connection = MysqlDatabaseConnection.getConnection("jdbc:mysql:// localhost:3306/kurssystem", "root", "");
    }


    @Override
    public Optional<Course> insert(Course entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Course> getById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Course> getAll() {
        String sql = "SELECT * FROM `courses`";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultset = preparedStatement.executeQuery();

            ArrayList<Course> courseList = new ArrayList<>();
            while (resultset.next()) {

                courseList.add(
                        new Course(
                                resultset.getLong("id"),
                                resultset.getString("name"),
                                resultset.getString("describtion"),
                                resultset.getInt("hours"),
                                resultset.getDate("begindate"),
                                resultset.getDate("enddate"),
                                CourseType.valueOf(resultset.getString("coursetype"))

                        )
                );
                return courseList;
            }
        } catch (SQLException e) {
            throw new DatabaseExcepton("Courselist error ocurred");

        }
        return null;
    }

    @Override
    public Optional<Course> update(Course entity) {
        return Optional.empty();
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public List<Course> findAllCoursesByName(String name) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByCourseType(CourseType courseType) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByDescribtion(String description) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByNameOrDescribtion(String searchtext) {
        return null;
    }

    @Override
    public List<Course> findAllCoursesByStartdate(Date startDate) {
        return null;
    }

    @Override
    public List<Course> findAllrunningCourses() {
        return null;
    }
}
