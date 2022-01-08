package com.zzx.mysql;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import joptsimple.internal.Strings;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.hasor.utils.StringUtils;

@Slf4j
public class demo {


    private static Pattern humpPattern = Pattern.compile("[A-Z]");

    private static Pattern linePattern = Pattern.compile("_(\\w)");
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://www.zzxn.club:3306/study?characterEncoding=UTF-8&serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "zzx123321";

    private static final String SQL = "SELECT * FROM ";// 数据库操作

    private static final String INSERT = "INSERT INTO %s(%s) VALUES(%s)";

    private static final String DELETE = "delete  from %s where %s";

    private static final String UPDATE = "update  %s  set %s where %s";

    private static final String QUERY = "SELECT * FROM %s where 1 =1 %s";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            log.error("can not load jdbc driver", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            log.error("get connection failure", e);
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        if(conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.error("close connection failure", e);
            }
        }
    }

    /**
     * 获取数据库下的所有表名
     */
    public static List<String> getTableNames() {
        List<String> tableNames = new ArrayList<>();
        Connection conn = getConnection();
        ResultSet rs = null;
        try {
            //获取数据库的元数据
            DatabaseMetaData db = conn.getMetaData();
            //从元数据中获取到所有的表名
            rs = db.getTables(conn.getCatalog(), "%", "%", new String[] { "TABLE" });
            while(rs.next()) {
                tableNames.add(rs.getString(3));
            }
        } catch (SQLException e) {
            log.error("getTableNames failure", e);
        } finally {
            try {
                rs.close();
                closeConnection(conn);
            } catch (SQLException e) {
                log.error("close ResultSet failure", e);
            }
        }
        return tableNames;
    }

    /**
     * 获取表中所有字段名称
     * @param tableName 表名
     * @return
     */
    public static List<String> getColumnNames(String tableName) {
        List<String> columnNames = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnNames.add(rsmd.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            log.error("getColumnNames failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnNames close pstem and connection failure", e);
                }
            }
        }
        return columnNames;
    }

    /**
     * 获取表中所有字段类型
     * @param tableName
     * @return
     */
    public static Map<String, String> getColumnTypes(String tableName) {
        Map<String, String> columnTypes = Maps.newHashMap();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        try {
            pStemt = conn.prepareStatement(tableSql);
            //结果集元数据
            ResultSetMetaData rsmd = pStemt.getMetaData();
            //表列数
            int size = rsmd.getColumnCount();
            for (int i = 0; i < size; i++) {
                columnTypes.put(rsmd.getColumnName(i + 1), rsmd.getColumnTypeName(i + 1));
            }
        } catch (SQLException e) {
            log.error("getColumnTypes failure", e);
        } finally {
            if (pStemt != null) {
                try {
                    pStemt.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnTypes close pstem and connection failure", e);
                }
            }
        }
        return columnTypes;
    }

    /**
     * 获取表中字段的所有注释
     * @param tableName
     * @return
     */
    public static List<String> getColumnComments(String tableName) {
        List<String> columnTypes = new ArrayList<>();
        //与数据库的连接
        Connection conn = getConnection();
        PreparedStatement pStemt = null;
        String tableSql = SQL + tableName;
        List<String> columnComments = new ArrayList<>();//列名注释集合
        ResultSet rs = null;
        try {
            pStemt = conn.prepareStatement(tableSql);
            rs = pStemt.executeQuery("show full columns from " + tableName);
            while (rs.next()) {
                columnComments.add(rs.getString("Comment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                    closeConnection(conn);
                } catch (SQLException e) {
                    log.error("getColumnComments close ResultSet and connection failure", e);
                }
            }
        }
        return columnComments;
    }

    public static String getPK(String tableName) {
        String PKName = null;
//        try {
//            DatabaseMetaData dmd = getConnection().getMetaData();
//            ResultSet rs = dmd.getPrimaryKeys(null, "%", tableName);
//            rs.next();
//            PKName = rs.getString("column_name");
//            rs.close();
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
                //方法二
        String sql = String.format("show index from %s", tableName);
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            rs.next();
            PKName = rs.getString("column_name");
            rs.close();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return PKName;
    }

    @SneakyThrows
    public static int insert(String table, String object, String where) {
        List<String> columnNames = getColumnNames(table);
        if (CollectionUtils.isEmpty(columnNames)) {
            log.info("table is error ！！");
            return 0;
        }
        String primaryKey = getPK(table);
        JSONObject json = parseObject(object);
        Set<String> valueKeySet = json.keySet();
        //过滤没有的字段
        Set<String> keys = columnNames.stream().filter(valueKeySet::contains)
                .filter(k -> !StringUtils.equalsIgnoreCase(k, primaryKey)).collect(Collectors.toSet());
        //value
        String value = keys.stream().map(k -> "?").collect(Collectors.joining(","));
        String sql = String.format(INSERT, table, String.join(",", keys), value);
        log.info("insert sql : {}", sql);
        PreparedStatement preparedStatement = getConnection().prepareStatement(sql);
        List<String> arrayList = Lists.newArrayList(keys);
        for (int i = 0; i < arrayList.size(); i++) {
            String s = arrayList.get(i);
            preparedStatement.setString(i + 1, json.getString(s));
        }
        return preparedStatement.executeUpdate();
    }

    public static int update(String table, String object, String where) throws SQLException {
        List<String> columnNames = getColumnNames(table);
        Map<String, String> columnTypes = getColumnTypes(table);
        JSONObject json = parseObject(object);
        String primaryKey = getPK(table);
        String set = columnNames.stream().filter(k -> !StringUtils.equalsIgnoreCase(k, primaryKey)).map(name -> {
            String value = json.getString(name);
            if (StringUtils.isEmpty(value)) {
                return Strings.EMPTY;
            }
            if (StringUtils.equalsIgnoreCase(columnTypes.get(name), "VARCHAR")) {
                value = "'" + value + "'";
            }
            return name + "=" + value;
        }).collect(Collectors.joining(","));
        if (StringUtils.isEmpty(where)) {
            where = primaryKey + " = " + json.getString(primaryKey);
        }
        String sql = String.format(UPDATE, table, set, where);
        log.info("update : {}", sql);
        return getConnection().prepareStatement(sql).executeUpdate();
    }

    public static String query(String table, String object, String where) throws SQLException {
        String sql = String.format(QUERY, table, where);
        List<Object> result = Lists.newArrayList();
        log.info("query sql : {}", sql);
        ResultSet resultSet = getConnection().prepareStatement(sql).executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int size = metaData.getColumnCount();
        while (resultSet.next()) {
            Map<String, String> map = Maps.newHashMap();
            for (int i = 0; i < size; i++) {
                map.put(lineToHump(metaData.getColumnName(i + 1)), resultSet.getString(i + 1));
            }
            result.add(map);
        }
        return JSON.toJSONString(result);
    }

    public static JSONObject parseObject(String json) {
        String humpToLine = humpToLine(json);
        return JSON.parseObject(humpToLine);
    }

    /** 下划线转驼峰 */
    public static String lineToHump(String str) {
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /** 驼峰转下划线,效率比上面高 */
    public static String humpToLine(String str) {
        Matcher matcher = humpPattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, "_" + matcher.group(0).toLowerCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void main(String[] args) throws SQLException {
        System.out.println(query("user", "", ""));
    }
}