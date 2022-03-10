package dataaccess;

import domain.Course;
import domain.CourseType;
import util.Assert;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySqlCourseRepository implements MyCourseRepository {
    private Connection connection;

    public MySqlCourseRepository() throws SQLException, ClassNotFoundException {
        this.connection = MysqlDatabaseConnection.getConnection("jdbc:mysql://localhost:3306/kurssystem", "root", "");
    }


    @Override
    public Optional<Course> insert(Course entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Course> getById(Long id) {
        Assert.notNull(id);
        if (countCoursesinDBWithID(id) == 0) {
            return Optional.empty();
        } else {
            try {
                String sql = "SELECT * FROM `courses` WHERE `id` = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setLong(1, id);
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                Course course = new Course(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getString("description"),
                        resultSet.getInt("hours"),
                        resultSet.getDate("beginDate"),
                        resultSet.getDate("endDate"),
                        CourseType.valueOf(resultSet.getString("coursetype"))
                );
                return Optional.of(course);
            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }

        }
    }

    private int countCoursesinDBWithID(Long id) {
        try {
            String sql = "SELECT COUNT(*) FROM `courses` WHERE `id`=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            int courseCount = resultSet.getInt(1);
            return courseCount;
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

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
                                resultset.getString("description"),
                                resultset.getInt("hours"),
                                resultset.getDate("begindate"),
                                resultset.getDate("enddate"),
                                CourseType.valueOf(resultset.getString("coursetype"))

                        )
                );

            }
            return courseList;
        } catch (SQLException e) {
            throw new DatabaseException("Courselist error ocurred");

        }
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
