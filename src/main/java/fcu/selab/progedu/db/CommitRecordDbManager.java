package fcu.selab.progedu.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CommitRecordDbManager {
  UserDbManager userDbManager = UserDbManager.getInstance();
  private static final String COUNT_STATUS = "count(status)";
  private static final String FIELD_NAME_STATUS = "status";
  private static CommitRecordDbManager dbManager = new CommitRecordDbManager();
  private static CommitStatusDbManager CSdbManager = new CommitStatusDbManager();

  public static CommitRecordDbManager getInstance() {
    return dbManager;
  }

  private IDatabase database = new MySqlDatabase();

  /**
   * insert student commit records into db
   * 
   * @param id           studrnt id
   * @param auId         auId
   * @param commitNumber commitNumber
   * @param status       status Id
   * @param time         commit time
   * @return check
   */
  public boolean insertCommitRecord(int id, int auId, int commitNumber, int status, String time) {
    String sql = "INSERT INTO Commit_Record" + "(id, auId, commitNumber, status, date, time) "
        + "VALUES(?, ?, ?, ?, ?)";
    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      preStmt.setInt(2, auId);
      preStmt.setInt(3, commitNumber);
      preStmt.setInt(4, status);
      preStmt.setString(5, time);
      preStmt.executeUpdate();
      check = true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

//  /**
//   * get all counts
//   *
//   * @return counts
//   */
//  public List<Integer> getCounts(int status) {
//    String query = "SELECT auId,count(status) FROM Commit_Record where status like ? "+
//    "group by auId";
//    List<Integer> array = new ArrayList<>();
//
//    try (Connection conn = database.getConnection();
//        PreparedStatement preStmt = conn.prepareStatement(query)) {
//      preStmt.setInt(1, status);
//      try (ResultSet rs = preStmt.executeQuery();) {
//        while (rs.next()) {
//          array.add(rs.getInt(COUNT_STATUS));
//        }
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return array;
//  }

//  /**
//   * get each hw's CommitRecordStateCounts
//   * 
//   * @param auId Commit_Record auId
//   * @return map
//   */
//  public Map<String, Integer> getCommitRecordStateCounts(int auId) {
//    String query = "SELECT auid,status,count(status) FROM Commit_Record where auId = ? "
//        + "group by status";
//    Map<String, Integer> map = new HashMap<>();
//
//    try (Connection conn = database.getConnection();
//        PreparedStatement preStmt = conn.prepareStatement(query)) {
//      preStmt.setInt(1, auId);
//      try (ResultSet rs = preStmt.executeQuery();) {
//        while (rs.next()) {
//          map.put(CSdbManager.getCommitStatusName(rs.getInt("status")), rs.getInt(COUNT_STATUS));
//        }
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return map;
//  }

//  /**
//   * get each hw's CommitRecordStateCounts
//   * 
//   * @param auId Commit_Record auId
//   * @param num  num
//   * @return status
//   */
//  public String getCommitRecordStatus(int auId, int num) {
//    String status = "";
//    String query = "SELECT status FROM Commit_Record where auId = ? and limit ?,1";
//
//    try (Connection conn = database.getConnection();
//        PreparedStatement preStmt = conn.prepareStatement(query)) {
//      preStmt.setInt(1, auId);
//      preStmt.setInt(2, num - 1);
//
//      try (ResultSet rs = preStmt.executeQuery();) {
//        if (rs.next()) {
//          status = rs.getString(FIELD_NAME_STATUS);
//        }
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//
//    return status;
//  }

  /**
   * check if record is in db
   *
   * @param id   student id
   * @param auId auId
   * @param time commit time
   * @return boolean
   */
  public boolean checkRecord(int id, int auId, String time) {
    String query = "SELECT * FROM Commit_Record where id=? and auId=? and time=?";

    boolean check = false;

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(query)) {
      preStmt.setInt(1, id);
      preStmt.setInt(2, auId);
      preStmt.setString(3, time);
      try (ResultSet rs = preStmt.executeQuery();) {
        while (rs.next()) {
          check = true;
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return check;
  }

  /**
   * update record status
   * 
   * @param id     student
   * @param auId   auId
   * @param status status
   * @param time   time
   */
  public void updateRecordStatus(int id, int auId, int status, String time) {
    String sql = "UPDATE Commit_Record SET status=? where id=? and auId=? and time=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, status);
      preStmt.setInt(2, id);
      preStmt.setInt(3, auId);
      preStmt.setString(4, time);

      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

//  /**
//   * get Count Group By Hw And Time
//   * 
//   * @param hw hw number
//   * @return records
//   */
//  public JSONArray getCountGroupByHwAndTime(String hw) {
//    String status = StatusEnum.INITIALIZATION.getTypeName();
//    String query = "select date, time, count(status) from Commit_Record where hw=?  and status!="
//        + "'" + status + "'" + "group by date, time";
//    JSONArray records = new JSONArray();
//
//    try (Connection conn = database.getConnection();
//        PreparedStatement preStmt = conn.prepareStatement(query)) {
//      preStmt.setString(1, hw);
//      try (ResultSet rs = preStmt.executeQuery();) {
//        while (rs.next()) {
//          String date = rs.getString("date");
//          String time = rs.getString("time");
//          String[] times = time.split(":");
//          final double timeValue = Integer.valueOf(times[0]) + (Integer.valueOf(times[1])) * 0.01;
//
//          Timestamp ts = new Timestamp(System.currentTimeMillis());
//          ts = Timestamp.valueOf(date + " " + time);
//
//          Timestamp xlabel = new Timestamp(System.currentTimeMillis());
//          xlabel = Timestamp.valueOf(date + " " + time);
//          xlabel.setHours(0);
//          xlabel.setMinutes(0);
//          xlabel.setSeconds(0);
//          int count = rs.getInt(COUNT_STATUS);
//          JSONObject record = new JSONObject();
//          record.put("x", xlabel.getTime());
//          record.put("y", timeValue);
//          record.put("r", count);
//          record.put("t", ts.getTime());
//          records.put(record);
//        }
//      }
//
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
//    return records;
//  }

  /**
   * get commit count by auId
   * 
   * 
   * @param id auId
   * @return aId assignment Id
   */
  public int getCommitCount(int id) {
    int count = 0;
    String sql = "SELECT * FROM Commit_Record WHERE auId=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          count = rs.getInt("count(auId)");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  /**
   * delete built record of specific auId
   *
   * @param auId auId
   */
  public void deleteRecord(int auId) {
    String sql = "DELETE FROM Commit_Record WHERE auId=?";

    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, auId);
      preStmt.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * get Commit_Status id
   * 
   * @param statusName Commit_Status statusName
   * @return id status id
   */
  public int getCommitStatusId(String statusName) {
    int id = 0;
    String sql = "SELECT * FROM Commit_Status WHERE status=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setString(1, statusName);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          id = rs.getInt("id");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return id;
  }

  /**
   * get Commit_Status name
   * 
   * @param id Commit_Status id
   * @return name Commit_Status name
   */
  public String getCommitStatusName(int id) {
    String statusName = null;
    String sql = "SELECT * FROM Commit_Status WHERE id=?";
    try (Connection conn = database.getConnection();
        PreparedStatement preStmt = conn.prepareStatement(sql)) {
      preStmt.setInt(1, id);
      try (ResultSet rs = preStmt.executeQuery()) {
        while (rs.next()) {
          statusName = rs.getString("status");
        }
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return statusName;
  }
}
