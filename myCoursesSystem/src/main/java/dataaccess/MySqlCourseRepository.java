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
        Assert.notNull(entity);
        try {
            String sql = "INSERT INTO `courses` (`name`, `description`, `hours`, `begindate`, `enddate`, `coursetype`) VALUES (?,?,?,?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getHours());
            preparedStatement.setDate(4, entity.getBeginDate());
            preparedStatement.setDate(5, entity.getEndDate());
            preparedStatement.setString(6, entity.getCourseType().toString());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                return Optional.empty();
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return this.getById(generatedKeys.getLong(1));
            } else {
                return Optional.empty();
            }
        } catch (SQLException sqlException) {
            throw new DatabaseException(sqlException.getMessage());
        }

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
        Assert.notNull(entity);

        String sql = "UPDATE `courses` SET `name` = ?, `description` = ?, `hours` = ?, `begindate` = ?, `enddate` = ?, `coursetype` = ? WHERE `courses`.`id` = ?";
        if (countCoursesinDBWithID(entity.getId()) == 0) {
            return Optional.empty();
        } else {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, entity.getName());
                preparedStatement.setString(2, entity.getDescription());
                preparedStatement.setInt(3, entity.getHours());
                preparedStatement.setDate(4, entity.getBeginDate());
                preparedStatement.setDate(5, entity.getEndDate());
                preparedStatement.setString(6, entity.getCourseType().toString());
                preparedStatement.setLong(7, entity.getId());

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    return Optional.empty();
                } else {
                    return this.getById(entity.getId());
                }
            } catch (SQLException sqlException) {
                throw new DatabaseException(sqlException.getMessage());
            }
        }
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
