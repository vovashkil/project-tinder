package com.tinder.DAO;

import com.tinder.Dto.Like;
import com.tinder.Dto.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LikeDaoSql implements DAO<Like> {
    private final Connection con;

    public LikeDaoSql(Connection con) {
        this.con = con;
    }

    @Override
    public Like get(int id) {
        return null;
    }

    @Override
    public List<Like> getAll() {

        List<Like> list = new ArrayList<>();

        final String sql = "SELECT * FROM likedlist";

        try (PreparedStatement stm = con.prepareStatement(sql);
             ResultSet resultSet = stm.executeQuery();) {

            while (resultSet.next()) {

                int likeId = resultSet.getInt("id");
                int userId = resultSet.getInt("userid");
                int userIdMarked = resultSet.getInt("marked_userid");
                boolean isLike = resultSet.getBoolean("islike");
                LocalDateTime likeTime = resultSet.getTimestamp("checktime").toLocalDateTime();
                list.add(new Like(likeId, userId, userIdMarked, isLike, likeTime));

            }

        } catch (SQLException e) {

            System.out.printf("Something went wrong: %s\n", e.getMessage());

        }

        return list;

    }

    @Override
    public boolean update(Like item) {

        boolean result = false;

        try {

            String insertQuery = "INSERT INTO likedlist (userid, marked_userid, islike) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(insertQuery);
            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getUserIdMarked());
            ps.setBoolean(3, true);
            ps.executeUpdate();
            ps.close();

            result = true;

        } catch (SQLException e) {

            System.out.printf("Something went wrong: %s\n", e.getMessage());

        }

        return result;

    }

    @Override
    public boolean remove(Like item) {
        return false;
    }

    @Override
    public boolean remove(int index) {
        return false;
    }

    public List<Like> getAllLiked(int userid) {

        List<Like> list = new ArrayList<>();

        final String sql = "SELECT * FROM likedlist where userid = ?";

        try (PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, userid);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                list.add(
                        new Like(resultSet.getInt("id")
                                , resultSet.getInt("userid")
                                , resultSet.getInt("marked_userid")
                                , resultSet.getBoolean("islike")
                                , resultSet.getTimestamp("checktime").toLocalDateTime())
                );
            }
        } catch (SQLException e) {
            System.out.printf("Something went wrong: %s\n", e.getMessage());
        }
        return list;
    }

    public List<User> getUsersLiked(int userid) {

        List<User> list = new ArrayList<>();

        final String sql = "" +
                "select distinct u.*\n" +
                "from users u\n" +
                "   inner join likedlist ll on u.id = ll.marked_userid\n" +
                "where ll.userid = ? and ll.islike = true\n" +
                "order by u.firstname, u.lastname";

        try (PreparedStatement stm = con.prepareStatement(sql)) {
            stm.setInt(1, userid);
            ResultSet resultSet = stm.executeQuery();
            while (resultSet.next()) {
                list.add(
                        new User(
                                resultSet.getInt("id"),
                                resultSet.getString("login"),
                                resultSet.getString("firstname"),
                                resultSet.getString("lastname"),
                                resultSet.getString("password"),
                                resultSet.getString("photolink"))
                );
            }
        } catch (SQLException e) {
            System.out.printf("Something went wrong: %s\n", e.getMessage());
        }
        return list;
    }
}
